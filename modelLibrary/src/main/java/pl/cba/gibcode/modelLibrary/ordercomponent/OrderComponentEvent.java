/*
 * C-Vault, @ 2018 Swisscom (Schweiz) AG
 */
package pl.cba.gibcode.modelLibrary.ordercomponent;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModel;
import org.immutables.value.Value;
import pl.cba.gibcode.modelLibrary.model.Event;

@ApiModel(value = "OrderComponentEvent", description = "The OrderComponentEvent contains all the information for triggering a modification.")
@JsonDeserialize(as= ImmutableOrderComponentEvent.class)
@JsonSerialize(as=ImmutableOrderComponentEvent.class)
@Value.Immutable
public interface OrderComponentEvent extends Event<OrderComponentHeader, OrderComponentBody> {

}
