/*
package pl.cba.gibcode.orderComponent.command.config;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.cba.gibcode.modelLibrary.brand.Brand;
import pl.cba.gibcode.modelLibrary.card.Card;
import pl.cba.gibcode.modelLibrary.config.CustomJsonSerde;
import pl.cba.gibcode.modelLibrary.config.KafkaConfig;
import pl.cba.gibcode.modelLibrary.exceptions.BusinessException;
import pl.cba.gibcode.modelLibrary.model.*;
import pl.cba.gibcode.modelLibrary.ordercomponent.ImmutableOrderHeader;
import pl.cba.gibcode.modelLibrary.ordercomponent.OrderComponentEvent;
import pl.cba.gibcode.modelLibrary.ordercomponent.OrderType;
import pl.cba.gibcode.orderComponent.command.service.CommandService;
import pl.cba.gibcode.orderComponent.command.service.StateMachine;
import pl.cba.gibcode.orderComponent.query.QueryKafkaConfig;

@Configuration
public class CommandKafkaConfig extends KafkaConfig {
	private static final Logger logger = LoggerFactory.getLogger(CommandKafkaConfig.class);
	private CommandService commandService;
	private final QueryKafkaConfig queryKafkaConfig;

	public CommandKafkaConfig(
			CommandService commandService,
			QueryKafkaConfig queryKafkaConfig) {
		this.commandService = commandService;
		this.queryKafkaConfig = queryKafkaConfig;
	}

	@Override protected String getApplicationId() {
		return "orderComponent";
	}

	@Bean
	public KStream<String, OrderComponentEvent> orderComponentStream(
			StreamsBuilder streamsBuilder,
			KStream<String, Brand> brandStream, KStream<String, Card> cardStream, KStream<String, Order> orderStream) {

		KStream<String, OrderComponentEvent> orderComponentStream = streamsBuilder
				.stream("orderComponentTopic", CustomJsonSerde.consume(OrderComponentEvent.class));

		KStream<String, OrderComponentEvent>[] orderComponentStreams =
				orderComponentStream.branch(
						(k, v) -> OrderType.CARD.equals(v.getHeader().getType()),
						(k, v) -> OrderType.BRAND.equals(v.getHeader().getType()));

		KStream<String, OrderComponentEvent> cardBranch = orderComponentStreams[0];
		KStream<String, OrderComponentEvent> brandBranch = orderComponentStreams[1];


		brandBranch.leftJoin(getLastBrand(brandStream), createOrUpdateEntity(), Joined.with(Serdes.String(), CustomJsonSerde.of(OrderComponentEvent.class), CustomJsonSerde.of(Brand.class)))
				.selectKey(((s, order) -> order.getHeader().getOrderUuid().toString()))
				.to("orderValidationStream", CustomJsonSerde.produce(Order.class));


		cardBranch.leftJoin(getLastCard(cardStream), createOrUpdateEntity())
				.selectKey(((s, order) -> order.getHeader().getOrderUuid().toString()))
				.to("orderValidationStream", CustomJsonSerde.produce(Order.class));

		return orderComponentStream;
	}


	@Bean
	public KStream<String, Order> orderStream(StreamsBuilder streamsBuilder) {
		var orderStream = streamsBuilder
				.stream("orderStream", CustomJsonSerde.consume(Order.class))
				.peek((k, v) -> {
					logger.info("Received orderStream: {} {}", k, v);
				});

		orderStream.filter((k, v) -> !EntityStateEnum.FAILED.equals(v.getHeader().getState())
				&& OrderType.CARD.equals(v.getHeader().getType())
				&& !v.getHasWrongState().isPresent())
				.mapValues((k, v) -> getCardFromSuccessBody(v))
				.selectKey(((s, card) -> card.getCardUuid().toString()))
				.to("cardStream", CustomJsonSerde.produce(Card.class));

		orderStream.filter((k, v) -> !EntityStateEnum.FAILED.equals(v.getHeader().getState())
				&& OrderType.BRAND.equals(v.getHeader().getType())
				&& !v.getHasWrongState().isPresent())
				.mapValues((k, v) -> getBrandFromSuccessBody(v))
				.selectKey(((s, brand) -> brand.getBrandId()))
				.to("brandStream", CustomJsonSerde.produce(Brand.class));
		return orderStream;
	}

	@Bean
	public KStream<String, Card> cardStream(StreamsBuilder streamsBuilder) {
		var cardStream = streamsBuilder
				.stream("cardStream", CustomJsonSerde.consume(Card.class))
				.peek((k, v) -> logger.info("Received cardStream: {} {}", k, v));
		return cardStream;
	}

	@Bean
	public KStream<String, Order> orderValidationStream(StreamsBuilder streamsBuilder, KStream<String, Order> orderStream) {
		var orderValidationStream = streamsBuilder
				.stream("orderValidationStream", CustomJsonSerde.consume(Order.class))
				.peek((k, v) -> logger.info("Received orderValidationStream: {} {}", k, v));

		orderValidationStream
				.selectKey(((s, order) -> order.getHeader().getEntityId()))
				.leftJoin(orderStream.groupBy((k, v) -> v.getHeader().getEntityId(), Serialized.with(Serdes.String(), CustomJsonSerde.of(Order.class))).reduce(getLatestEntity()), ((order, existingOrder) -> {
			if(existingOrder != null && !StateMachine.getPossibleTransitions(existingOrder.getHeader().getState()).contains(order.getHeader().getState())){
				logger.info("Incoming order is in wrong state. Going to skip new order {}", order);
				return ImmutableOrder.copyOf(existingOrder).withHeader(
						ImmutableOrderHeader.copyOf(existingOrder.getHeader()).withOrderUuid(order.getHeader().getOrderUuid()))
						.withHasWrongState(true);
			}
			else return order;
		}), Joined.with(Serdes.String(), CustomJsonSerde.of(Order.class),  CustomJsonSerde.of(Order.class)))
				.selectKey((k, v) -> v.getHeader().getOrderUuid().toString())
				.to("orderStream", CustomJsonSerde.produce(Order.class));
		return orderValidationStream;
	}



	@Bean
	public KStream<String, Brand> brandStream(StreamsBuilder streamsBuilder) {
		var brandStream = streamsBuilder
				.stream("brandStream", CustomJsonSerde.consume(Brand.class))
				.peek((k, v) -> logger.info("Received brandStream: {} {}", k, v));
		//queryKafkaConfig.brandByCategories(brandStream);
		return brandStream;
	}

	private Brand getBrandFromSuccessBody(Order v) {
		var successBody = (SuccessBody) v.getBody();
		return successBody.getBrand().orElseThrow(() -> new BusinessException(String
				.format("Brand was not forwarded into SuccessBody %s", successBody)));
	}

	private Card getCardFromSuccessBody(Order v) {
		var successBody = (SuccessBody) v.getBody();
		return successBody.getCard().orElseThrow(() -> new BusinessException(String
				.format("Card was not forwarded into SuccessBody %s", successBody)));
	}

	private ValueJoiner<OrderComponentEvent, EntityFragment, Order> createOrUpdateEntity() {
		return (orderComponentEvent, order) -> order == null ?
				commandService.createNewEntity(orderComponentEvent) :
				commandService
						.updateEntity(orderComponentEvent, order);
	}

	private KTable<String, Brand> getLastBrand(KStream<String, Brand> brandStream) {
		return brandStream.filter((k, v) -> !v.getDeleted()).groupByKey().reduce(getLatestEntity(), CustomJsonSerde.materialize(Brand.class));
	}

	private KTable<String, Card> getLastCard(KStream<String, Card> cardStream) {
		return cardStream.filter((k, v) -> !v.getDeleted()).groupByKey().reduce(getLatestEntity(), CustomJsonSerde.materialize(Card.class));
	}

	private <T> Reducer<T> getLatestEntity() {
		return (l, r) -> r;
	}


}
*/
