package pl.cba.gibcode.modelLibrary.query;

import pl.cba.gibcode.modelLibrary.model.JoinedCardWithOrder;

import java.util.ArrayList;
import java.util.List;

public class QueryBuyerOrders {
	private List<JoinedCardWithOrder> joinedCardWithOrderList;

	public QueryBuyerOrders() {
		this.joinedCardWithOrderList = new ArrayList<>();
	}

	public List<JoinedCardWithOrder> getJoinedCardWithOrderList() {
		return joinedCardWithOrderList;
	}

	public void setJoinedCardWithOrderList(List<JoinedCardWithOrder> joinedCardWithOrderList) {
		this.joinedCardWithOrderList = joinedCardWithOrderList;
	}

	@Override public String toString() {
		return "QueryBuyerOrders{" +
				"joinedCardWithOrderList=" + joinedCardWithOrderList +
				'}';
	}
}
