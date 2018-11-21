package pl.cba.gibcode.orderComponent.command.strategy;

import pl.cba.gibcode.modelLibrary.model.EntityFragment;
import pl.cba.gibcode.modelLibrary.model.Order;
import pl.cba.gibcode.modelLibrary.ordercomponent.OrderComponentEvent;
import pl.cba.gibcode.orderComponent.command.model.ProcessingWrapper;

public interface CreateEntityStrategy {
	ProcessingWrapper processEvent(OrderComponentEvent event);

}
