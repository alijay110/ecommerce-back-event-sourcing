package pl.cba.gibcode.apiQuery.web;

import io.swagger.annotations.*;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.cba.gibcode.apiQuery.model.KafkaMessage;

import javax.validation.Valid;
import javax.ws.rs.*;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@RestController
@Api
@RequestMapping("api")
public class QueryController {

	private final Producer<String, KafkaMessage> producer;

	public QueryController(Producer<String, KafkaMessage> producer) {
		this.producer = producer;
	}

	@ApiOperation(
			value = "Create a card as a seller",
			httpMethod = "POST")
	@ApiResponses({
			@ApiResponse(code = 200, message = "Card returned"),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 500, message = "Unknown error")
	})
	@PostMapping("test")
	public ResponseEntity<Void> createCard(
			@RequestBody @Valid KafkaMessage kafkaMessage) {
		try {
			Future<RecordMetadata> send = producer
					.send(new ProducerRecord<>("dummyTopic01", RandomStringUtils.randomAlphanumeric(5), kafkaMessage));
			send.get(5, TimeUnit.SECONDS);
		} catch(InterruptedException | ExecutionException | TimeoutException e) {
			throw new ServiceUnavailableException("asdf");
		}
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

}