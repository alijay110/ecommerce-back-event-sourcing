package pl.cba.gibcode.modelLibrary.brand;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import pl.cba.gibcode.modelLibrary.model.ImmutableBasicBrandBodyFragment;

import javax.validation.constraints.NotNull;

@JsonDeserialize(as = ImmutableBasicBrandBodyFragment.class)
@JsonSerialize(as = ImmutableBasicBrandBodyFragment.class)
public interface BasicBrandBodyFragment {
	@NotNull
	String getBrandId();
}
