package pl.cba.gibcode.apiQuery.web;

import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.cba.gibcode.apiQuery.service.OrderService;
import pl.cba.gibcode.modelLibrary.model.CreatedOrderUuidBodyFragment;
import pl.cba.gibcode.modelLibrary.model.KafkaMessage;
import pl.cba.gibcode.modelLibrary.model.Order;

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