package pl.cba.gibcode.modelLibrary.query;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.value.Value;
import pl.cba.gibcode.modelLibrary.model.JoinedCardWithOrder;
import pl.cba.gibcode.modelLibrary.model.PriceRangeEnum;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@JsonDeserialize(as = ImmutableQueryCardsByBrand.class)
@JsonSerialize(as = ImmutableQueryCardsByBrand.class)
@Value.Immutable
public interface QueryCardsByBrand {

	boolean getIsAvailable();

	boolean getIsDeleted();

	Optional<List<JoinedCardWithOrder>> getCards();

	Set<PriceRangeEnum> getPriceRanges();

}
