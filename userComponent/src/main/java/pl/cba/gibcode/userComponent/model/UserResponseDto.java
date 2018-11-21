package pl.cba.gibcode.userComponent.model;

import lombok.Value;
import pl.cba.gibcode.modelLibrary.model.UserType;

import java.util.Set;

@Value(staticConstructor = "of")
public class UserResponseDto {
	private String username;
	private Set<UserType> userRoles;
}
