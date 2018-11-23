package pl.cba.gibcode.apiCommand.config;

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
import org.apache.kafka.streams.state.internals.GlobalStateStoreProvider;
import org.apache.kafka.streams.state.internals.QueryableStoreProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import pl.cba.gibcode.modelLibrary.brand.Brand;
import pl.cba.gibcode.modelLibrary.brand.BrandList;
import pl.cba.gibcode.modelLibrary.card.Card;
import pl.cba.gibcode.modelLibrary.config.CustomJsonSerde;
import pl.cba.gibcode.modelLibrary.model.CategoryEnum;
import pl.cba.gibcode.modelLibrary.model.Order;
import pl.cba.gibcode.modelLibrary.ordercomponent.OrderComponentEvent;
import pl.cba.gibcode.modelLibrary.config.CustomJsonSerializer;
import pl.cba.gibcode.modelLibrary.config.KafkaConfig;

import java.util.Map;

@Configuration
public class ComponentKafkaConfig extends KafkaConfig {

	@Value("${kafka.bootstrap-servers}")
	private String bootstrapServers;

	@Override protected String getApplicationId() {
		return "apiCommand";
	}

	@Bean
	public Producer<String, OrderComponentEvent> producer() {
		return new KafkaProducer<>(producerProperties());
	}

	@Bean
	@Lazy
	public ReadOnlyKeyValueStore<String, Brand> brandStore(
			KafkaStreams kafkaStreams,
			GlobalKTable<String, Brand> globalBrandTable) {
		return kafkaStreams.store(globalBrandTable.queryableStoreName(), QueryableStoreTypes.keyValueStore());
	}

	@Bean
	public GlobalKTable<String, Brand> globalBrandTable(StreamsBuilder builder) {
		return builder.globalTable("brand", CustomJsonSerde.consume(Brand.class), Materialized.as("brandStore"));
	}

	@Bean
	@Lazy
	public ReadOnlyKeyValueStore<String, Card> cardStore(
			KafkaStreams kafkaStreams,
			GlobalKTable<String, Card> globalCardTable) {
		return kafkaStreams.store(globalCardTable.queryableStoreName(), QueryableStoreTypes.keyValueStore());
	}

	@Bean
	public GlobalKTable<String, Card> globalCardTable(StreamsBuilder builder) {
		return builder.globalTable("card", CustomJsonSerde.consume(Card.class), Materialized.as("cardStore"));
	}

	@Bean
	@Lazy
	public ReadOnlyKeyValueStore<CategoryEnum, BrandList> testStore(
			KafkaStreams kafkaStreams,
			GlobalKTable<CategoryEnum, BrandList> globalTestTable) {
		return kafkaStreams.store(globalTestTable.queryableStoreName(), QueryableStoreTypes.keyValueStore());
	}

	@Bean
	public GlobalKTable<CategoryEnum, BrandList> globalTestTable(StreamsBuilder builder) {
		return builder.globalTable("brandsByCategories", CustomJsonSerde.consume(CategoryEnum.class, BrandList.class), Materialized.as("brandsByCategoriesStore"));
	}

	private Map<String, Object> producerProperties() {
		return Map.of(
				ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers,
				ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class,
				ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, CustomJsonSerializer.class,
				ProducerConfig.MAX_BLOCK_MS_CONFIG, 10000);
	}

}
