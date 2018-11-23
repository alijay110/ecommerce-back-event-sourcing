package pl.cba.gibcode.modelLibrary.brand;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModel;
import org.immutables.value.Value;
import pl.cba.gibcode.modelLibrary.ordercomponent.OrderComponentBody;

import javax.validation.Valid;

@ApiModel(value = "DeleteBrandBody", description = "")
@Valid
@JsonDeserialize(as = ImmutableDeleteBrandBody.class)
@JsonSerialize(as = ImmutableDeleteBrandBody.class)
@Value.Immutable
public interface DeleteBrandBody extends OrderComponentBody, BasicBrandBodyFragment {

}
