package pl.cba.gibcode.modelLibrary.brand;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModel;
import org.immutables.value.Value;

import java.util.List;

@JsonDeserialize(as=ModifiableBrandList.class)
@JsonSerialize(as=ModifiableBrandList.class)
@Value.Modifiable
public interface BrandList {
	List<Brand> getBrands();
}
