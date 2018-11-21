package pl.cba.gibcode.modelLibrary.brand;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModel;
import org.immutables.value.Value;
import pl.cba.gibcode.modelLibrary.model.EntityFragment;

@ApiModel(value = "Brand", description = "The Brand represents the state triggered by the CardEvents.")
@JsonDeserialize(as = ImmutableBrand.class)
@JsonSerialize(as = ImmutableBrand.class)
@Value.Immutable
public interface Brand extends EntityFragment, BasicBrandBodyFragment, BrandDetailsBodyFragment {

}
