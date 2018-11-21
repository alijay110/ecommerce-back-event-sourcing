package pl.cba.gibcode.orderComponent.command.model;

import pl.cba.gibcode.modelLibrary.model.*;
import pl.cba.gibcode.modelLibrary.ordercomponent.OrderComponentEvent;

import static pl.cba.gibcode.modelLibrary.model.OrderStateEnum.FAILURE;
import static pl.cba.gibcode.modelLibrary.model.OrderStateEnum.SUCCESS;

public class ModelBuilderUtils {



	public static Order buildSuccessOrder(OrderComponentEvent event, EntityStateEnum entityStateEnum){
		return ImmutableOrder.builder().entityState(entityStateEnum).orderUuid(event.getHeader().getOrderUuid()).orderState(SUCCESS).type(event.getHeader().getType()).build();
	}

	public static Order buildFailureOrder(OrderComponentEvent event, String reason,  EntityStateEnum entityStateEnum){

		return ImmutableOrder.builder().entityState(entityStateEnum).orderState(FAILURE).type(event.getHeader().getType())
				.orderUuid(event.getHeader().getOrderUuid())
				.errorMessage(reason)
				.build();
	}

}
