package pl.cba.gibcode.orderComponent.command.model;

import pl.cba.gibcode.modelLibrary.card.CardTypeEnum;
import pl.cba.gibcode.modelLibrary.model.PriceRangeEnum;

public class CardAggregationEntry {
	private Double discount;
	private CardTypeEnum cardTypeEnum;
	private PriceRangeEnum priceRangeEnum;

	public Double getDiscount() {
		return discount;
	}

	public void setDiscount(Double discount) {
		this.discount = discount;
	}

	public CardTypeEnum getCardTypeEnum() {
		return cardTypeEnum;
	}

	public void setCardTypeEnum(CardTypeEnum cardTypeEnum) {
		this.cardTypeEnum = cardTypeEnum;
	}

	public PriceRangeEnum getPriceRangeEnum() {
		return priceRangeEnum;
	}

	public void setPriceRangeEnum(PriceRangeEnum priceRangeEnum) {
		this.priceRangeEnum = priceRangeEnum;
	}

	@Override public String toString() {
		return "CardAggregationEntry{" +
				"discount=" + discount +
				", cardTypeEnum=" + cardTypeEnum +
				", priceRangeEnum=" + priceRangeEnum +
				'}';
	}
}
