package pl.cba.gibcode.modelLibrary.brand;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModel;
import org.immutables.value.Value;
import pl.cba.gibcode.modelLibrary.model.CreatedOrderUuidBodyFragment;
import pl.cba.gibcode.modelLibrary.ordercomponent.OrderComponentBody;

import javax.validation.Valid;

@ApiModel(value = "BasicBrandBody", description = "")
@Valid
@JsonDeserialize(as = ImmutableBasicBrandBody.class)
@JsonSerialize(as = ImmutableBasicBrandBody.class)
@Value.Immutable
public interface BasicBrandBody extends OrderComponentBody, BasicBrandBodyFragment, CreatedOrderUuidBodyFragment {

}
