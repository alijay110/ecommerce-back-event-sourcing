package pl.cba.gibcode.apiCommand.service;

import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.springframework.stereotype.Service;
import pl.cba.gibcode.modelLibrary.brand.Brand;
import pl.cba.gibcode.modelLibrary.card.Card;
import pl.cba.gibcode.modelLibrary.exceptions.BusinessException;

import javax.inject.Provider;

@Service
public class ValidatorService {

	private final Provider<ReadOnlyKeyValueStore<String, Brand>> brandStoreProvider;
	private final Provider<ReadOnlyKeyValueStore<String, Card>> cardStoreProvider;

	public ValidatorService(

			Provider<ReadOnlyKeyValueStore<String, Brand>> brandStoreProvider,
			Provider<ReadOnlyKeyValueStore<String, Card>> cardStoreProvider) {
		this.brandStoreProvider = brandStoreProvider;
		this.cardStoreProvider = cardStoreProvider;
	}

	public void validateBrand(String entityId) {
		Brand brand = brandStoreProvider.get().get(entityId);
		if(brand == null) {
			throw new BusinessException(String.format("Brand not found with entityId %s", entityId));
		}
	}
	public void validateCard(String entityId) {
		Card card = cardStoreProvider.get().get(entityId);
		if(card == null) {
			throw new BusinessException(String.format("Brand not found with entityId %s", entityId));
		}
	}

	public void validateNewBrandCreation(String entityId){
		Brand brand = brandStoreProvider.get().get(entityId);
		//if(nonNull(brand)) throw new BusinessException(String.format("Entity already found in Order with entityId %s", entityId));
	}

	public void validateNewCardCreation(String entityId){
		Card card = cardStoreProvider.get().get(entityId);
		//if(nonNull(card)) throw new BusinessException(String.format("Entity already found in Order with entityId %s", entityId));

	}
}
