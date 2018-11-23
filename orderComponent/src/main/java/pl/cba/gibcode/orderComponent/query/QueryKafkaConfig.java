package pl.cba.gibcode.orderComponent.query;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.Serialized;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import pl.cba.gibcode.modelLibrary.brand.Brand;
import pl.cba.gibcode.modelLibrary.card.Card;
import pl.cba.gibcode.modelLibrary.config.CustomJsonSerde;
import pl.cba.gibcode.modelLibrary.model.PriceRangeEnum;
import pl.cba.gibcode.modelLibrary.query.ImmutableQueryBrand;
import pl.cba.gibcode.modelLibrary.query.QueryBrand;
import pl.cba.gibcode.orderComponent.command.model.CardWrapperAggregator;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Set;

import static java.util.Objects.isNull;

@Configuration
public class QueryKafkaConfig {
	private static final Logger logger = LoggerFactory.getLogger(QueryKafkaConfig.class);


	public void  cardStream(
			StreamsBuilder streamsBuilder) {

		KStream<String, Card> cardStream = streamsBuilder
				.stream("card", CustomJsonSerde.consume(Card.class));

		KStream<String, Brand> brandStream = streamsBuilder
				.stream("brand", CustomJsonSerde.consume(Brand.class));

		var cardAggregation = cardStream
				.groupByKey(Serialized.with(Serdes.String(), CustomJsonSerde.of(Card.class))).reduce((card, v1) -> v1)
				.toStream()
				.selectKey((s, card) -> card.getBrandName()).groupByKey(Serialized.with(Serdes.String(), CustomJsonSerde.of(Card.class)))
				.aggregate(CardWrapperAggregator::new, (s, card, cardWrapperAggregator) -> {
					logger.info("Aggregator {} {} {}", s, card, cardWrapperAggregator);
					if(!card.getDeleted() && card.getIsAvailable()) {
						logger.info("card not delete and is available");
						var discount = calculateDiscount(card.getMarketValue(), card.getPrice()).doubleValue();
						cardWrapperAggregator.getDiscounts().put(card.getCardUuid().toString(), discount);
						cardWrapperAggregator.getCardTypes().add(card.getCardType());
						cardWrapperAggregator.getPriceRanges()
								.add(PriceRangeEnum.decideRangeFrom(card.getPrice().doubleValue()));
					}else{
						cardWrapperAggregator.getDiscounts().remove(card.getCardUuid().toString());
					}
					return cardWrapperAggregator;
				}, Materialized.with(Serdes.String(), CustomJsonSerde.of(CardWrapperAggregator.class)));

		var queryBrandStream = brandStream.groupByKey(Serialized.with(Serdes.String(), CustomJsonSerde.of(Brand.class))).reduce((brand, v1) -> v1)
				.leftJoin(
						cardAggregation
						, (brand, cardWrapperAggregator) -> {
							logger.info("Left join {}, {}", brand, cardWrapperAggregator);
							var queryBrand = ImmutableQueryBrand.builder().brand(brand).isDeleted(false);
							if(brand.getDeleted()){
								queryBrand.isAvailable(false);
								queryBrand.isDeleted(true);
								queryBrand.maxDiscount(0.0);
								queryBrand.addAllCardTypes(Set.of());
								queryBrand.addAllPriceRanges(Set.of());
								return (QueryBrand) queryBrand.build();
							}
							if(isNull(cardWrapperAggregator) || cardWrapperAggregator.getDiscounts().isEmpty()) {
								queryBrand.isAvailable(false);
								queryBrand.maxDiscount(0.0);
								queryBrand.addAllCardTypes(Set.of());
								queryBrand.addAllPriceRanges(Set.of());
							} else {
								queryBrand.isAvailable(true);
								queryBrand.addAllPriceRanges(cardWrapperAggregator.getPriceRanges());
								queryBrand.addAllCardTypes(cardWrapperAggregator.getCardTypes());

								var maxDiscount = cardWrapperAggregator.getDiscounts().values().stream().max(Double::compare).get();
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