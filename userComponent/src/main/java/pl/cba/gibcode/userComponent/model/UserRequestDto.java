package pl.cba.gibcode.userComponent.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.cba.gibcode.modelLibrary.model.UserType;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@NoArgsConstructor
@Getter
@Setter
public class UserRequestDto {
	@NotEmpty
	private String username;
	@NotNull
	private UserType userType;
	@NotEmpty
	private String fullName;
	@NotEmpty
	private String address;

}
