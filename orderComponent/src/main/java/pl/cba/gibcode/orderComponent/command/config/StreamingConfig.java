package pl.cba.gibcode.orderComponent.command.config;

import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.ValueTransformerWithKey;
import org.apache.kafka.streams.processor.ProcessorContext;
import org.apache.kafka.streams.state.KeyValueStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import pl.cba.gibcode.modelLibrary.brand.Brand;
import pl.cba.gibcode.modelLibrary.card.Card;
import pl.cba.gibcode.modelLibrary.config.CustomJsonSerde;
import pl.cba.gibcode.modelLibrary.config.KafkaConfig;
import pl.cba.gibcode.modelLibrary.model.Order;
import pl.cba.gibcode.modelLibrary.ordercomponent.OrderComponentEvent;
import pl.cba.gibcode.modelLibrary.ordercomponent.OrderType;
import pl.cba.gibcode.orderComponent.command.model.ModelBuilderUtils;
import pl.cba.gibcode.orderComponent.command.model.ProcessingWrapper;
import pl.cba.gibcode.orderComponent.command.service.StateMachine;
import pl.cba.gibcode.orderComponent.command.strategy.ProcessingStrategyContext;
import pl.cba.gibcode.orderComponent.query.QueryKafkaConfig;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static pl.cba.gibcode.modelLibrary.model.EntityStateEnum.UNKNOWN;
import static pl.cba.gibcode.orderComponent.command.config.LocalStateStoresConfig.*;

@Component
public class StreamingConfig  extends KafkaConfig {

	private static final Logger logger = LoggerFactory.getLogger(StreamingConfig.class);

	private final ProcessingStrategyContext processingStrategyContext;
	private final QueryKafkaConfig queryKafkaConfig;
	public StreamingConfig(
			ProcessingStrategyContext processingStrategyContext,
			QueryKafkaConfig queryKafkaConfig) {
		this.processingStrategyContext = processingStrategyContext;
		this.queryKafkaConfig = queryKafkaConfig;
	}

	@Bean
	public KStream<String, OrderComponentEvent> orderComponentStream(
			StreamsBuilder streamsBuilder){
		KStream<String, OrderComponentEvent> orderComponentStream = streamsBuilder
				.stream("orderComponentEvent", CustomJsonSerde.consume(OrderComponentEvent.class));


		var transformedStream =
				orderComponentStream
						.peek((key, value) -> logger.info("orderComponentEvent: {}: {}", key, value))
						.selectKey((k, v)-> v.getHeader().getEntityId())
				.transformValues(Transformer::new,
						ORDER_COMPONENT_EVENT_STORE,ORDER_STORE,BRAND_STORE,CARD_STORE);

		transformedStream.mapValues((s, processingWrapper) -> processingWrapper.getResponse())
				.selectKey(((s, order) -> order.getOrderUuid().toString()))
				.peek((key, value) -> logger.info("order: {}: {}", key, value))
				.to("order", CustomJsonSerde.produce(Order.class));

		transformedStream.mapValues((s, processingWrapper) -> processingWrapper.getBrand())
				.filter(((s, brand) -> nonNull(brand)))
				.peek((key, value) -> logger.info("brand: {}: {}", key, value))
				.to("brand", CustomJsonSerde.produce(Brand.class));


		transformedStream.mapValues((s, processingWrapper) -> processingWrapper.getCard())
				.filter(((s, card) -> nonNull(card)))
				.peek((key, value) -> logger.info("card: {}: {}", key, value))
				.to("card", CustomJsonSerde.produce(Card.class));
		queryKafkaConfig.cardStream(streamsBuilder);
		return orderComponentStream;
	}

	@Override protected String getApplicationId() {
		return "orderComponent";
	}

	private class Transformer implements
			ValueTransformerWithKey<String, OrderComponentEvent, ProcessingWrapper> {
		private KeyValueStore<String, OrderComponentEvent> orderComponentStore;
		private KeyValueStore<String, Order> orderStore;
		private KeyValueStore<String, Brand> brandStore;
		private KeyValueStore<String, Card> cardStore;

		@Override public void init(ProcessorContext processorContext) {
			orderComponentStore = (KeyValueStore<String, OrderComponentEvent>) processorContext
					.getStateStore(ORDER_COMPONENT_EVENT_STORE);
			orderStore = (KeyValueStore<String, Order>) processorContext
					.getStateStore(ORDER_STORE);
			brandStore = (KeyValueStore<String, Brand>) processorContext
					.getStateStore(BRAND_STORE);
			cardStore = (KeyValueStore<String, Card>) processorContext
					.getStateStore(CARD_STORE);
		}

		@Override public ProcessingWrapper transform(String key, OrderComponentEvent event) {

			//1. check timestamp

			var processingWrapper = new ProcessingWrapper();
			var keyWithAction = event.getHeader().getAction().name().concat(key);
			var existingOrderComponentEvent = orderComponentStore.get(keyWithAction);
			if(nonNull(existingOrderComponentEvent) && event.getHeader().getAction().equals(existingOrderComponentEvent.getHeader().getAction())
					&& event.getHeader().getCreationTimestamp()
					.isBefore(existingOrderComponentEvent.getHeader().getCreationTimestamp())) {
				processingWrapper.setResponse(ModelBuilderUtils.buildFailureOrder(event, "Event timestamp incorrect.", UNKNOWN));
				return processingWrapper;
			}

			if(nonNull(orderStore.get(key)) && !StateMachine.getPossibleTransitions(orderStore.get(key).getEntityState()).contains(event.getHeader().getAction())){
				var possibleTransitions = StateMachine.getPossibleTransitions(orderStore.get(key).getEntityState());
				processingWrapper.setResponse(ModelBuilderUtils.buildFailureOrder(event, "Event transition incorrect:" + orderStore.get(key).getEntityState() + " -> " +event.getHeader().getAction() + ", possible are: " + possibleTransitions, orderStore.get(key).getEntityState()));
				return processingWrapper;
			}

			if(event.getHeader().getType().equals(OrderType.BRAND)){
				var brand = brandStore.get(key);
				if(isNull(brand) || brand.getDeleted()){
					processingWrapper = processingStrategyContext.createNewEntity(event);
					if(isNull(processingWrapper.getBrand())){
						return processingWrapper;
					}
				}else{
					processingWrapper = processingStrategyContext.updateEntity(event, brand);
					if(isNull(processingWrapper.getBrand())){
						return processingWrapper;
					}
				}
				brandStore.put(key, processingWrapper.getBrand());
			}

			if(event.getHeader().getType().equals(OrderType.CARD)){
				var card = cardStore.get(key);
				if(isNull(card) || card.getDeleted()){
					processingWrapper = processingStrategyContext.createNewEntity(event);
					if(isNull(processingWrapper.getCard())){
						return processingWrapper;
					}
				}else{
					processingWrapper = processingStrategyContext.updateEntity(event, card);
					if(isNull(processingWrapper.getCard())){
						return processingWrapper;
					}
				}
				cardStore.put(key, processingWrapper.getCard());

			}

			orderStore.put(key, processingWrapper.getResponse());
			orderComponentStore.put(keyWithAction, event);
			return processingWrapper;
		}

		@Override public void close() {

		}
	}



}
