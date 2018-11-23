package pl.cba.gibcode.modelLibrary.brand;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModel;
import org.immutables.value.Value;
import pl.cba.gibcode.modelLibrary.ordercomponent.OrderComponentBody;

import javax.validation.Valid;

@ApiModel(value = "UpdateBrandBody", description = "")
@Valid
@JsonDeserialize(as = ImmutableUpdateBrandBody.class)
@JsonSerialize(as = ImmutableUpdateBrandBody.class)
@Value.Immutable
public interface UpdateBrandBody extends OrderComponentBody, BasicBrandBodyFragment, BrandDetailsBodyFragment{

}
