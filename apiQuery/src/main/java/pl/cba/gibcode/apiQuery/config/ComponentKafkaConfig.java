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
import pl.cba.gibcode.modelLibrary.brand.Brand;
import pl.cba.gibcode.modelLibrary.config.CustomJsonSerde;
import pl.cba.gibcode.modelLibrary.config.CustomJsonSerializer;
import pl.cba.gibcode.modelLibrary.config.KafkaConfig;
import pl.cba.gibcode.modelLibrary.model.KafkaMessage;
import pl.cba.gibcode.modelLibrary.model.Order;
import pl.cba.gibcode.modelLibrary.query.QueryBrand;

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
		return builder.globalTable("queryBrand", CustomJsonSerde.consume(QueryBrand.class), Materialized.as("queryBrandStore"));
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
}
