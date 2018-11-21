/*
 * C-Vault, @ 2018 Swisscom (Schweiz) AG
 */
package pl.cba.gibcode.modelLibrary.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.header.Headers;
import org.apache.kafka.common.serialization.ExtendedDeserializer;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

public class CustomJsonDeserializer<T> implements ExtendedDeserializer<T> {
	private final ObjectMapper objectMapper = getObjectMapper();
	private final ObjectReader reader;

	public CustomJsonDeserializer(Class<T> targetType) {
		this.reader = this.objectMapper.readerFor(targetType);
	}

	@Override
	public T deserialize(String topic, Headers headers, byte[] data) {
		return deserialize(topic, data);
	}

	@Override
	public T deserialize(String topic, byte[] data) {
		if (data == null) {
			return null;
		} else {
			try {
				return this.reader.readValue(data);
			} catch (IOException e) {
				throw new SerializationException(
						"Can't deserialize data [" + Arrays.toString(data) + "] from topic [" + topic + "]", e);
			}
		}
	}

	@Override public void configure(Map<String, ?> configs, boolean isKey) {}
	@Override public void close() {}

	public  ObjectMapper getObjectMapper() {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.registerModule(new Jdk8Module());
		objectMapper.registerModule(new JavaTimeModule());
		objectMapper.configure(MapperFeature.DEFAULT_VIEW_INCLUSION, false);
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		return objectMapper;
	}
}
