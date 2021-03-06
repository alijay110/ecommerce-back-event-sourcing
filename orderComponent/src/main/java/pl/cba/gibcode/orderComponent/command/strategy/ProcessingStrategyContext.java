package pl.cba.gibcode.orderComponent.command.strategy;

import org.apache.kafka.common.network.Send;
import org.springframework.stereotype.Component;
import pl.cba.gibcode.modelLibrary.model.ActionEnum;
import pl.cba.gibcode.modelLibrary.model.EntityFragment;
import pl.cba.gibcode.modelLibrary.ordercomponent.OrderComponentEvent;
import pl.cba.gibcode.orderComponent.command.model.ProcessingWrapper;

import java.util.HashMap;

import static pl.cba.gibcode.modelLibrary.model.EntityStateEnum.UNKNOWN;
import static pl.cba.gibcode.orderComponent.command.model.ModelBuilderUtils.buildFailureOrder;

@Component
public class ProcessingStrategyContext {

	private final HashMap<ActionEnum, ProcessingStrategy> strategies = new HashMap<>();
	private final DefaultProcessingStrategy defaultProcessingStrategy;

	public ProcessingStrategyContext(
			CreateBrandProcessingStrategy createBrandProcessingStrategy,
			DefaultProcessingStrategy defaultProcessingStrategy,
			UpdateBrandProcessingStrategy updateBrandProcessingStrategy,
			DeleteBrandProcessingStrategy deleteBrandProcessingStrategy,
			CreateCardProcessingStrategy createCardProcessingStrategy,
			UpdateCardProcessingStrategy updateCardProcessingStrategy,
			DeleteCardProcessingStrategy deleteCardProcessingStrategy,
			CheckoutCardProcessingStrategy checkoutCardProcessingStrategy,
			PayCardProcessingStrategy payCardProcessingStrategy,
			SendCardProcessingStrategy sendCardProcessingStrategy,
			ValidateCardProcessingStrategy validateCardProcessingStrategy) {
		this.defaultProcessingStrategy = defaultProcessingStrategy;
		strategies.put(createBrandProcessingStrategy.getAction(), createBrandProcessingStrategy);
		strategies.put(updateBrandProcessingStrategy.getAction(), updateBrandProcessingStrategy);
		strategies.put(deleteBrandProcessingStrategy.getAction(), deleteBrandProcessingStrategy);
		strategies.put(createCardProcessingStrategy.getAction(), createCardProcessingStrategy);
		strategies.put(updateCardProcessingStrategy.getAction(), updateCardProcessingStrategy);
		strategies.put(deleteCardProcessingStrategy.getAction(), deleteCardProcessingStrategy);
		strategies.put(checkoutCardProcessingStrategy.getAction(), checkoutCardProcessingStrategy);
		strategies.put(payCardProcessingStrategy.getAction(), payCardProcessingStrategy);
		strategies.put(sendCardProcessingStrategy.getAction(), sendCardProcessingStrategy);
		strategies.put(validateCardProcessingStrategy.getAction(), validateCardProcessingStrategy);
	}

	public ProcessingWrapper createNewEntity(OrderComponentEvent event) {
		if(event.getHeader().getAction() != ActionEnum.CREATE_BRAND
				&& event.getHeader().getAction() != ActionEnum.CREATE_CARD) {
			var processingWrapper = new ProcessingWrapper();
			processingWrapper.setResponse(buildFailureOrder(event,
					"Provided Action is incorrect: " + event.getHeader().getAction(),
					UNKNOWN));
			return processingWrapper;
		}
		var strategy = (CreateEntityStrategy) strategies
				.getOrDefault(event.getHeader().getAction(), defaultProcessingStrategy);
		return strategy.processEvent(event);
	}

	public ProcessingWrapper updateEntity(OrderComponentEvent event, EntityFragment entity) {
		if(event.getHeader().getAction() == ActionEnum.CREATE_BRAND
				|| event.getHeader().getAction() == ActionEnum.CREATE_CARD) {
			var processingWrapper = new ProcessingWrapper();
			processingWrapper.setResponse(buildFailureOrder(event,
					"Provided Action is incorrect:" + event.getHeader().getAction(),
					UNKNOWN));
			return processingWrapper;
		}
		var strategy = (UpdateEntityStrategy) strategies
				.getOrDefault(event.getHeader().getAction(), defaultProcessingStrategy);
		return strategy.processEvent(event, entity);
	}
}
