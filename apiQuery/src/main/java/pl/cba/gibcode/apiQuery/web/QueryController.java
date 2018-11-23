package pl.cba.gibcode.apiQuery.web;

import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.cba.gibcode.apiQuery.model.OrderCriteriaDto;
import pl.cba.gibcode.apiQuery.service.OrderService;
import pl.cba.gibcode.apiQuery.service.UserServiceDelegate;
import pl.cba.gibcode.modelLibrary.model.*;

import javax.validation.Valid;
import javax.ws.rs.*;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@RestController
@Api
@RequestMapping("api")
@RequiredArgsConstructor
public class QueryController {

	private final OrderService orderService;
	private final UserServiceDelegate userServiceDelegate;

	@ApiOperation(
			value = "Gets order state",
			notes = "This call is used to get order uuid",
			httpMethod = "POST")
	@PostMapping("orders")
	public ResponseEntity<Order> getOrderUuid(
			@Valid @RequestBody CreatedOrderUuidBodyFragment orderUuidBodyFragment) {
		return ResponseEntity.status(HttpStatus.OK).body(orderService.getOrder(orderUuidBodyFragment.getOrderUuid()));
	}

	@ApiOperation(
			value = "Get all transactions for buyer",
			httpMethod = "POST")
	@PostMapping("buyer/transactions")
	public ResponseEntity<Page<JoinedCardWithOrder>> findAllTransactionForBuyer(Pageable pageable, @RequestBody OrderCriteriaDto criteria, @RequestParam String username) {
		var userId = userServiceDelegate.login(username, UserType.BUYER);
		return ResponseEntity.ok(orderService.getOrdersForBuyer(pageable, userId, criteria));
	}

	@ApiOperation(
			value = "Get all transactions for seller",
			httpMethod = "POST")
	@PostMapping("seller/transactions")
	public ResponseEntity<Page<JoinedCardWithOrder>> findAllTransactionForSeller(Pageable pageable, @RequestBody OrderCriteriaDto criteria, @RequestParam String username) {
		var userId = userServiceDelegate.login(username, UserType.SELLER);
		return ResponseEntity.ok(orderService.getOrdersForSeller(pageable, userId, criteria));
	}

	@ApiOperation(
			value = "Get all transactions for admin",
			httpMethod = "POST")
	@PostMapping("admin/transactions")
	public ResponseEntity<Page<Order>> findAllTransactionsForAdmin(Pageable pageable, @RequestBody OrderCriteriaDto criteria, @RequestParam String username) {
		userServiceDelegate.login(username, UserType.ADMIN);
		return ResponseEntity.ok(orderService.getOrdersForAdmin(pageable, criteria));
	}



}