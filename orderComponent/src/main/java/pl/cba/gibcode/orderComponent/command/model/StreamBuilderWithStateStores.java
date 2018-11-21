package pl.cba.gibcode.orderComponent.command.model;

import org.apache.kafka.streams.StreamsBuilder;

public class StreamBuilderWithStateStores {
	private final StreamsBuilder streamsBuilder;

	public StreamBuilderWithStateStores(StreamsBuilder streamsBuilder) {
		this.streamsBuilder = streamsBuilder;
	}

	public StreamsBuilder getStreamsBuilder() {
		return streamsBuilder;
	}
}
