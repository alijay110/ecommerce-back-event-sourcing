package pl.cba.gibcode.orderComponent.command.strategy;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import pl.cba.gibcode.modelLibrary.brand.Brand;
import pl.cba.gibcode.modelLibrary.brand.BrandDetailsBodyFragment;
import pl.cba.gibcode.modelLibrary.brand.ImmutableBrand;
import pl.cba.gibcode.modelLibrary.model.*;
import pl.cba.gibcode.modelLibrary.ordercomponent.OrderComponentEvent;
import pl.cba.gibcode.orderComponent.command.model.ProcessingWrapper;

import static pl.cba.gibcode.orderComponent.command.model.ModelBuilderUtils.buildSuccessOrder;

@Component
@RequiredArgsConstructor
public class UpdateBrandProcessingStrategy implements ProcessingStrategy, UpdateEntityStrategy {

	private static final Logger logger = LoggerFactory.getLogger(UpdateBrandProcessingStrategy.class);

	@Override public ProcessingWrapper processEvent(OrderComponentEvent event, EntityFragment entityFragment) {
		logger.info("Updating brand {} with details {}", entityFragment, event);
		var brandDetails = (BrandDetailsBodyFragment) event.getBody();
		var brand = (Brand) entityFragment;
		var processingWrapper = new ProcessingWrapper();
		processingWrapper.setBrand(ImmutableBrand
				.builder()
				.from(brand)
				.from(brandDetails)
				.lastEditedBy(event.getHeader().getUserId()).build());

		processingWrapper.setResponse(buildSuccessOrder(event,getState()));
		return processingWrapper;
	}

	@Override public EntityStateEnum getState() {
		return EntityStateEnum.BRAND_UPDATED;
	}

	@Override public ActionEnum getAction() {
		return ActionEnum.UPDATE_BRAND;
	}

}
