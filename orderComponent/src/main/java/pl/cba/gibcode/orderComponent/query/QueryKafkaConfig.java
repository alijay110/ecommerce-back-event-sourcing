package pl.cba.gibcode.orderComponent.query;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.kstream.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import pl.cba.gibcode.modelLibrary.brand.Brand;
import pl.cba.gibcode.modelLibrary.card.Card;
import pl.cba.gibcode.modelLibrary.config.CustomJsonSerde;
import pl.cba.gibcode.modelLibrary.model.JoinedCardWithOrder;
import pl.cba.gibcode.modelLibrary.model.Order;
import pl.cba.gibcode.modelLibrary.model.PriceRangeEnum;
import pl.cba.gibcode.modelLibrary.query.*;
import pl.cba.gibcode.orderComponent.command.model.CardAggregationEntry;
import pl.cba.gibcode.orderComponent.command.model.CardByBrandAggregationEntry;
import pl.cba.gibcode.orderComponent.command.model.CardByBrandWrapperAggregator;
import pl.cba.gibcode.orderComponent.command.model.CardWrapperAggregator;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

@Configuration
public class QueryKafkaConfig {
	private static final Logger logger = LoggerFactory.getLogger(QueryKafkaConfig.class);

	public void prepareQueries(
			KStream<String, Order> orderStream,
			KStream<String, Brand> brandStream,
			KStream<String, Card> cardStream) {

		var brandsKTable = brandStream.groupByKey(Serialized.with(
				Serdes.String(),
				CustomJsonSerde.of(Brand.class)))
				.reduce((brand, v1) -> v1);

		var orderKTableGroupedByEntityId = orderStream.groupBy(
				(s, order) -> order.getEntityId(),
				Serialized.with(Serdes.String(), CustomJsonSerde.of(Order.class)))
				.reduce((order, v1) -> v1);

		var cardsKTable = cardStream
				.groupByKey(Serialized.with(Serdes.String(), CustomJsonSerde.of(Card.class))).reduce((card, v1) -> v1);

		queryBrand(cardsKTable, brandsKTable);
		queryCards(cardsKTable, brandsKTable, orderKTableGroupedByEntityId);
		queryOrders(orderStream, cardsKTable);
	}

	private void queryOrders(
			KStream<String, Order> orderStream,
			KTable<String, Card> cardsKTable) {
		var joinedOrderWithCardStream = orderStream.selectKey((s, order) -> order.getEntityId())
				.leftJoin(
						cardsKTable,
						(order, card) -> {
							logger.info("QueryOrder Lef Join {} {}", order, card);
							if(isNull(card)) {
								return null;
							}
							var joinedCardWithOrder = new JoinedCardWithOrder();
							joinedCardWithOrder.setCard(card);
							joinedCardWithOrder.setOrder(order);
							joinedCardWithOrder.setDiscount(calculateDiscount(card.getMarketValue(), card.getPrice())
									.doubleValue());
							return joinedCardWithOrder;
						},
						Joined.with(Serdes.String(), CustomJsonSerde.of(Order.class), CustomJsonSerde.of(Card.class)))
				.filter((s, joinedCardWithOrder) -> nonNull(joinedCardWithOrder));

		joinedOrderWithCardStream
				.groupBy(
						(s, joinedCardWithOrder) -> joinedCardWithOrder.getCard().getSellerId().toString(),
						Serialized.with(Serdes.String(), CustomJsonSerde.of(JoinedCardWithOrder.class)))
				.aggregate(QuerySellerOrders::new, (s, joinedCardWithOrder, queryOrder) -> {
					logger.info("QueryOrder seller Aggregator {} {} {}", s, joinedCardWithOrder, queryOrder);
					queryOrder.getJoinedCardWithOrderList().add(joinedCardWithOrder);
					return queryOrder;
				}, Materialized.with(Serdes.String(), CustomJsonSerde.of(QuerySellerOrders.class))).toStream()
				.to("sellerOrder", CustomJsonSerde.produce(QuerySellerOrders.class));

		joinedOrderWithCardStream
				.filter((s, joinedCardWithOrder) -> joinedCardWithOrder.getOrder().getBuyerId().isPresent())
				.groupBy(
						(s, joinedCardWithOrder) -> joinedCardWithOrder.getOrder().getBuyerId().get().toString(),
						Serialized.with(Serdes.String(), CustomJsonSerde.of(JoinedCardWithOrder.class)))
				.aggregate(QueryBuyerOrders::new, (s, joinedCardWithOrder, queryOrder) -> {
					logger.info("QueryOrder buyer Aggregator {} {} {}", s, joinedCardWithOrder, queryOrder);
					queryOrder.getJoinedCardWithOrderList().add(joinedCardWithOrder);
					return queryOrder;
				}, Materialized.with(Serdes.String(), CustomJsonSerde.of(QueryBuyerOrders.class))).toStream()
				.to("buyerOrder", CustomJsonSerde.produce(QueryBuyerOrders.class));
	}

