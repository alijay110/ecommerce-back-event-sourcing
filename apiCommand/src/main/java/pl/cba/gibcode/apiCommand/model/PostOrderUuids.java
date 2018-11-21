package pl.cba.gibcode.apiCommand.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class PostOrderUuids {
	private String entityId;
	private UUID orderUuid;
}
