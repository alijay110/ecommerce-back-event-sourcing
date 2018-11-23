package pl.cba.gibcode.modelLibrary.query;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.value.Value;
import pl.cba.gibcode.modelLibrary.model.JoinedCardWithOrder;

import java.util.ArrayList;
import java.util.List;

public class QuerySellerOrders {
	private List<JoinedCardWithOrder> joinedCardWithOrderList;

	public QuerySellerOrders() {
		this.joinedCardWithOrderList = new ArrayList<>();
	}

	public List<JoinedCardWithOrder> getJoinedCardWithOrderList() {
		return joinedCardWithOrderList;
	}

	public void setJoinedCardWithOrderList(List<JoinedCardWithOrder> joinedCardWithOrderList) {
		this.joinedCardWithOrderList = joinedCardWithOrderList;
	}

	@Override public String toString() {
		return "QuerySellerOrders{" +
				"joinedCardWithOrderList=" + joinedCardWithOrderList +
				'}';
	}
}
