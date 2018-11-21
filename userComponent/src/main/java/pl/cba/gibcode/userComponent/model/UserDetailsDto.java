package pl.cba.gibcode.userComponent.model;

import lombok.Value;

@Value(staticConstructor = "of")
public class UserDetailsDto {
	private String username;
	private String fullName;
	private String address;
}
