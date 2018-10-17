package pl.cba.gibcode.apiQuery.config;

import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.errors.LogAndContinueExceptionHandler;
import org.apache.kafka.streams.processor.WallclockTimestampExtractor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.SmartLifecycle;
import org.springframework.context.annotation.Bean;

import java.util.EnumSet;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.concurrent.TimeUnit;


public abstract class KafkaConfig {

	private final static String KAFKA_STREAMS_PATH = "./tmp/kafka-streams";
	protected abstract String getApplicationId();

	@Value("${kafka.bootstrap-servers}")
	private String bootstrapServers;

	/*@Bean
	public Producer<String, Object> producer() {
		Map<String, Object> props = Map.of(
				ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers,
				ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class,
				ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, CustomJsonSerializer.class,
				ProducerConfig.MAX_BLOCK_MS_CONFIG, 10000);
		return new KafkaProducer<>(props);
	}*/

	@Bean("streamsBuilder")
	public StreamsBuilder streamsBuilder() {
		return new StreamsBuilder();
	}

	@Bean("kafkaStreams")
	public KafkaStreams kafkaStreams(
			StreamsBuilder builder) {
		Properties streamsConfig = new Properties();
		streamsConfig.putAll(Map.of(
				StreamsConfig.BOOTSTRAP_SERVERS_CONFIG,
				bootstrapServers,
				StreamsConfig.APPLICATION_ID_CONFIG,
				this.getApplicationId(),
				StreamsConfig.PROCESSING_GUARANTEE_CONFIG,
				StreamsConfig.EXACTLY_ONCE,
				StreamsConfig.STATE_DIR_CONFIG,
				KAFKA_STREAMS_PATH,
				StreamsConfig.DEFAULT_DESERIALIZATION_EXCEPTION_HANDLER_CLASS_CONFIG,
				LogAndContinueExceptionHandler.class,
				StreamsConfig.DEFAULT_TIMESTAMP_EXTRACTOR_CLASS_CONFIG,
				WallclockTimestampExtractor.class.getName()));

		Topology topology = builder.build();
		System.out.println("Streams topology:\n{}" + topology.describe());

		return new KafkaStreams(topology, streamsConfig);
	}

	@Bean("kafkaStreamsLifecycle")
	public KafkaStreamsLifecycle kafkaStreamsLifecycle(KafkaStreams kafkaStreams) {
		return new KafkaStreamsLifecycle(kafkaStreams);
	}

	public static class KafkaStreamsLifecycle implements SmartLifecycle {

		private final EnumSet<KafkaStreams.State> RUNNING_STATES =
				EnumSet.of(KafkaStreams.State.RUNNING, KafkaStreams.State.REBALANCING);

		private final KafkaStreams kafkaStreams;

		public KafkaStreamsLifecycle(KafkaStreams kafkaStreams) {
			this.kafkaStreams = Objects.requireNonNull(kafkaStreams);
		}

		@Override
		public boolean isAutoStartup() {
			return true;
		}

		@Override
		public void stop() {
			stop(null);
		}

		@Override
		public void stop(Runnable callback) {
			if(callback != null) {
				callback.run();
			}

			if(isRunning()) {
				kafkaStreams.close(10, TimeUnit.SECONDS);
			}
		}

		@Override
		public void start() {
			if(!isRunning()) {
				try {
					kafkaStreams.start();
				} catch(Exception e) {
					throw new RuntimeException("Failed to start kafka streams", e);
				}
			}
		}

		@Override
		public boolean isRunning() {
			return RUNNING_STATES.contains(kafkaStreams.state());
		}

		@Override
		public int getPhase() {
			return Integer.MAX_VALUE;
		}
	}


}