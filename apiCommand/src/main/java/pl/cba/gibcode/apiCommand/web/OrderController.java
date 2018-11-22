package pl.cba.gibcode.apiCommand.web;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.cba.gibcode.apiCommand.service.BrandService;
import pl.cba.gibcode.apiCommand.service.OrderService;
import pl.cba.gibcode.apiCommand.service.UserServiceDelegate;
import pl.cba.gibcode.modelLibrary.brand.BasicBrandBody;
import pl.cba.gibcode.modelLibrary.brand.CreateBrandBody;
import pl.cba.gibcode.modelLibrary.brand.DeleteBrandBody;
import pl.cba.gibcode.modelLibrary.brand.UpdateBrandBody;
import pl.cba.gibcode.modelLibrary.model.CreatedOrderUuidBody;
import pl.cba.gibcode.modelLibrary.model.CreatedOrderUuidBodyFragment;
import pl.cba.gibcode.modelLibrary.model.Order;
import pl.cba.gibcode.modelLibrary.model.UserType;

import javax.validation.Valid;

@RestController
@Api
@RequestMapping("api")
@RequiredArgsConstructor
public class OrderController {

	private final OrderService orderService;

	@ApiOperation(
			value = "Gets order state",
			notes = "This call is used to get order uuid",
			httpMethod = "POST")
	@ApiResponses({
			@ApiResponse(code = 201, message = "Created brand returned"),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 500, message = "Unknown error")
	})
	@PostMapping("orders")
	public ResponseEntity<Order> getOrderUuid(
			@Valid @RequestBody CreatedOrderUuidBodyFragment orderUuidBodyFragment) {

		return ResponseEntity.status(HttpStatus.OK).body(orderService.getOrder(orderUuidBodyFragment.getOrderUuid()));
	}

}
