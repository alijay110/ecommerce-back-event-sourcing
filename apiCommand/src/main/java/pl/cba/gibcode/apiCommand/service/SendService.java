package pl.cba.gibcode.apiCommand.service;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pl.cba.gibcode.apiCommand.model.PostOrderUuids;
import pl.cba.gibcode.modelLibrary.model.ActionEnum;
import pl.cba.gibcode.modelLibrary.ordercomponent.*;

import javax.ws.rs.ServiceUnavailableException;
import java.time.Instant;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Service
@RequiredArgsConstructor
public class SendService {

	private static final Logger logger = LoggerFactory.getLogger(SendService.class);

	private final Producer<String, OrderComponentEvent> producer;

	public PostOrderUuids postOrder(String entityId, OrderComponentBody body, ActionEnum actionEnum, Long userId, OrderType orderType) {
		var orderUuid = UUID.randomUUID();
		sendMessage(orderUuid, entityId, body, actionEnum, userId, orderType);
		return new PostOrderUuids(entityId, orderUuid);
	}

	public UUID putOrder(String entityId, OrderComponentBody body, ActionEnum actionEnum, Long userId, OrderType orderType) {
		var orderUuid = UUID.randomUUID();
		sendMessage(orderUuid, entityId, body, actionEnum, userId, orderType);
		return orderUuid;
	}

	private void sendMessage(UUID orderUuid, String entityId, OrderComponentBody body, ActionEnum actionEnum, Long userId, OrderType type) {
		try {
			var message = ImmutableOrderComponentEvent.builder().body(body)
					.header(ImmutableOrderComponentHeader.builder().action(actionEnum)
							.orderUuid(orderUuid)
							.entityId(entityId)
							.userId(userId)
							.creationTimestamp(Instant.now())
							.type(type)
							.build()).build();
			Future<RecordMetadata> send = producer.send(new ProducerRecord<>("orderComponentEvent", entityId,message));
			send.get(5, TimeUnit.SECONDS);
			logger.info("Sent message {}", message);
		} catch(InterruptedException | ExecutionException | TimeoutException e) {
			throw new ServiceUnavailableException("asdf");
		}
	}
}
