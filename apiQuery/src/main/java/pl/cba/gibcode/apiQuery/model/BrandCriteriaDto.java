package pl.cba.gibcode.apiQuery.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.cba.gibcode.modelLibrary.card.CardTypeEnum;
import pl.cba.gibcode.modelLibrary.model.CategoryEnum;
import pl.cba.gibcode.modelLibrary.model.PriceRangeEnum;

@NoArgsConstructor
@Getter
@Setter
public class BrandCriteriaDto {
    private String brandLetter;
    private PriceRangeEnum priceRangeId;
    private CategoryEnum categoryId;
    private CardTypeEnum cardTypeId;
}
