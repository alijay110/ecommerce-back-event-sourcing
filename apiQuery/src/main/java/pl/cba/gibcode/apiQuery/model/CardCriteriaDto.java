package pl.cba.gibcode.apiQuery.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.cba.gibcode.modelLibrary.model.PriceRangeEnum;

import javax.validation.constraints.NotEmpty;

@NoArgsConstructor
@Getter
@Setter
public class CardCriteriaDto {
	private PriceRangeEnum priceRangeId;
	private String brandName;

}
