package pl.cba.gibcode.orderComponent.command.strategy;

import org.springframework.stereotype.Component;
import pl.cba.gibcode.modelLibrary.model.*;
import pl.cba.gibcode.modelLibrary.ordercomponent.OrderComponentEvent;
import pl.cba.gibcode.orderComponent.command.model.ProcessingWrapper;

import static pl.cba.gibcode.orderComponent.command.model.ModelBuilderUtils.buildFailureOrder;

@Component
public class DefaultProcessingStrategy implements CreateEntityStrategy, UpdateEntityStrategy, ProcessingStrategy {

	private ActionEnum actionEnum;

	@Override public ProcessingWrapper processEvent(OrderComponentEvent event) {
		actionEnum = event.getHeader().getAction();
		var processingWrapper = new ProcessingWrapper();
		processingWrapper.setResponse(buildFailureOrder(event, "No strategy found to process this message", getState()));
		return processingWrapper;
	}
	@Override public ProcessingWrapper processEvent(OrderComponentEvent event, EntityFragment entityFragment) {
		actionEnum = event.getHeader().getAction();
		var processingWrapper = new ProcessingWrapper();
		processingWrapper.setResponse(buildFailureOrder(event, "No strategy found to process this message", getState()));
		return processingWrapper;
	}

	@Override public EntityStateEnum getState() {
		return EntityStateEnum.UNKNOWN;
	}

	@Override public ActionEnum getAction() {
		return actionEnum;
	}


}
