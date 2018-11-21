package pl.cba.gibcode.apiQuery.config;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.cba.gibcode.modelLibrary.config.CustomJsonSerializer;
import pl.cba.gibcode.modelLibrary.config.KafkaConfig;
import pl.cba.gibcode.modelLibrary.model.KafkaMessage;

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

}
