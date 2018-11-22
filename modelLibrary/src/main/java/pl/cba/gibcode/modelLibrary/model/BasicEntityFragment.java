package pl.cba.gibcode.modelLibrary.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import javax.validation.constraints.NotNull;

@JsonDeserialize(as = ImmutableBasicEntityFragment.class)
@JsonSerialize(as = ImmutableBasicEntityFragment.class)
public interface BasicEntityFragment {

	@NotNull
	String getEntityId();
}
