/*
 * C-Vault, @ 2018 Swisscom (Schweiz) AG
 */
package pl.cba.gibcode.modelLibrary.card;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import pl.cba.gibcode.modelLibrary.model.ImmutableBasicCardBodyFragment;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@JsonDeserialize(as = ImmutableBasicCardBodyFragment.class)
@JsonSerialize(as = ImmutableBasicCardBodyFragment.class)
public interface BasicCardBodyFragment {
	@NotNull
	UUID getCardUuid();
}
