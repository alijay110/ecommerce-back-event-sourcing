package pl.cba.gibcode.apiQuery.service;

import org.springframework.stereotype.Service;
import pl.cba.gibcode.modelLibrary.model.UserType;

@Service
public class UserServiceDelegate {

	public Long login(String username, UserType userType){
		return 1L;
	}
}
