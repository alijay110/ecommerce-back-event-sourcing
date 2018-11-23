package pl.cba.gibcode.modelLibrary.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModel;
import org.immutables.value.Value;

import javax.validation.Valid;

@ApiModel(value = "CreatedOrderUuidBody", description = "")
@Valid
@JsonDeserialize(as = ImmutableCreatedOrderUuidBody.class)
@JsonSerialize(as = ImmutableCreatedOrderUuidBody.class)
@Value.Immutable
public interface CreatedOrderUuidBody extends CreatedOrderUuidBodyFragment, BasicEntityFragment {

}
