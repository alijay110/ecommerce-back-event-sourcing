package pl.cba.gibcode.apiQuery.model;

import lombok.Value;
import pl.cba.gibcode.modelLibrary.card.CardTypeEnum;
import pl.cba.gibcode.modelLibrary.model.Order;
import pl.cba.gibcode.modelLibrary.model.PriceRangeEnum;

import java.math.BigDecimal;
import java.util.UUID;

@Value(staticConstructor = "of")
public class CardResponseForAdminDto {
	private UUID cardUuid;
	private Double price;
	private Double discount;
	private CardTypeEnum cardType;
	private String brandName;
	private Order order;
}
