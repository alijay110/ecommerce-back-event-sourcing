package pl.cba.gibcode.modelLibrary.config;

import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.serialization.Serializer;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.Produced;
import org.apache.kafka.streams.processor.StateStore;

import java.util.Map;

public class CustomJsonSerde<T> implements Serde<T> {
	private final CustomJsonSerializer<T> jsonSerializer;
	private final CustomJsonDeserializer<T> customJsonDeserializer;

	public CustomJsonSerde() {
		this(null);
	}

	public CustomJsonSerde(Class<T> targetType) {
		this.jsonSerializer = new CustomJsonSerializer<>();
		this.customJsonDeserializer = new CustomJsonDeserializer<>(targetType);
	}

	@Override public Serializer<T> serializer() {
		return this.jsonSerializer;
	}
	@Override public Deserializer<T> deserializer() {
		return this.customJsonDeserializer;
	}
	@Override public void configure(Map<String, ?> map, boolean b) {}
	@Override public void close() {}

	public static <T> CustomJsonSerde<T> of(Class<T> cls) {
		return new CustomJsonSerde<>(cls);
	}

	public static <T> Consumed<String, T> consume(Class<T> cls){
		return Consumed.with(Serdes.String(), new CustomJsonSerde<>(cls));
	}

	public static <K, T> Consumed<K, T> consume(Class<K> clsK, Class<T> cls){
		return Consumed.with(new CustomJsonSerde<>(clsK), new CustomJsonSerde<>(cls));
	}

	public static <T> Produced<String, T> produce(Class<T> cls){
		return Produced.with(Serdes.String(), new CustomJsonSerde<>(cls));
	}

	public static <K, T> Produced<K, T> produce(Class<K> clzK, Class<T> cls){
		return Produced.with(new CustomJsonSerde<>(clzK), new CustomJsonSerde<>(cls));
	}

	public static <T, S extends StateStore> Materialized<String, T, S> materialize(Class<T> cls){
		return Materialized.with(Serdes.String(), new CustomJsonSerde<>(cls));
	}

	public static <K, T, S extends StateStore> Materialized<K, T, S> materialize(Class<K> clzK, Class<T> cls){
		return Materialized.with(new CustomJsonSerde<>(clzK), new CustomJsonSerde<>(cls));
	}
}
