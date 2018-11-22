/*
 * C-Vault, @ 2018 Swisscom (Schweiz) AG
 */
package pl.cba.gibcode.modelLibrary.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.immutables.value.Value;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.Instant;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@ApiModel(value = "Header", description = "Contains meta information regarding the Event body element.")
public interface Header extends BasicEntityFragment{

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
