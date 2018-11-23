package pl.cba.gibcode.apiQuery.helper;

import pl.cba.gibcode.apiQuery.model.BrandCriteriaDto;
import pl.cba.gibcode.modelLibrary.query.QueryBrand;

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
}
