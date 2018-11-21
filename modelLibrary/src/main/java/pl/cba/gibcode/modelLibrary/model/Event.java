/*
 * C-Vault, @ 2018 Swisscom (Schweiz) AG
 */
package pl.cba.gibcode.modelLibrary.model;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.immutables.value.Value;

import javax.validation.constraints.NotNull;


/**
 * Common Event for all Components
 *
 * @param <H> generic header type
 * @param <B> generic body type
 */
public interface Event<H extends Header, B extends Body> {

	@NotNull
	@Value.Parameter
	H getHeader();

	@NotNull
	@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
	@Value.Parameter
	B getBody();
}
