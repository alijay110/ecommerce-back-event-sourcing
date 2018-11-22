package pl.cba.gibcode.orderComponent.command.strategy;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import pl.cba.gibcode.modelLibrary.brand.Brand;
import pl.cba.gibcode.modelLibrary.brand.ImmutableBrand;
import pl.cba.gibcode.modelLibrary.card.Card;
import pl.cba.gibcode.modelLibrary.card.ImmutableCard;
import pl.cba.gibcode.modelLibrary.model.ActionEnum;
import pl.cba.gibcode.modelLibrary.model.EntityFragment;
import pl.cba.gibcode.modelLibrary.model.EntityStateEnum;
import pl.cba.gibcode.modelLibrary.ordercomponent.OrderComponentEvent;
import pl.cba.gibcode.orderComponent.command.model.ProcessingWrapper;

import static pl.cba.gibcode.orderComponent.command.model.ModelBuilderUtils.buildSuccessOrder;

@Component
@RequiredArgsConstructor
public class DeleteCardProcessingStrategy implements ProcessingStrategy, UpdateEntityStrategy {

	private static final Logger logger = LoggerFactory.getLogger(DeleteCardProcessingStrategy.class);

	@Override public ProcessingWrapper processEvent(OrderComponentEvent event, EntityFragment entityFragment) {
		logger.info("Deleting card {}", entityFragment);
		var card = (Card) entityFragment;
		var processingWrapper = new ProcessingWrapper();
		processingWrapper.setCard(ImmutableCard
				.builder()
				.from(card)
				.isAvailable(false)
				.deleted(Boolean.TRUE)
				.lastEditedBy(event.getHeader().getUserId()).build());

		processingWrapper.setResponse(buildSuccessOrder(event,getState()));
		return processingWrapper;
	}

	@Override public EntityStateEnum getState() {
		return EntityStateEnum.CARD_DELETED;
	}

	@Override public ActionEnum getAction() {
		return ActionEnum.DELETE_CARD;
	}

}
