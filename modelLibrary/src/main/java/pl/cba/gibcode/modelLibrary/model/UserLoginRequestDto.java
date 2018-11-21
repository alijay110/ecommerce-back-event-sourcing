package pl.cba.gibcode.modelLibrary.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class UserLoginRequestDto {
	@NotEmpty
	private String username;
	@NotNull
	private UserType userType;

}
