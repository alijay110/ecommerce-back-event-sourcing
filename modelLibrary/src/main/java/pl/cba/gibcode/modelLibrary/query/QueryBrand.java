package pl.cba.gibcode.modelLibrary.query;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.value.Value;
import pl.cba.gibcode.modelLibrary.brand.Brand;
import pl.cba.gibcode.modelLibrary.card.CardTypeEnum;
import pl.cba.gibcode.modelLibrary.model.PriceRangeEnum;
import pl.cba.gibcode.modelLibrary.ordercomponent.ImmutableOrderComponentEvent;

import java.util.Optional;
import java.util.Set;

@JsonDeserialize(as= ImmutableQueryBrand.class)
@JsonSerialize(as = ImmutableQueryBrand.class)
@Value.Immutable
public interface QueryBrand {
	Brand getBrand();
	Double getMaxDiscount();
	boolean getIsAvailable();
	boolean getIsDeleted();
	Set<CardTypeEnum> getCardTypes();
	Set<PriceRangeEnum> getPriceRanges();
}
