package pl.cba.gibcode.orderComponent.command.model;

import pl.cba.gibcode.modelLibrary.model.EntityStateEnum;
import pl.cba.gibcode.modelLibrary.model.ImmutableOrder;
import pl.cba.gibcode.modelLibrary.model.Order;
import pl.cba.gibcode.modelLibrary.ordercomponent.OrderComponentEvent;

import static pl.cba.gibcode.modelLibrary.model.OrderStateEnum.FAILURE;
import static pl.cba.gibcode.modelLibrary.model.OrderStateEnum.SUCCESS;

public class ModelBuilderUtils {

	public static Order buildSuccessOrder(OrderComponentEvent event, EntityStateEnum entityStateEnum) {
		return ImmutableOrder.builder().entityState(entityStateEnum).orderUuid(event.getHeader().getOrderUuid())
				.orderState(SUCCESS).type(event.getHeader().getType()).entityId(event.getHeader().getEntityId()).build();
	}

	public static Order buildFailureOrder(OrderComponentEvent event, String reason, EntityStateEnum entityStateEnum) {

		return ImmutableOrder.builder().entityState(entityStateEnum).orderState(FAILURE)
				.type(event.getHeader().getType())
				.orderUuid(event.getHeader().getOrderUuid())
				.errorMessage(reason)
				.entityId(event.getHeader().getEntityId())
				.build();
	}

	public static ImmutableOrder.Builder getBuildSuccessOrderBuilder(OrderComponentEvent event, EntityStateEnum entityStateEnum) {
		return ImmutableOrder.builder().entityState(entityStateEnum).orderUuid(event.getHeader().getOrderUuid()).entityId(event.getHeader().getEntityId())
				.orderState(SUCCESS).type(event.getHeader().getType());
	}

}
