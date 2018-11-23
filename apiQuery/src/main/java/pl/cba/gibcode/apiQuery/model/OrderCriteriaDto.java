package pl.cba.gibcode.apiQuery.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.cba.gibcode.modelLibrary.model.EntityStateEnum;
import pl.cba.gibcode.modelLibrary.model.OrderStateEnum;
import pl.cba.gibcode.modelLibrary.ordercomponent.OrderType;

@NoArgsConstructor
@Getter
@Setter
public class OrderCriteriaDto {
	private EntityStateEnum entityState;
	private OrderType orderType;
	private OrderStateEnum orderState;
}
