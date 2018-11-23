package pl.cba.gibcode.apiQuery.service;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pl.cba.gibcode.apiQuery.model.BrandCriteriaDto;
import pl.cba.gibcode.apiQuery.model.BrandResponseDto;
import pl.cba.gibcode.modelLibrary.brand.Brand;
import pl.cba.gibcode.modelLibrary.query.QueryBrand;

import javax.inject.Provider;
import java.util.ArrayList;
import java.util.List;

import static pl.cba.gibcode.apiQuery.helper.CriteriaBuilderHelper.meetsCriteria;

@Service
@RequiredArgsConstructor
public class BrandService {

	private final Provider<ReadOnlyKeyValueStore<String, QueryBrand>> brandStoreProvider;


	public Page<BrandResponseDto> findAll(Pageable pageable, BrandCriteriaDto criteria) {
		var iterator =brandStoreProvider.get().all();
		List<BrandResponseDto> list= new ArrayList<>();
		while(iterator.hasNext()){
			var brand = iterator.next().value;
			if(meetsCriteria(criteria, brand) && brand.getIsAvailable()){
				var brandResponse = BrandResponseDto.of(brand.getBrand().getName(), brand.getBrand().getImageUrl(), brand.getMaxDiscount());
				list.add(brandResponse);
			}
		}
		return new PageImpl<>(list, pageable, list.size());
	}

	public Page<BrandResponseDto> findAllForAdmin(Pageable pageable, BrandCriteriaDto criteria) {
		var iterator =brandStoreProvider.get().all();
		List<BrandResponseDto> list= new ArrayList<>();
		while(iterator.hasNext()){
			var brand = iterator.next().value;
			if(meetsCriteria(criteria, brand)){
				var brandResponse = BrandResponseDto.of(brand.getBrand().getName(), brand.getBrand().getImageUrl(), brand.getMaxDiscount());
				list.add(brandResponse);
			}
		}
		return new PageImpl<>(list, pageable, list.size());	}
}
