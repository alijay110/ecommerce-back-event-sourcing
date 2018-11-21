package pl.cba.gibcode.orderComponent.command.model;

import pl.cba.gibcode.modelLibrary.brand.Brand;
import pl.cba.gibcode.modelLibrary.card.Card;
import pl.cba.gibcode.modelLibrary.model.Order;

public class ProcessingWrapper {
	private Order response;
	private Brand brand;
	private Card card;


	public Order getResponse() {
		return response;
	}

	public void setResponse(Order response) {
		this.response = response;
	}

	public Brand getBrand() {
		return brand;
	}

	public void setBrand(Brand brand) {
		this.brand = brand;
	}

	public Card getCard() {
		return card;
	}

	public void setCard(Card card) {
		this.card = card;
	}
}
