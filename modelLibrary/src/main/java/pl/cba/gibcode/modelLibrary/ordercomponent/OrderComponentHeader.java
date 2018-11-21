/*
 * C-Vault, @ 2018 Swisscom (Schweiz) AG
 */
package pl.cba.gibcode.modelLibrary.ordercomponent;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.immutables.value.Value;
import pl.cba.gibcode.modelLibrary.model.ActionEnum;
import pl.cba.gibcode.modelLibrary.model.Header;

import javax.validation.constraints.NotNull;
import java.time.Instant;

@ApiModel(value = "OrderComponentHeader", description = "Contains meta information regarding the OrderEvent body element.")
@JsonDeserialize(as = ImmutableOrderComponentHeader.class)
@JsonSerialize(as = ImmutableOrderComponentHeader.class)
@Value.Immutable
public interface OrderComponentHeader extends Header {

	@ApiModelProperty(value = "Action", required = true)
	@NotNull
	ActionEnum getAction();

	@NotNull
	OrderType getType();

}