	private void queryCards(
			KTable<String, Card> cardsKTable,
			KTable<String, Brand> brandKTable,
			KTable<String, Order> orderKTableGroupedByEntityId) {

		var joinedCardWithOrderStream = cardsKTable
				.toStream()
				.leftJoin(
						orderKTableGroupedByEntityId,
						(card, order) -> {
							var joinedCardWithOrder = new JoinedCardWithOrder();
							joinedCardWithOrder.setCard(card);
							joinedCardWithOrder.setOrder(order);
							joinedCardWithOrder.setDiscount(calculateDiscount(card.getMarketValue(), card.getPrice())
									.doubleValue());
							return joinedCardWithOrder;
						});
		joinedCardWithOrderStream.to("joinedCardWithOrder", CustomJsonSerde.produce(JoinedCardWithOrder.class));

		var cardAggregation = joinedCardWithOrderStream
				.selectKey((s, cardWrapper) -> cardWrapper.getCard().getBrandName())
				.groupByKey(Serialized.with(Serdes.String(), CustomJsonSerde.of(JoinedCardWithOrder.class)))
				.aggregate(CardByBrandWrapperAggregator::new, (s, cardWrapper, cardWrapperAggregator) -> {
					var card = cardWrapper.getCard();
					if(!card.getDeleted() && card.getIsAvailable()) {
						var entry = new CardByBrandAggregationEntry();
						entry.setCard(cardWrapper);
						entry.setPriceRangeEnum(PriceRangeEnum.decideRangeFrom(card.getPrice().doubleValue()));
						cardWrapperAggregator.getEntries().put(card.getCardUuid().toString(), entry);
					} else {
						cardWrapperAggregator.getEntries().remove(card.getCardUuid().toString());
					}
					logger.info("Aggregator {} {} {}", s, card, cardWrapperAggregator);
					return cardWrapperAggregator;
				}, Materialized.with(Serdes.String(), CustomJsonSerde.of(CardByBrandWrapperAggregator.class)));

		var queryCardsByBrand = brandKTable
				.leftJoin(
						cardAggregation,
						(brand, cardWrapperAggregator) -> {
							logger.info("Left join {}, {}", brand, cardWrapperAggregator);
							var cardsByBrand = ImmutableQueryCardsByBrand.builder().isDeleted(false);
							if(brand.getDeleted()) {
								cardsByBrand.isAvailable(false);
								cardsByBrand.isDeleted(true);
								return (QueryCardsByBrand) cardsByBrand.build();
							}
							if(isNull(cardWrapperAggregator) || cardWrapperAggregator.getEntries().isEmpty()) {
								cardsByBrand.isAvailable(false);
							} else {
								cardsByBrand.isAvailable(true);
								cardsByBrand.cards(cardWrapperAggregator.getEntries().values().stream()
										.map(CardByBrandAggregationEntry::getCard).collect(
												Collectors.toList()));
								cardsByBrand.addAllPriceRanges(
										cardWrapperAggregator
												.getEntries()
												.values()
												.stream()
												.map(CardByBrandAggregationEntry::getPriceRangeEnum).distinct().collect(
												Collectors.toList()));
							}
							return (QueryCardsByBrand) cardsByBrand.build();
						}).toStream();

		queryCardsByBrand.to("queryCardsByBrand", CustomJsonSerde.produce(QueryCardsByBrand.class));
	}

