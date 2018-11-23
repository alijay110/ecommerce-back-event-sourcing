package pl.cba.gibcode.orderComponent.command.model;

import pl.cba.gibcode.modelLibrary.card.CardTypeEnum;
import pl.cba.gibcode.modelLibrary.model.PriceRangeEnum;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class CardWrapperAggregator {

	private Map<String, Double> discounts;
	private Set<CardTypeEnum> cardTypes;
	private Set<PriceRangeEnum> priceRanges;

	public CardWrapperAggregator() {
		this.discounts = new HashMap<>();
		this.cardTypes = new HashSet<>();
		this.priceRanges = new HashSet<>();
	}


	public Map<String, Double> getDiscounts() {
		return discounts;
	}

	public void setDiscounts(Map<String, Double> discounts) {
		this.discounts = discounts;
	}

	public Set<CardTypeEnum> getCardTypes() {
		return cardTypes;
	}

	public void setCardTypes(Set<CardTypeEnum> cardTypes) {
		this.cardTypes = cardTypes;
	}

	public Set<PriceRangeEnum> getPriceRanges() {
		return priceRanges;
	}

	public void setPriceRanges(Set<PriceRangeEnum> priceRanges) {
		this.priceRanges = priceRanges;
	}
}
