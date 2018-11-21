package pl.cba.gibcode.userComponent.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.cba.gibcode.modelLibrary.exceptions.BusinessException;
import pl.cba.gibcode.modelLibrary.model.UserLoginRequestDto;
import pl.cba.gibcode.modelLibrary.model.UserType;
import pl.cba.gibcode.userComponent.mapper.UserMapper;
import pl.cba.gibcode.userComponent.model.*;
import pl.cba.gibcode.userComponent.repository.UserDetailsRepository;
import pl.cba.gibcode.userComponent.repository.UserRepository;

import java.util.Collections;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;
	private final UserDetailsRepository userDetailsRepository;

	@Transactional(readOnly = true)
	public Page<UserResponseDto> findAll(Pageable pageable) {
		return userRepository.findAll(pageable).map(UserMapper::map);
	}

	@Transactional
	public UserResponseDto registerOrAddRole(UserRequestDto userRequestDto){
		Optional<User> user = userRepository.findByUsername(userRequestDto.getUsername());
		if(user.isPresent()){
			if(user.get().getUserTypes().contains(userRequestDto.getUserType())){
				throw new BusinessException(String.format("User with this username %s and role %s already exists", userRequestDto.getUsername(), userRequestDto.getUserType()));
			} else{
				User foundUser = user.get();
				foundUser.getUserTypes().add(userRequestDto.getUserType());
				return UserMapper.map(foundUser);
			}
		}else {
			User newUser = new User();
			newUser.setUsername(userRequestDto.getUsername());
			newUser.setUserTypes(Collections.singleton(userRequestDto.getUserType()));
			User savedUser = userRepository.save(newUser);
			UserDetails userDetails = new UserDetails();
			userDetails.setUser(savedUser);
			userDetails.setFullName(userRequestDto.getFullName());
			userDetails.setAddress(userRequestDto.getAddress());
			userDetailsRepository.save(userDetails);
			return UserMapper.map(savedUser);
		}

	}

	@Transactional
	public UserResponseDto login(UserLoginRequestDto dto){
		User user = userRepository.findByUsername(dto.getUsername()).orElseThrow( () -> new BusinessException("No such username found"));
		if(!user.getUserTypes().contains(dto.getUserType())){
			throw new BusinessException(String.format("User %s is not %s", dto.getUsername(), dto.getUserType()));
		}
		return UserMapper.map(user);
	}

}
