package pl.cba.gibcode.apiQuery.helper;

import org.springframework.util.CollectionUtils;
import pl.cba.gibcode.apiQuery.model.BrandCriteriaDto;
import pl.cba.gibcode.apiQuery.model.CardCriteriaDto;
import pl.cba.gibcode.apiQuery.model.OrderCriteriaDto;
import pl.cba.gibcode.modelLibrary.exceptions.BusinessException;
import pl.cba.gibcode.modelLibrary.model.JoinedCardWithOrder;
import pl.cba.gibcode.modelLibrary.model.Order;
import pl.cba.gibcode.modelLibrary.model.PriceRangeEnum;
import pl.cba.gibcode.modelLibrary.query.QueryBrand;
import pl.cba.gibcode.modelLibrary.query.QueryCardsByBrand;

import static java.util.Objects.isNull;

public class CriteriaBuilderHelper {

	public static boolean meetsCriteria(BrandCriteriaDto criteria, QueryBrand queryBrand) {
		var correctBrandLetter = isNull(criteria.getBrandLetter()) || queryBrand.getBrand().getName()
				.startsWith(criteria.getBrandLetter());
		var correctCardType =
				isNull(criteria.getCardTypeId()) || queryBrand.getCardTypes().contains(criteria.getCardTypeId());
		var correctCategory = isNull(criteria.getCategoryId()) || queryBrand.getBrand().getCategories()
				.contains(criteria.getCategoryId());
		var correctPriceRange =
				isNull(criteria.getPriceRangeId()) || queryBrand.getPriceRanges().contains(criteria.getPriceRangeId());
		return !queryBrand.getIsDeleted() && correctBrandLetter && correctCardType && correctCategory
				&& correctPriceRange;
	}
	public static boolean meetsCriteria(CardCriteriaDto cardCriteriaDto, QueryCardsByBrand queryCardsByBrand){
		if(isNull(cardCriteriaDto.getBrandName())){
			throw new BusinessException("No card brand name provided.");
		}
		if(!queryCardsByBrand
				.getCards().isPresent()){
			throw new BusinessException("No cards found for this brand");
		}
		if(CollectionUtils.isEmpty(queryCardsByBrand.getCards().get())){
			throw new BusinessException("No cards found for this brand");
		}
		var correctPriceRange =
				isNull(cardCriteriaDto.getPriceRangeId()) || queryCardsByBrand.getPriceRanges().contains(cardCriteriaDto.getPriceRangeId());
		return !queryCardsByBrand.getIsDeleted() && correctPriceRange;
	}

	public static boolean meetsCriteria(CardCriteriaDto criteria, JoinedCardWithOrder joinedCardWithOrder) {
		var correctPriceRange =
				isNull(criteria.getPriceRangeId()) || criteria.getPriceRangeId().equals(PriceRangeEnum.decideRangeFrom(joinedCardWithOrder.getCard().getPrice().doubleValue()));
		var correctBrand = isNull(criteria.getBrandName()) || joinedCardWithOrder.getCard().getBrandName().equals(criteria.getBrandName());
		return correctPriceRange && correctBrand;
	}

	public static boolean meetsCriteria(OrderCriteriaDto criteriaDto, JoinedCardWithOrder joinedCardWithOrder) {
		var correctEntityState = isNull(criteriaDto.getEntityState()) || criteriaDto.getEntityState().equals(joinedCardWithOrder.getOrder().getEntityState());
		var correctOrderType = isNull(criteriaDto.getOrderType()) || criteriaDto.getOrderType().equals(joinedCardWithOrder.getOrder().getType());
		var correctOrderState = isNull(criteriaDto.getOrderState()) || criteriaDto.getOrderState().equals(joinedCardWithOrder.getOrder().getOrderState());
		return correctEntityState && correctOrderType && correctOrderState;
	}

	public static boolean meetsCriteria(OrderCriteriaDto criteriaDto, Order order) {
		var correctEntityState = isNull(criteriaDto.getEntityState()) || criteriaDto.getEntityState().equals(order.getEntityState());
		var correctOrderType = isNull(criteriaDto.getOrderType()) || criteriaDto.getOrderType().equals(order.getType());
		var correctOrderState = isNull(criteriaDto.getOrderState()) || criteriaDto.getOrderState().equals(order.getOrderState());
		return correctEntityState && correctOrderType && correctOrderState;
	}
}
