package pl.cba.gibcode.apiCommand.service;

import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.springframework.stereotype.Service;
import pl.cba.gibcode.modelLibrary.exceptions.BusinessException;
import pl.cba.gibcode.modelLibrary.model.Order;

import javax.inject.Provider;
import java.util.UUID;

import static java.util.Objects.isNull;

@Service
public class OrderService {

	private final Provider<ReadOnlyKeyValueStore<String, Order>> orderBrandStoreProvider;

	public OrderService(Provider<ReadOnlyKeyValueStore<String, Order>> orderBrandStoreProvider) {
		this.orderBrandStoreProvider = orderBrandStoreProvider;
	}

	public Order getOrder(UUID orderUuid){
		var order = orderBrandStoreProvider.get().get(orderUuid.toString());
		if(isNull(order)){
			throw new BusinessException(String.format("Order with orderUuid %s not found", orderUuid.toString()));
		}
		return order;
	}
}
