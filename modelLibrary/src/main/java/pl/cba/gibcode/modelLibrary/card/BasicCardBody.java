package pl.cba.gibcode.modelLibrary.card;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModel;
import org.immutables.value.Value;
import pl.cba.gibcode.modelLibrary.model.CreatedOrderUuidBodyFragment;
import pl.cba.gibcode.modelLibrary.ordercomponent.OrderComponentBody;

import javax.validation.Valid;

@ApiModel(value = "BasicCardBody", description = "")
@Valid
@JsonDeserialize(as = ImmutableBasicCardBody.class)
@JsonSerialize(as = ImmutableBasicCardBody.class)
@Value.Immutable
public interface BasicCardBody extends OrderComponentBody, BasicCardBodyFragment {

}
