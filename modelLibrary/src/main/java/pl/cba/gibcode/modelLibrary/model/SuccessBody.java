package pl.cba.gibcode.modelLibrary.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModel;
import org.immutables.value.Value;
import pl.cba.gibcode.modelLibrary.brand.Brand;
import pl.cba.gibcode.modelLibrary.card.Card;
import pl.cba.gibcode.modelLibrary.ordercomponent.OrderComponentEvent;

import java.util.Optional;

@ApiModel(value = "SuccessBody", description = "The FailureBody represents the state triggered by the failed events.")
@JsonDeserialize(as=ImmutableSuccessBody.class)
@JsonSerialize(as=ImmutableSuccessBody.class)
@Value.Immutable
public interface SuccessBody extends Body  {
	Optional<Card> getCard();
	Optional<Brand> getBrand();
}
