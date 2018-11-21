package pl.cba.gibcode.orderComponent.command.strategy;

import pl.cba.gibcode.modelLibrary.model.*;

public interface ProcessingStrategy {

	EntityStateEnum getState();
	ActionEnum getAction();

}
