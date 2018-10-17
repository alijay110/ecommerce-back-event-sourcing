package pl.cba.gibcode.userComponent.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pl.cba.gibcode.apiQuery.model.KafkaMessage;
import pl.cba.gibcode.userComponent.repositories.DummyRepository;

@Service
public class ProcessingService {

	private static final Logger logger = LoggerFactory.getLogger(ProcessingService.class);
	private final DummyRepository dummyRepository;

	public ProcessingService(DummyRepository dummyRepository) {
		this.dummyRepository = dummyRepository;
	}

	public void processMessage(String key, KafkaMessage object){
		logger.info("Received {}", object.getMessage());
	}

}
