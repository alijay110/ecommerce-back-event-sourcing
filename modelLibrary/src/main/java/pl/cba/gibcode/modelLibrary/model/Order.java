package pl.cba.gibcode.modelLibrary.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModel;
import org.immutables.value.Value;
import pl.cba.gibcode.modelLibrary.ordercomponent.OrderType;

import javax.validation.constraints.NotNull;
import java.util.Optional;
import java.util.UUID;

@ApiModel(value = "Order", description = "The Order represents the state triggered by the OrderEvent.")
@JsonDeserialize(as = ImmutableOrder.class)
@JsonSerialize(as = ImmutableOrder.class)
@Value.Immutable
public interface Order {

	@NotNull
	EntityStateEnum getEntityState();

	@NotNull
	OrderType getType();

	@NotNull
	OrderStateEnum getOrderState();

	@NotNull
	UUID getOrderUuid();

	Optional<String> getErrorMessage();

	Optional<Long> getSellerId();

	Optional<Long> getBuyerId();

	@NotNull
	String getEntityId();
}
