package pl.cba.gibcode.orderComponent.command.strategy;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import pl.cba.gibcode.modelLibrary.card.CardDetailsBodyFragment;
import pl.cba.gibcode.modelLibrary.card.ImmutableCard;
import pl.cba.gibcode.modelLibrary.model.ActionEnum;
import pl.cba.gibcode.modelLibrary.model.EntityStateEnum;
import pl.cba.gibcode.modelLibrary.ordercomponent.OrderComponentEvent;
import pl.cba.gibcode.orderComponent.command.model.ProcessingWrapper;

import java.util.UUID;

import static pl.cba.gibcode.orderComponent.command.model.ModelBuilderUtils.buildFailureOrder;
import static pl.cba.gibcode.orderComponent.command.model.ModelBuilderUtils.buildSuccessOrder;

@Component
@RequiredArgsConstructor
public class CreateCardProcessingStrategy implements ProcessingStrategy, CreateEntityStrategy {

	private static final Logger logger = LoggerFactory.getLogger(CreateCardProcessingStrategy.class);

	@Override public ProcessingWrapper processEvent(OrderComponentEvent event) {
		logger.info("Creating new card from event {}", event);
		var cardDetails = (CardDetailsBodyFragment) event.getBody();

		var processingWrapper = new ProcessingWrapper();

		UUID cardUuid;
		try {
			cardUuid = UUID.fromString(event.getHeader().getEntityId());
		} catch(Exception ex) {
			processingWrapper.setResponse(buildFailureOrder(event, "Could not cast card entityId to UUID", getState()));
			return processingWrapper;

		}
		processingWrapper.setCard(
				ImmutableCard
						.builder()
						.from(cardDetails)
						.cardUuid(cardUuid)
						.isAvailable(false) //need to be validated
						.sellerId(event.getHeader().getUserId())
						.lastEditedBy(event.getHeader().getUserId()).build()
		);
		processingWrapper.setResponse(buildSuccessOrder(event, getState()));
		return processingWrapper;

	}

	@Override public EntityStateEnum getState() {
		return EntityStateEnum.CARD_CREATED;
	}

	@Override public ActionEnum getAction() {
		return ActionEnum.CREATE_CARD;
	}
}
