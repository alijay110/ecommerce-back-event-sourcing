package pl.cba.gibcode.apiCommand.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.cba.gibcode.apiCommand.model.OrderTypeSelector;
import pl.cba.gibcode.modelLibrary.brand.*;
import pl.cba.gibcode.modelLibrary.model.ActionEnum;
import pl.cba.gibcode.modelLibrary.model.CreatedOrderUuidBody;
import pl.cba.gibcode.modelLibrary.model.CreatedOrderUuidBodyFragment;
import pl.cba.gibcode.modelLibrary.model.ImmutableCreatedOrderUuidBody;
import pl.cba.gibcode.modelLibrary.ordercomponent.OrderType;

@Service
@RequiredArgsConstructor
public class BrandService implements OrderTypeSelector {

	private final SendService sendService;
	private final ValidatorService validatorService;

	public BasicBrandBody createBrand(CreateBrandBody createBrandDto, Long userId) {
		validatorService.validateNewBrandCreation(createBrandDto.getName());
		var uuids = sendService.postOrder(createBrandDto.getName(), createBrandDto, ActionEnum.CREATE_BRAND, userId, getOrderType());
		return ImmutableBasicBrandBody.builder().brandId(uuids.getEntityId())
				.orderUuid(uuids.getOrderUuid()).build();
	}

	public CreatedOrderUuidBody updateBrand(UpdateBrandBody dto, Long userId) {
		validatorService.validateBrand(dto.getBrandId());
		var orderUuid = sendService.putOrder(dto.getBrandId(), dto, ActionEnum.UPDATE_BRAND, userId, getOrderType());
		return ImmutableCreatedOrderUuidBody.builder().orderUuid(orderUuid).build();
	}

	public CreatedOrderUuidBody deleteBrand(DeleteBrandBody dto, Long userId) {
		validatorService.validateBrand(dto.getBrandId());
		var orderUuid = sendService.putOrder(dto.getBrandId(), dto, ActionEnum.DELETE_BRAND, userId, getOrderType());
		return ImmutableCreatedOrderUuidBody.builder().orderUuid(orderUuid).build();
	}

	@Override public OrderType getOrderType() {
		return OrderType.BRAND;
	}
}
