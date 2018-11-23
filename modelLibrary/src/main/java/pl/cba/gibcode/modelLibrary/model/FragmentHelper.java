package pl.cba.gibcode.modelLibrary.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.value.Value;
import pl.cba.gibcode.modelLibrary.brand.BasicBrandBodyFragment;
import pl.cba.gibcode.modelLibrary.brand.BrandDetailsBodyFragment;
import pl.cba.gibcode.modelLibrary.card.BasicCardBodyFragment;
import pl.cba.gibcode.modelLibrary.card.CardDetailsBodyFragment;

// The Json annotation is used only to trigger the xxxBuilder creation of the Value.Include classes
@JsonDeserialize()
@JsonSerialize()
@Value.Include({
		BasicCardBodyFragment.class,
		CreatedOrderUuidBodyFragment.class,
		BasicBrandBodyFragment.class,
		EntityFragment.class,
		BrandDetailsBodyFragment.class,
		CardDetailsBodyFragment.class,
		BasicEntityFragment.class })
/**
 * Helper class to define immutable objects of the xxxFragments to generate an immutable object. This is because
 * @Value.Immutable cannot be set on xxxFragments since they are used to extend a body which is already defined as
 * immutable
 *
 */
public interface FragmentHelper {
}
