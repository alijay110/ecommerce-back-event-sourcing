package pl.cba.gibcode.orderComponent.command.config;

import pl.cba.gibcode.modelLibrary.model.Event;

public class StreamKeyValueWrapper<K, V extends Event> {
	private final K streamKey;
	private final V streamValue;

	public StreamKeyValueWrapper(K streamKey, V streamValue) {
		this.streamKey = streamKey;
		this.streamValue = streamValue;
	}

	public K getStreamKey() {
		return streamKey;
	}

	public V getStreamValue() {
		return streamValue;
	}
}
