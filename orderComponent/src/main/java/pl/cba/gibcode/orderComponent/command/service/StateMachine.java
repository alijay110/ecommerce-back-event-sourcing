package pl.cba.gibcode.orderComponent.command.service;

import pl.cba.gibcode.modelLibrary.model.ActionEnum;
import pl.cba.gibcode.modelLibrary.model.EntityStateEnum;

import java.util.List;

import static pl.cba.gibcode.modelLibrary.model.ActionEnum.*;

public class StateMachine {

	public static List<ActionEnum> getPossibleTransitions(EntityStateEnum entityStateEnum) {
		switch(entityStateEnum) {
		case BRAND_DELETED:
			return List.of(CREATE_BRAND);
		case BRAND_CREATED:
			return List.of(UPDATE_BRAND, DELETE_BRAND);
		case BRAND_UPDATED:
			return List.of(UPDATE_BRAND, DELETE_BRAND);
		case CARD_DELETED:
			return List.of(CREATE_BRAND);
		case CARD_SENT:
			return List.of();
		case CARD_PAID:
			return List.of(SEND_CARD);
		case CARD_CHECKED_OUT:
			return List.of(PAY_CARD);
		case CARD_UPDATED:
			return List.of(UPDATE_CARD, VALIDATE_CARD, DELETE_CARD);
		case CARD_CREATED:
			return List.of(UPDATE_CARD, VALIDATE_CARD, DELETE_CARD);
		case CARD_VALIDATED:
			return List.of(UPDATE_CARD, CHECKOUT_CARD, DELETE_CARD);
		default:
			return List.of();
		}
	}
}
