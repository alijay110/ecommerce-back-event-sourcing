package pl.cba.gibcode.apiCommand.web;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.cba.gibcode.apiCommand.service.CardService;
import pl.cba.gibcode.apiCommand.service.UserServiceDelegate;
import pl.cba.gibcode.modelLibrary.card.BasicCardBody;
import pl.cba.gibcode.modelLibrary.card.CreateCardBody;
import pl.cba.gibcode.modelLibrary.card.DeleteCardBody;
import pl.cba.gibcode.modelLibrary.card.UpdateCardBody;
import pl.cba.gibcode.modelLibrary.model.CreatedOrderUuidBody;
import pl.cba.gibcode.modelLibrary.model.CreatedOrderUuidBodyFragment;
import pl.cba.gibcode.modelLibrary.model.UserType;

@RestController
@Api
@RequestMapping("api")
@RequiredArgsConstructor
public class CardCommandController {

	private final UserServiceDelegate userServiceDelegate;
	private final CardService cardService;

	@ApiOperation(
			value = "Create a card as a seller",
			httpMethod = "POST")
	@ApiResponses({
			@ApiResponse(code = 201, message = "Card returned"),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 500, message = "Unknown error")
	})
	@PostMapping("seller/card")
	public ResponseEntity<CreatedOrderUuidBody> createCard(
			@RequestBody CreateCardBody dto,
			@RequestParam String username) {
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(cardService.createCard(dto, userServiceDelegate.login(username, UserType.SELLER)));
	}

	@ApiOperation(
			value = "Validate card for admin",
			httpMethod = "POST")
	@ApiResponses({
			@ApiResponse(code = 202, message = "Card returned"),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 500, message = "Unknown error")
	})
	@PostMapping("admin/cards/validate")
	public ResponseEntity<CreatedOrderUuidBody> validateCard(@RequestBody BasicCardBody dto, @RequestParam String username) {
		var response = cardService.validate(dto, userServiceDelegate.login(username, UserType.ADMIN));
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
	}

	@ApiOperation(
			value = "Delete card for admin",
			httpMethod = "DELETE")
	@ApiResponses({
			@ApiResponse(code = 202, message = "Card returned"),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 500, message = "Unknown error")
	})
	@DeleteMapping("admin/cards")
	public ResponseEntity<CreatedOrderUuidBody> deleteCardForAdmin(@RequestBody DeleteCardBody dto, @RequestParam String username) {
		var response = cardService.deleteCard(dto, userServiceDelegate.login(username, UserType.ADMIN));
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
	}

	@ApiOperation(
			value = "Update a card as a seller",
			httpMethod = "PUT")
	@ApiResponses({
			@ApiResponse(code = 202, message = "Card returned"),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 500, message = "Unknown error")
	})
	@PutMapping("seller/card")
	public ResponseEntity<CreatedOrderUuidBody> updateCard(
			@RequestBody UpdateCardBody dto,
			@RequestParam String username) {
		var response = cardService.updateCard(dto, userServiceDelegate.login(username, UserType.SELLER));
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
	}

	@ApiOperation(
			value = "Send the card",
			httpMethod = "PUT")
	@ApiResponses({
			@ApiResponse(code = 202, message = "Card returned"),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 500, message = "Unknown error")
	})
	@PutMapping("seller/card/send")
	public ResponseEntity<CreatedOrderUuidBody> sendCard(
			@RequestBody BasicCardBody dto,
			@RequestParam String username) {
		var response = cardService.sendCard(dto, userServiceDelegate.login(username, UserType.SELLER));
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
	}

	@ApiOperation(
			value = "Delete a card as a seller",
			httpMethod = "DELETE")
	@ApiResponses({
			@ApiResponse(code = 202, message = "Card returned"),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 500, message = "Unknown error")
	})
	@DeleteMapping("seller/card")
	public ResponseEntity<CreatedOrderUuidBody> deleteCard(
			@RequestBody DeleteCardBody dto,
			@RequestParam String username) {
		var response = cardService.deleteCard(dto, userServiceDelegate.login(username, UserType.SELLER));
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
	}

	@ApiOperation(
			value = "Checkout a card as a buyer",
			httpMethod = "POST")
	@ApiResponses({
			@ApiResponse(code = 202, message = "New Transaction returned"),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 500, message = "Unknown error")
	})
	@PostMapping("buyer/checkout")
	public ResponseEntity<CreatedOrderUuidBody> checkout(
			@RequestBody BasicCardBody dto,
			@RequestParam String username) {
		var response = cardService.checkout(dto, userServiceDelegate.login(username, UserType.BUYER));
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
	}

	@ApiOperation(
			value = "Pay a card as a buyer",
			httpMethod = "POST")
	@ApiResponses({
			@ApiResponse(code = 202, message = "New Transaction returned"),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 500, message = "Unknown error")
	})
	@PostMapping("buyer/pay")
	public ResponseEntity<CreatedOrderUuidBody> pay(
			@RequestBody BasicCardBody dto,
			@RequestParam String username) {
		var response = cardService.pay(dto, userServiceDelegate.login(username, UserType.BUYER));
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
	}

}
