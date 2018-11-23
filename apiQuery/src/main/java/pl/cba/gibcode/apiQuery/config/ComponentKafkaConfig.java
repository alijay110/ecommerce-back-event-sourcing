package pl.cba.gibcode.apiQuery.config;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.GlobalKTable;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import pl.cba.gibcode.modelLibrary.config.CustomJsonSerde;
import pl.cba.gibcode.modelLibrary.config.CustomJsonSerializer;
import pl.cba.gibcode.modelLibrary.config.KafkaConfig;
import pl.cba.gibcode.modelLibrary.model.JoinedCardWithOrder;
import pl.cba.gibcode.modelLibrary.model.KafkaMessage;
import pl.cba.gibcode.modelLibrary.model.Order;
import pl.cba.gibcode.modelLibrary.query.QueryBrand;
import pl.cba.gibcode.modelLibrary.query.QueryBuyerOrders;
import pl.cba.gibcode.modelLibrary.query.QueryCardsByBrand;
import pl.cba.gibcode.modelLibrary.query.QuerySellerOrders;

import java.util.Map;

@Configuration
public class ComponentKafkaConfig extends KafkaConfig {

	@Value("${kafka.bootstrap-servers}")
	private String bootstrapServers;

	@Override protected String getApplicationId() {
		return "apiQuery";
	}

	@Bean
	public Producer<String, KafkaMessage> producer() {
		return new KafkaProducer<>(producerProperties());
	}

	private Map<String, Object> producerProperties() {
		return Map.of(
				ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers,
				ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class,
				ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, CustomJsonSerializer.class,
				ProducerConfig.MAX_BLOCK_MS_CONFIG, 10000);
	}

	@Bean
	@Lazy
	public ReadOnlyKeyValueStore<String, QueryBrand> brandStore(
			KafkaStreams kafkaStreams,
			GlobalKTable<String, QueryBrand> globalBrandTable) {
		return kafkaStreams.store(globalBrandTable.queryableStoreName(), QueryableStoreTypes.keyValueStore());
	}

	@Bean
	public GlobalKTable<String, QueryBrand> globalBrandTable(StreamsBuilder builder) {
		return builder.globalTable("queryBrand",
				CustomJsonSerde.consume(QueryBrand.class),
				Materialized.as("queryBrandStore"));
	}

	@Bean
	@Lazy
	public ReadOnlyKeyValueStore<String, Order> orderStore(
			KafkaStreams kafkaStreams,
			GlobalKTable<String, Order> globalOrderTable) {
		return kafkaStreams.store(globalOrderTable.queryableStoreName(), QueryableStoreTypes.keyValueStore());
	}

	@Bean
	public GlobalKTable<String, Order> globalOrderTable(StreamsBuilder builder) {
		return builder.globalTable("order", CustomJsonSerde.consume(Order.class), Materialized.as("orderStore"));
	}

	@Bean
	@Lazy
	public ReadOnlyKeyValueStore<String, QueryCardsByBrand> queryCardsByBrandStore(
			KafkaStreams kafkaStreams,
			GlobalKTable<String, QueryCardsByBrand> globalQueryCardsByBrand) {
		return kafkaStreams.store(globalQueryCardsByBrand.queryableStoreName(), QueryableStoreTypes.keyValueStore());
	}

	@Bean
	public GlobalKTable<String, QueryCardsByBrand> globalQueryCardsByBrand(StreamsBuilder builder) {
		return builder.globalTable("queryCardsByBrand",
				CustomJsonSerde.consume(QueryCardsByBrand.class),
				Materialized.as("queryCardsByBrandStore"));
	}

	@Bean
	@Lazy
	public ReadOnlyKeyValueStore<String, JoinedCardWithOrder> joinedCardWithOrderStore(
			KafkaStreams kafkaStreams,
			GlobalKTable<String, JoinedCardWithOrder> globalJoinedCardWithOrder) {
		return kafkaStreams.store(globalJoinedCardWithOrder.queryableStoreName(), QueryableStoreTypes.keyValueStore());
	}

	@Bean
	public GlobalKTable<String, JoinedCardWithOrder> globalJoinedCardWithOrder(StreamsBuilder builder) {
		return builder.globalTable("joinedCardWithOrder",
				CustomJsonSerde.consume(JoinedCardWithOrder.class),
				Materialized.as("joinedCardWithOrderStore"));
	}

	@Bean
	@Lazy
	public ReadOnlyKeyValueStore<String, QueryBuyerOrders> buyerOrderStore(
			KafkaStreams kafkaStreams,
			GlobalKTable<String, QueryBuyerOrders> globalBuyerOrder) {
		return kafkaStreams.store(globalBuyerOrder.queryableStoreName(), QueryableStoreTypes.keyValueStore());
	}

	@Bean
	public GlobalKTable<String, QueryBuyerOrders> globalBuyerOrder(StreamsBuilder builder) {
		return builder.globalTable("buyerOrder",
				CustomJsonSerde.consume(QueryBuyerOrders.class),
				Materialized.as("buyerOrderStore"));
	}

	@Bean
	@Lazy
	public ReadOnlyKeyValueStore<String, QuerySellerOrders> sellerOrderStore(
			KafkaStreams kafkaStreams,
			GlobalKTable<String, QuerySellerOrders> globalSellerOrder) {
		return kafkaStreams.store(globalSellerOrder.queryableStoreName(), QueryableStoreTypes.keyValueStore());
	}

	@Bean
	public GlobalKTable<String, QuerySellerOrders> globalSellerOrder(StreamsBuilder builder) {
		return builder.globalTable("sellerOrder",
				CustomJsonSerde.consume(QuerySellerOrders.class),
				Materialized.as("sellerOrderStore"));
	}
}
