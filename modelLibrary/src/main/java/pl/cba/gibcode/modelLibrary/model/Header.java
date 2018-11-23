package pl.cba.gibcode.modelLibrary.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.util.UUID;

@ApiModel(value = "Header", description = "Contains meta information regarding the Event body element.")
public interface Header extends BasicEntityFragment {

	/**
	 * @return uuid of the Order
	 */
	@ApiModelProperty(value = "uuid of the Order", required = true)
	@NotNull
	UUID getOrderUuid();

	@NotNull
	Long getUserId();

	@NotNull
	Instant getCreationTimestamp();

}
