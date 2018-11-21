package pl.cba.gibcode.modelLibrary.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.value.Value;

import javax.validation.constraints.NotNull;
import java.time.Instant;

@JsonDeserialize(as = ImmutableEntityFragment.class)
@JsonSerialize(as = ImmutableEntityFragment.class)
public interface EntityFragment {
	@NotNull
	Long getLastEditedBy();

	@NotNull
	@Value.Default
	default Boolean getDeleted(){ return Boolean.FALSE;};

	@NotNull
	@Value.Default
	default Instant getTimestamp() {
		return Instant.now();
	}
}
