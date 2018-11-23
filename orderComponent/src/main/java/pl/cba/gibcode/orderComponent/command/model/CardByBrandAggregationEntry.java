package pl.cba.gibcode.orderComponent.command.model;

import pl.cba.gibcode.modelLibrary.card.Card;
import pl.cba.gibcode.modelLibrary.model.JoinedCardWithOrder;
import pl.cba.gibcode.modelLibrary.model.PriceRangeEnum;

public class CardByBrandAggregationEntry {
	private JoinedCardWithOrder card;
	private PriceRangeEnum priceRangeEnum;

	public JoinedCardWithOrder getCard() {
		return card;
	}

	public void setCard(JoinedCardWithOrder card) {
		this.card = card;
	}

	public PriceRangeEnum getPriceRangeEnum() {
		return priceRangeEnum;
	}

	public void setPriceRangeEnum(PriceRangeEnum priceRangeEnum) {
		this.priceRangeEnum = priceRangeEnum;
	}

	@Override public String toString() {
		return "CardByBrandAggregationEntry{" +
				"card=" + card +
				", priceRangeEnum=" + priceRangeEnum +
				'}';
	}
}
