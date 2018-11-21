package pl.cba.gibcode.modelLibrary.card;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModel;
import org.immutables.value.Value;
import pl.cba.gibcode.modelLibrary.model.EntityFragment;

@ApiModel(value = "Card", description = "The Card represents the state triggered by the CardEvents.")
@JsonDeserialize(as = ImmutableCard.class)
@JsonSerialize(as = ImmutableCard.class)
@Value.Immutable
public interface Card extends EntityFragment, BasicCardBodyFragment, CardDetailsBodyFragment {

}
