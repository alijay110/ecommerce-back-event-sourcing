package pl.cba.gibcode.modelLibrary.card;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import pl.cba.gibcode.modelLibrary.model.ImmutableCardDetailsBodyFragment;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.UUID;

@JsonDeserialize(as = ImmutableCardDetailsBodyFragment.class)
@JsonSerialize(as = ImmutableCardDetailsBodyFragment.class)
public interface CardDetailsBodyFragment {
	@NotNull
	BigDecimal getMarketValue();
	@NotNull
	BigDecimal getPrice();
	@NotNull
	CardTypeEnum getCardType();
	@NotEmpty
	String getBrandName();
	@NotEmpty
	String getCardNumber();
}
