/*
 * C-Vault, @ 2018 Swisscom (Schweiz) AG
 */
package pl.cba.gibcode.modelLibrary.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModel;
import org.immutables.value.Value;
import pl.cba.gibcode.modelLibrary.brand.BasicBrandBodyFragment;
import pl.cba.gibcode.modelLibrary.brand.ImmutableBasicBrandBody;
import pl.cba.gibcode.modelLibrary.card.BasicCardBodyFragment;
import pl.cba.gibcode.modelLibrary.ordercomponent.OrderComponentBody;

import javax.validation.Valid;

@ApiModel(value = "CreatedOrderUuidBody", description = "")
@Valid
@JsonDeserialize(as = ImmutableCreatedOrderUuidBody.class)
@JsonSerialize(as = ImmutableCreatedOrderUuidBody.class)
@Value.Immutable
public interface CreatedOrderUuidBody extends CreatedOrderUuidBodyFragment, BasicEntityFragment {

}
