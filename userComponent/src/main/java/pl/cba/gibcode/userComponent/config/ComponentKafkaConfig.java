package pl.cba.gibcode.userComponent.config;

import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.KStream;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.cba.gibcode.modelLibrary.config.CustomJsonSerde;
import pl.cba.gibcode.modelLibrary.config.KafkaConfig;
import pl.cba.gibcode.modelLibrary.model.KafkaMessage;
import pl.cba.gibcode.userComponent.service.ProcessingService;

@Configuration
public class ComponentKafkaConfig extends KafkaConfig {
	private ProcessingService processingService;

	public ComponentKafkaConfig(ProcessingService processingService) {
		this.processingService = processingService;
	}

	@Override protected String getApplicationId() {
		return "apiQuery";
	}

	@Bean
	public KStream<String, KafkaMessage> kStream(StreamsBuilder streamsBuilder) {
		KStream<String, KafkaMessage> eventStream = streamsBuilder
				.stream("dummyTopic01", CustomJsonSerde.consume(KafkaMessage.class));
		KStream<String, KafkaMessage> orderStream = eventStream.peek(processingService::processMessage);
		orderStream.to("dummyTopic02", CustomJsonSerde.produce(KafkaMessage.class));
		return orderStream;
	}

}
