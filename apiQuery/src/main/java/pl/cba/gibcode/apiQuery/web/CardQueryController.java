/*
package pl.cba.gibcode.apiQuery.web;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Api
@RequestMapping("api")
@RequiredArgsConstructor
public class CardQueryController {


	@ApiOperation(
			value = "Get all cards for admin",
			notes = "This call is used to get all cards filtered for admin",
			httpMethod = "POST")
	@ApiResponses({
			@ApiResponse(code = 200, message = "Cards returned"),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 500, message = "Unknown error")
	})
	@PostMapping("admin/cards")
	public ResponseEntity<Page<CardResponseDto>> findAllCards(Pageable pageable, @RequestBody CardCriteriaDto criteria, @RequestParam String username) {
		//pretend validation for admin
		userService.loginAsAdmin(username);
		return ResponseEntity.ok(cardService.findAllForAdmin(pageable, criteria));
	}

	@ApiOperation(
			value = "Get all cards for sale",
			notes = "This call is used to get all cards filtered",
			httpMethod = "POST")
	@ApiResponses({
			@ApiResponse(code = 200, message = "Cards returned"),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 500, message = "Unknown error")
	})
	@PostMapping("cards/list")
	public ResponseEntity<Page<CardResponseDto>> getCardsBy(Pageable pageable, @RequestBody CardCriteriaDto criteria) {
		return ResponseEntity.ok(cardService.findAll(pageable, criteria));
	}
}
*/
