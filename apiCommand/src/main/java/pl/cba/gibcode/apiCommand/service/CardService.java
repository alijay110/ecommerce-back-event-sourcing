package pl.cba.gibcode.apiCommand.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.cba.gibcode.apiCommand.model.OrderTypeSelector;
import pl.cba.gibcode.modelLibrary.card.*;
import pl.cba.gibcode.modelLibrary.model.ActionEnum;
import pl.cba.gibcode.modelLibrary.model.CreatedOrderUuidBody;
import pl.cba.gibcode.modelLibrary.model.CreatedOrderUuidBodyFragment;
import pl.cba.gibcode.modelLibrary.model.ImmutableCreatedOrderUuidBody;
import pl.cba.gibcode.modelLibrary.ordercomponent.OrderType;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CardService implements OrderTypeSelector {

	private final SendService sendService;
	private final ValidatorService validatorService;

	public CreatedOrderUuidBody createCard(CreateCardBody dto, Long userId) {
		validatorService.validateBrand(dto.getBrandName());
		var uuids = sendService.postOrder(UUID.randomUUID().toString(), dto, ActionEnum.CREATE_CARD, userId, getOrderType());
		return ImmutableCreatedOrderUuidBody.builder().entityId(uuids.getEntityId()).orderUuid(uuids.getOrderUuid()).build();
	}

	public CreatedOrderUuidBody validate(BasicCardBody dto, Long userId) {
		validatorService.validateCard(dto.getCardUuid().toString());
		var orderUuid = sendService.putOrder(dto.getCardUuid().toString(), dto, ActionEnum.VALIDATE_CARD, userId, getOrderType());
		return ImmutableCreatedOrderUuidBody.builder().entityId(dto.getCardUuid().toString()).orderUuid(orderUuid).build();
	}

	public CreatedOrderUuidBody deleteCard(DeleteCardBody dto, Long userId) {
		validatorService.validateCard(dto.getCardUuid().toString());
		var orderUuid = sendService.putOrder(dto.getCardUuid().toString(), dto, ActionEnum.DELETE_CARD, userId, getOrderType());
		return ImmutableCreatedOrderUuidBody.builder().entityId(dto.getCardUuid().toString()).orderUuid(orderUuid).build();
	}

	public CreatedOrderUuidBody updateCard(UpdateCardBody dto, Long userId) {
		validatorService.validateCard(dto.getCardUuid().toString());
		validatorService.validateBrand(dto.getBrandName());
		var orderUuid = sendService.putOrder(dto.getCardUuid().toString(), dto, ActionEnum.UPDATE_CARD, userId, getOrderType());
		return ImmutableCreatedOrderUuidBody.builder().entityId(dto.getCardUuid().toString()).orderUuid(orderUuid).build();
	}

	public CreatedOrderUuidBody sendCard(BasicCardBody dto, Long userId) {
		validatorService.validateCard(dto.getCardUuid().toString());
		var orderUuid = sendService.putOrder(dto.getCardUuid().toString(), dto, ActionEnum.SEND_CARD, userId, getOrderType());
		return ImmutableCreatedOrderUuidBody.builder().entityId(dto.getCardUuid().toString()).orderUuid(orderUuid).build();
	}

	public CreatedOrderUuidBody checkout(BasicCardBody dto, Long userId) {
		validatorService.validateCard(dto.getCardUuid().toString());
		var orderUuid = sendService.putOrder(dto.getCardUuid().toString(), dto, ActionEnum.CHECKOUT_CARD, userId, getOrderType());
		return ImmutableCreatedOrderUuidBody.builder().entityId(dto.getCardUuid().toString()).orderUuid(orderUuid).build();
	}

	public CreatedOrderUuidBody pay(BasicCardBody dto, Long userId) {
		validatorService.validateCard(dto.getCardUuid().toString());
		var orderUuid = sendService.putOrder(dto.getCardUuid().toString(), dto, ActionEnum.PAY_CARD, userId, getOrderType());
		return ImmutableCreatedOrderUuidBody.builder().entityId(dto.getCardUuid().toString()).orderUuid(orderUuid).build();
	}

	@Override public OrderType getOrderType() {
		return OrderType.CARD;
	}
}
