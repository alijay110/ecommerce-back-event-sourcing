package pl.cba.gibcode.modelLibrary.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModel;
import org.immutables.value.Value;
import pl.cba.gibcode.modelLibrary.ordercomponent.OrderComponentEvent;

import java.util.Optional;

@ApiModel(value = "FailureBody", description = "The FailureBody represents the state triggered by the failed events.")
@JsonDeserialize(as=ImmutableFailureBody.class)
@JsonSerialize(as=ImmutableFailureBody.class)
@Value.Immutable
public interface FailureBody extends Body {
	String getReason();
	Optional<OrderComponentEvent> getEvent();
}
