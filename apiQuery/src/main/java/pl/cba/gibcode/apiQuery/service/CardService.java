package pl.cba.gibcode.apiQuery.service;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pl.cba.gibcode.apiQuery.model.*;
import pl.cba.gibcode.modelLibrary.exceptions.BusinessException;
import pl.cba.gibcode.modelLibrary.model.JoinedCardWithOrder;
import pl.cba.gibcode.modelLibrary.query.QueryCardsByBrand;

import javax.inject.Provider;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static java.util.Objects.isNull;
import static pl.cba.gibcode.apiQuery.helper.CriteriaBuilderHelper.meetsCriteria;

@Service
@RequiredArgsConstructor
public class CardService {

	private final Provider<ReadOnlyKeyValueStore<String, QueryCardsByBrand>> cardStoreProvider;
	private final Provider<ReadOnlyKeyValueStore<String, JoinedCardWithOrder>> joinedCardWithOrderProvider;

	public Page<CardResponseDto> findAllByBrand(Pageable pageable, CardCriteriaDto criteria) {
		if(isNull(criteria.getBrandName())){
			throw new BusinessException("No brand provided");
		}
		var cardsByBrand = cardStoreProvider.get().get(criteria.getBrandName());
		List<CardResponseDto> responseList = new LinkedList<>();

		if(meetsCriteria(criteria, cardsByBrand) && cardsByBrand.getIsAvailable()) {
			for(JoinedCardWithOrder joinedCardWithOrder : cardsByBrand.getCards().get()) {
				var card = joinedCardWithOrder.getCard();
				responseList.add(CardResponseDto.of(card.getCardUuid(),
						card.getPrice().doubleValue(),
						joinedCardWithOrder.getDiscount(),
						card.getCardType(),
						card.getBrandName()));
			}
		}
		return new PageImpl<>(responseList, pageable, responseList.size());
	}

	public Page<CardResponseForAdminDto> findAllByBrandForAdmin(Pageable pageable, CardCriteriaDto criteria) {
		var iterator = joinedCardWithOrderProvider.get().all();
		List<CardResponseForAdminDto> list= new ArrayList<>();
		while(iterator.hasNext()){
			var joinedCardWithOrder = iterator.next().value;
			if(meetsCriteria(criteria, joinedCardWithOrder)){
				var card = joinedCardWithOrder.getCard();
				list.add(CardResponseForAdminDto.of(card.getCardUuid(),
						card.getPrice().doubleValue(),
						joinedCardWithOrder.getDiscount(),
						card.getCardType(),
						card.getBrandName(), joinedCardWithOrder.getOrder()));
			}
		}
		return new PageImpl<>(list, pageable, list.size());
	}
}
