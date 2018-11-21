package pl.cba.gibcode.orderComponent.command.strategy;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import pl.cba.gibcode.modelLibrary.brand.BrandDetailsBodyFragment;
import pl.cba.gibcode.modelLibrary.brand.ImmutableBrand;
import pl.cba.gibcode.modelLibrary.model.*;
import pl.cba.gibcode.modelLibrary.ordercomponent.OrderComponentEvent;
import pl.cba.gibcode.orderComponent.command.model.ProcessingWrapper;

import static pl.cba.gibcode.orderComponent.command.model.ModelBuilderUtils.buildSuccessOrder;

@Component
@RequiredArgsConstructor
public class CreateBrandProcessingStrategy implements ProcessingStrategy, CreateEntityStrategy {

	private static final Logger logger = LoggerFactory.getLogger(CreateBrandProcessingStrategy.class);

	@Override public ProcessingWrapper processEvent(OrderComponentEvent event) {
		logger.info("Creating new brand from event {}", event);
		var brandDetails = (BrandDetailsBodyFragment) event.getBody();

		var processingWrapper = new ProcessingWrapper();

		processingWrapper.setBrand(
				ImmutableBrand
						.builder()
						.from(brandDetails)
						.brandId(event.getHeader().getEntityId())
						.lastEditedBy(event.getHeader().getUserId()).build());
		 processingWrapper.setResponse(buildSuccessOrder(event,getState()));

		 return processingWrapper;

	}

	@Override public EntityStateEnum getState() {
		return EntityStateEnum.BRAND_CREATED;
	}

	@Override public ActionEnum getAction() {
		return ActionEnum.CREATE_BRAND;
	}
}
