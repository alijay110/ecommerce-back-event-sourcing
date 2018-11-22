package pl.cba.gibcode.orderComponent.command.strategy;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import pl.cba.gibcode.modelLibrary.card.Card;
import pl.cba.gibcode.modelLibrary.card.ImmutableCard;
import pl.cba.gibcode.modelLibrary.model.ActionEnum;
import pl.cba.gibcode.modelLibrary.model.EntityFragment;
import pl.cba.gibcode.modelLibrary.model.EntityStateEnum;
import pl.cba.gibcode.modelLibrary.ordercomponent.OrderComponentEvent;
import pl.cba.gibcode.orderComponent.command.model.ProcessingWrapper;

import static pl.cba.gibcode.orderComponent.command.model.ModelBuilderUtils.getBuildSuccessOrderBuilder;

@Component
@RequiredArgsConstructor
public class SendCardProcessingStrategy implements ProcessingStrategy, UpdateEntityStrategy {

	private static final Logger logger = LoggerFactory.getLogger(SendCardProcessingStrategy.class);

	@Override public ProcessingWrapper processEvent(
			OrderComponentEvent event, EntityFragment entityFragment) {
		logger.info("Send card {} with details {}", entityFragment, event);
		var card = (Card) entityFragment;
		var processingWrapper = new ProcessingWrapper();
		processingWrapper.setResponse(
				getBuildSuccessOrderBuilder(event, getState())
						.buyerId(event.getHeader().getUserId())
						.sellerId(card.getSellerId())
						.build());
		processingWrapper.setCard(ImmutableCard
				.builder()
				.from(card)
				.deleted(true)
				.lastEditedBy(event.getHeader().getUserId()).build());
		return processingWrapper;
	}

	@Override public EntityStateEnum getState() {
		return EntityStateEnum.CARD_SENT;
	}

	@Override public ActionEnum getAction() {
		return ActionEnum.SEND_CARD;
	}

}