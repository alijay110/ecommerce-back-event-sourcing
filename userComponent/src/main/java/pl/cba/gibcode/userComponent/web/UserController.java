package pl.cba.gibcode.userComponent.web;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.cba.gibcode.modelLibrary.model.UserLoginRequestDto;
import pl.cba.gibcode.userComponent.model.UserRequestDto;
import pl.cba.gibcode.userComponent.model.UserResponseDto;
import pl.cba.gibcode.userComponent.service.UserService;

import static pl.cba.gibcode.modelLibrary.model.UserType.ADMIN;

@RestController
@Api
@RequestMapping("api")
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;

	@ApiOperation(
			value = "Registers user",
			httpMethod = "POST")
	@ApiResponses({
			@ApiResponse(code = 200, message = "User returned"),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 500, message = "Unknown error")
	})
	@PostMapping("user/register")
	public ResponseEntity<UserResponseDto> registerUser(@RequestBody UserRequestDto userRequestDto) {
		return ResponseEntity.ok(userService.registerOrAddRole(userRequestDto));
	}


	@ApiOperation(
			value = "Login user",
			httpMethod = "POST")
	@ApiResponses({
			@ApiResponse(code = 200, message = "User returned"),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 500, message = "Unknown error")
	})
	@PostMapping("user/login")
	public ResponseEntity<UserResponseDto> loginUser(@RequestBody UserLoginRequestDto dto) {
		return ResponseEntity.ok(userService.login(dto));
	}

	@ApiOperation(
			value = "Get all users for admin",
			notes = "This call is used to get all users for admin",
			httpMethod = "GET")
	@ApiResponses({
			@ApiResponse(code = 200, message = "Users returned"),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 500, message = "Unknown error")
	})
	@GetMapping("admin/users")
	public ResponseEntity<Page<UserResponseDto>> findAllUsers(Pageable pageable, @RequestParam String username) {
		userService.login(new UserLoginRequestDto(username, ADMIN));
		return ResponseEntity.ok(userService.findAll(pageable));
	}
}




