package pl.cba.gibcode.modelLibrary.brand;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import pl.cba.gibcode.modelLibrary.model.CategoryEnum;
import pl.cba.gibcode.modelLibrary.model.ImmutableBrandDetailsBodyFragment;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Set;

@JsonDeserialize(as = ImmutableBrandDetailsBodyFragment.class)
@JsonSerialize(as = ImmutableBrandDetailsBodyFragment.class)
public interface BrandDetailsBodyFragment {
	@NotEmpty
	String getName();

	String getImageUrl();

	@NotNull
	Set<CategoryEnum> getCategories();
}
