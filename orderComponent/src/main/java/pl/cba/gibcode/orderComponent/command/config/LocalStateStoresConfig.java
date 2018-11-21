package pl.cba.gibcode.orderComponent.command.config;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.state.KeyValueStore;
import org.apache.kafka.streams.state.StoreBuilder;
import org.apache.kafka.streams.state.Stores;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.cba.gibcode.modelLibrary.brand.Brand;
import pl.cba.gibcode.modelLibrary.card.Card;
import pl.cba.gibcode.modelLibrary.config.CustomJsonSerde;
import pl.cba.gibcode.modelLibrary.model.Order;
import pl.cba.gibcode.modelLibrary.ordercomponent.OrderComponentEvent;
import pl.cba.gibcode.orderComponent.command.model.StreamBuilderWithStateStores;

@Configuration
public class LocalStateStoresConfig {

	public static final String ORDER_COMPONENT_EVENT_STORE = "orderComponentEventStore";
	public static final String ORDER_STORE = "orderStore";
	public static final String BRAND_STORE = "brandStore";
	public static final String CARD_STORE = "cardStore";

	@Bean StreamBuilderWithStateStores initStateStore(StreamsBuilder streamsBuilder) {
		streamsBuilder.addStateStore(getCryptoPositionsStore());
		streamsBuilder.addStateStore(getWalletInfoStore());
		streamsBuilder.addStateStore(getCustomerStore());
		streamsBuilder.addStateStore(getMasterDataEventStore());
		return new StreamBuilderWithStateStores(streamsBuilder);
	}

	private StoreBuilder<KeyValueStore<String, OrderComponentEvent>> getCryptoPositionsStore() {
		return Stores.keyValueStoreBuilder(
				Stores.persistentKeyValueStore(ORDER_COMPONENT_EVENT_STORE),
				Serdes.String(),
				CustomJsonSerde.of(OrderComponentEvent.class)
		);
	}

	private StoreBuilder<KeyValueStore<String, Order>> getWalletInfoStore() {
		return Stores.keyValueStoreBuilder(
				Stores.persistentKeyValueStore(ORDER_STORE),
				Serdes.String(),
				CustomJsonSerde.of(Order.class)
		);
	}

	private StoreBuilder<KeyValueStore<String, Brand>> getCustomerStore() {
		return Stores.keyValueStoreBuilder(
				Stores.persistentKeyValueStore(BRAND_STORE),
				Serdes.String(),
				CustomJsonSerde.of(Brand.class)
		);
	}

	private StoreBuilder<KeyValueStore<String, Card>> getMasterDataEventStore() {
		return Stores.keyValueStoreBuilder(
				Stores.persistentKeyValueStore(CARD_STORE),
				Serdes.String(),
				CustomJsonSerde.of(Card.class)
		);
	}
}
