package pl.cba.gibcode.userComponent.mapper;

import pl.cba.gibcode.userComponent.model.User;
import pl.cba.gibcode.userComponent.model.UserResponseDto;

public class UserMapper {


	public static UserResponseDto map(User user){
		return UserResponseDto.of(user.getUsername(), user.getUserTypes());
	}
}
