package pl.cba.gibcode.apiQuery.service;

import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pl.cba.gibcode.apiQuery.helper.CriteriaBuilderHelper;
import pl.cba.gibcode.apiQuery.model.OrderCriteriaDto;
import pl.cba.gibcode.modelLibrary.exceptions.BusinessException;
import pl.cba.gibcode.modelLibrary.model.JoinedCardWithOrder;
import pl.cba.gibcode.modelLibrary.model.Order;
import pl.cba.gibcode.modelLibrary.query.QueryBuyerOrders;
import pl.cba.gibcode.modelLibrary.query.QuerySellerOrders;

import javax.inject.Provider;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static java.util.Objects.isNull;
import static pl.cba.gibcode.apiQuery.helper.CriteriaBuilderHelper.meetsCriteria;

@Service
public class OrderService {

	private final Provider<ReadOnlyKeyValueStore<String, Order>> orderStoreProvider;
	private final Provider<ReadOnlyKeyValueStore<String, QueryBuyerOrders>> buyerOrdersStore;
	private final Provider<ReadOnlyKeyValueStore<String, QuerySellerOrders>> sellerOrdersStore;

	public OrderService(
			Provider<ReadOnlyKeyValueStore<String, Order>> orderStoreProvider,
			Provider<ReadOnlyKeyValueStore<String, QueryBuyerOrders>> buyerOrdersStore,
			Provider<ReadOnlyKeyValueStore<String, QuerySellerOrders>> sellerOrdersStore) {
		this.orderStoreProvider = orderStoreProvider;
		this.buyerOrdersStore = buyerOrdersStore;
		this.sellerOrdersStore = sellerOrdersStore;
	}

	public Order getOrder(UUID orderUuid) {
		var order = orderStoreProvider.get().get(orderUuid.toString());
		if(isNull(order)) {
			throw new BusinessException(String.format("Order with orderUuid %s not found", orderUuid.toString()));
		}
		return order;
	}

	public Page<JoinedCardWithOrder> getOrdersForSeller(Pageable pageable, Long userId, OrderCriteriaDto criteriaDto) {
		var queryOrder = sellerOrdersStore.get().get(userId.toString());
		if(isNull(queryOrder)) {
			throw new BusinessException("There are no transactions for this seller.");
		}
		List<JoinedCardWithOrder> orders = new ArrayList<>();
		for(JoinedCardWithOrder joinedCardWithOrder : queryOrder.getJoinedCardWithOrderList()) {
			if(meetsCriteria(criteriaDto, joinedCardWithOrder)) {
				orders.add(joinedCardWithOrder);
			}
		}
		return new PageImpl<>(orders, pageable, orders.size());
	}

	public Page<JoinedCardWithOrder> getOrdersForBuyer(Pageable pageable, Long userId, OrderCriteriaDto criteriaDto) {
		var queryOrder = buyerOrdersStore.get().get(userId.toString());
		if(isNull(queryOrder)) {
			throw new BusinessException("There are no transactions for this buyer.");
		}
		List<JoinedCardWithOrder> orders = new ArrayList<>();
		for(JoinedCardWithOrder joinedCardWithOrder : queryOrder.getJoinedCardWithOrderList()) {
			if(meetsCriteria(criteriaDto, joinedCardWithOrder)) {
				orders.add(joinedCardWithOrder);
			}
		}
		return new PageImpl<>(orders, pageable, orders.size());
	}

	public Page<Order> getOrdersForAdmin(Pageable pageable, OrderCriteriaDto criteriaDto) {
		var iterator = orderStoreProvider.get().all();
		List<Order> list = new ArrayList<>();
		while(iterator.hasNext()) {
			var order = iterator.next().value;
			if(CriteriaBuilderHelper.meetsCriteria(criteriaDto, order))
				list.add(order);
		}
		return new PageImpl<>(list, pageable, list.size());
	}

}