	private void queryBrand(KTable<String, Card> cardsKTable, KTable<String, Brand> brandKTable) {
		var cardAggregation = cardsKTable
				.toStream()
				.selectKey((s, card) -> card.getBrandName())
				.groupByKey(Serialized.with(Serdes.String(), CustomJsonSerde.of(Card.class)))
				.aggregate(CardWrapperAggregator::new, (s, card, cardWrapperAggregator) -> {
					logger.info("Aggregator {} {} {}", s, card, cardWrapperAggregator);
					if(!card.getDeleted() && card.getIsAvailable()) {
						var discount = calculateDiscount(card.getMarketValue(), card.getPrice()).doubleValue();
						var entry = new CardAggregationEntry();
						entry.setDiscount(discount);
						entry.setCardTypeEnum(card.getCardType());
						entry.setPriceRangeEnum(PriceRangeEnum.decideRangeFrom(card.getPrice().doubleValue()));
						cardWrapperAggregator.getEntries().put(card.getCardUuid().toString(), entry);
					} else {
						cardWrapperAggregator.getEntries().remove(card.getCardUuid().toString());
					}
					return cardWrapperAggregator;
				}, Materialized.with(Serdes.String(), CustomJsonSerde.of(CardWrapperAggregator.class)));

		var queryBrandStream = brandKTable
				.leftJoin(
						cardAggregation,
						(brand, cardWrapperAggregator) -> {
							logger.info("Left join {}, {}", brand, cardWrapperAggregator);
							var queryBrand = ImmutableQueryBrand.builder().brand(brand).isDeleted(false);
							if(brand.getDeleted()) {
								queryBrand.isAvailable(false);
								queryBrand.isDeleted(true);
								queryBrand.maxDiscount(0.0);
								queryBrand.addAllCardTypes(Set.of());
								queryBrand.addAllPriceRanges(Set.of());
								return (QueryBrand) queryBrand.build();
							}
							if(isNull(cardWrapperAggregator) || cardWrapperAggregator.getEntries().isEmpty()) {
								queryBrand.isAvailable(false);
								queryBrand.maxDiscount(0.0);
								queryBrand.addAllCardTypes(Set.of());
								queryBrand.addAllPriceRanges(Set.of());
							} else {
								queryBrand.isAvailable(true);
								queryBrand.addAllPriceRanges(
										cardWrapperAggregator
												.getEntries()
												.values()
												.stream()
												.map(CardAggregationEntry::getPriceRangeEnum).distinct().collect(
												Collectors.toList()));
								queryBrand.addAllCardTypes(
										cardWrapperAggregator
												.getEntries()
												.values()
												.stream()
												.map(CardAggregationEntry::getCardTypeEnum).distinct().collect(
												Collectors.toList()));

								var maxDiscount = cardWrapperAggregator.getEntries().values().stream().map(
										CardAggregationEntry::getDiscount).max(Double::compare).get();
								queryBrand.maxDiscount(maxDiscount);
							}
							return (QueryBrand) queryBrand.build();
						}).toStream();
		queryBrandStream.to("queryBrand", CustomJsonSerde.produce(QueryBrand.class));
	}

	private BigDecimal calculateDiscount(BigDecimal marketValue, BigDecimal price) {
		return BigDecimal.ONE.subtract(price.divide(marketValue, 8, RoundingMode.CEILING));
	}

}