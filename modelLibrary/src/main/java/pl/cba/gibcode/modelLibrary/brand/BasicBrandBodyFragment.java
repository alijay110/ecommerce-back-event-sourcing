/*
 * C-Vault, @ 2018 Swisscom (Schweiz) AG
 */
package pl.cba.gibcode.modelLibrary.brand;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import pl.cba.gibcode.modelLibrary.model.ImmutableBasicBrandBodyFragment;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@JsonDeserialize(as = ImmutableBasicBrandBodyFragment.class)
@JsonSerialize(as = ImmutableBasicBrandBodyFragment.class)
public interface BasicBrandBodyFragment {
	@NotNull
	String getBrandId();
}
