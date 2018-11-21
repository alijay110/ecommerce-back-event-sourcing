/*
 * C-Vault, @ 2018 Swisscom (Schweiz) AG
 */
package pl.cba.gibcode.modelLibrary.card;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModel;
import org.immutables.value.Value;
import pl.cba.gibcode.modelLibrary.ordercomponent.OrderComponentBody;

import javax.validation.Valid;

@ApiModel(value = "UpdateCardBody", description = "")
@Valid
@JsonDeserialize(as = ImmutableUpdateCardBody.class)
@JsonSerialize(as = ImmutableUpdateCardBody.class)
@Value.Immutable
public interface UpdateCardBody extends OrderComponentBody, BasicCardBodyFragment, CardDetailsBodyFragment {

}
