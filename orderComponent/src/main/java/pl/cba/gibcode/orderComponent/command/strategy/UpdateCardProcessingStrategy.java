package pl.cba.gibcode.orderComponent.command.strategy;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import pl.cba.gibcode.modelLibrary.brand.Brand;
import pl.cba.gibcode.modelLibrary.brand.BrandDetailsBodyFragment;
import pl.cba.gibcode.modelLibrary.brand.ImmutableBrand;
import pl.cba.gibcode.modelLibrary.card.Card;
import pl.cba.gibcode.modelLibrary.card.CardDetailsBodyFragment;
import pl.cba.gibcode.modelLibrary.card.ImmutableCard;
import pl.cba.gibcode.modelLibrary.model.ActionEnum;
import pl.cba.gibcode.modelLibrary.model.EntityFragment;
import pl.cba.gibcode.modelLibrary.model.EntityStateEnum;
import pl.cba.gibcode.modelLibrary.ordercomponent.OrderComponentEvent;
import pl.cba.gibcode.orderComponent.command.model.ProcessingWrapper;

import java.util.UUID;

import static pl.cba.gibcode.orderComponent.command.model.ModelBuilderUtils.buildFailureOrder;
import static pl.cba.gibcode.orderComponent.command.model.ModelBuilderUtils.buildSuccessOrder;

@Component
@RequiredArgsConstructor
public class UpdateCardProcessingStrategy implements ProcessingStrategy, UpdateEntityStrategy {

	private static final Logger logger = LoggerFactory.getLogger(UpdateCardProcessingStrategy.class);

	@Override public ProcessingWrapper processEvent(
			OrderComponentEvent event, EntityFragment entityFragment) {
		logger.info("Updating card {} with details {}", entityFragment, event);
		var cardDetails = (CardDetailsBodyFragment) event.getBody();
		var card = (Card) entityFragment;
		var processingWrapper = new ProcessingWrapper();
		processingWrapper.setCard(ImmutableCard
				.builder()
				.from(card)
				.from(cardDetails)
				.lastEditedBy(event.getHeader().getUserId()).build());

		processingWrapper.setResponse(buildSuccessOrder(event, getState()));
		return processingWrapper;
	}

	@Override public EntityStateEnum getState() {
		return EntityStateEnum.CARD_UPDATED;
	}

	@Override public ActionEnum getAction() {
		return ActionEnum.UPDATE_CARD;
	}

}