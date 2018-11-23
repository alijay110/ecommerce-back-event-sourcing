package pl.cba.gibcode.apiCommand.service;

import org.springframework.stereotype.Service;
import pl.cba.gibcode.modelLibrary.model.UserType;

@Service
public class UserServiceDelegate {

	public Long login(String username, UserType userType) {

		switch(userType) {
		case ADMIN:
			return 1L;
		case BUYER:
			return 2L;
		case SELLER:
			return 3L;
		default:
			return 4L;
		}
	}
}
