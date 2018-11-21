package pl.cba.gibcode.modelLibrary.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import javax.validation.constraints.NotEmpty;
import java.util.UUID;

@JsonDeserialize(as = ImmutableCreatedOrderUuidBodyFragment.class)
@JsonSerialize(as = ImmutableCreatedOrderUuidBodyFragment.class)
public interface CreatedOrderUuidBodyFragment {
	@NotEmpty
	UUID getOrderUuid();
}
