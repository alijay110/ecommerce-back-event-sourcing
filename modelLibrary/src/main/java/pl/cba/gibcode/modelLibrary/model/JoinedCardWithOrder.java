package pl.cba.gibcode.modelLibrary.model;

import pl.cba.gibcode.modelLibrary.card.Card;

public class JoinedCardWithOrder {
	private Card card;
	private Order order;
	private Double discount;

	public Card getCard() {
		return card;
	}

	public void setCard(Card card) {
		this.card = card;
	}

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	public Double getDiscount() {
		return discount;
	}

	public void setDiscount(Double discount) {
		this.discount = discount;
	}

	@Override public String toString() {
		return "JoinedCardWithOrder{" +
				"card=" + card +
				", order=" + order +
				", discount=" + discount +
				'}';
	}
}
