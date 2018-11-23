package pl.cba.gibcode.apiQuery;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.TopologyTestDriver;
import org.apache.kafka.streams.processor.WallclockTimestampExtractor;
import org.apache.kafka.streams.test.ConsumerRecordFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.springframework.util.FileSystemUtils;
import pl.cba.gibcode.modelLibrary.config.CustomJsonSerde;
import pl.cba.gibcode.modelLibrary.model.KafkaMessage;

import java.io.File;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;

public abstract class KafkaBaseTest {

	private final static String KAFKA_STREAMS_PATH = "./tmp/test/kafka-streams/";
	protected TopologyTestDriver driver;
	protected ConsumerRecordFactory<String, KafkaMessage> messageConsumerRecordFactory;
	private String stateDir;

	@BeforeClass
	public static void prepare() {
		// cleanup old temp folder if not deleted on cleanup (known issue on windows)
		FileSystemUtils.deleteRecursively(new File(KAFKA_STREAMS_PATH));
	}

	@Before
	public void setUp() {
		stateDir = KAFKA_STREAMS_PATH + UUID.randomUUID().toString();
		var streamsConfigs = new Properties();
		streamsConfigs.putAll(Map.of(
				StreamsConfig.BOOTSTRAP_SERVERS_CONFIG,
				"localhost:9092",
				// dummy value
				StreamsConfig.APPLICATION_ID_CONFIG,
				"test",
				// dummy value
				StreamsConfig.STATE_DIR_CONFIG,
				stateDir,
				StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG,
				Serdes.StringSerde.class,
				StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG,
				CustomJsonSerde.class,
				StreamsConfig.CACHE_MAX_BYTES_BUFFERING_CONFIG,
				0,
				// disable cache -> actually needed in unit tests for aggregation
				StreamsConfig.DEFAULT_TIMESTAMP_EXTRACTOR_CLASS_CONFIG,
				WallclockTimestampExtractor.class.getName()));
		StreamsBuilder builder = new StreamsBuilder();

		// Create the topology to start testing
		Topology topology = builder.build();
		driver = new TopologyTestDriver(topology, streamsConfigs);

		messageConsumerRecordFactory = new ConsumerRecordFactory<String, KafkaMessage>(
				"dummyTopic01",
				Serdes.String().serializer(),
				CustomJsonSerde.of(KafkaMessage.class).serializer());
	}

	@After
	public void cleanup() {
		try {
			driver.close();
		} catch(Exception e) {
			// silently ignore exceptions while closing (known issue on windows)
		}
	}
}
