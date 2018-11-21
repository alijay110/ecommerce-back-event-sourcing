package pl.cba.gibcode.orderComponent.query;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Serialized;
import org.springframework.context.annotation.Configuration;
import pl.cba.gibcode.modelLibrary.brand.Brand;
import pl.cba.gibcode.modelLibrary.brand.BrandList;
import pl.cba.gibcode.modelLibrary.brand.ModifiableBrandList;
import pl.cba.gibcode.modelLibrary.config.CustomJsonSerde;
import pl.cba.gibcode.modelLibrary.model.CategoryEnum;

import java.util.ArrayList;
import java.util.List;

@Configuration
@RequiredArgsConstructor
public class QueryKafkaConfig {

	//TODO delete from each category changed
	public void brandByCategories(KStream<String, Brand> brandStream) {
		/*brandStream
				.filterNot((k, v) -> v.getDeleted())
				.flatMap((key, values) -> {
					List<KeyValue<CategoryEnum, BrandWrapper>> arrayList = new ArrayList<>();
					var brandWrapper = new BrandWrapper();
					for(CategoryEnum value : CategoryEnum.values()) {
						if(values.getCategories().contains(value)){
							brandWrapper.addedBrand = values;
						}else{
							brandWrapper.deletedBrand = values;
						}
						arrayList.add(KeyValue.pair(value, brandWrapper));
					}

					//values.getCategories().forEach(v ->
					//		arrayList.add(KeyValue.pair(v, values)));
					return arrayList; })
				.groupByKey(Serialized.with(new CustomJsonSerde<>(CategoryEnum.class), new CustomJsonSerde<>(BrandWrapper.class)))
				.aggregate(
						ModifiableBrandList::create,
						(k, v, list) -> {
							list.getBrands().stream().anyMatch(brand -> brand.getName().equals(v.deletedBrand.getName()))
							if(list.getBrands().stream().anyMatch(brand -> brand.getName().equals(v.deletedBrand.getName()))){
								list.getBrands().remove(v);
							}

							if(list.getBrands().stream().noneMatch(brand -> brand.getName().equals(v.getName()))) {
								list.getBrands().add(v);
							}
							return list;
						},
						CustomJsonSerde.materialize(CategoryEnum.class, BrandList.class))
				.toStream()
				.to("brandsByCategories", CustomJsonSerde.produce(CategoryEnum.class, BrandList.class));*/
	}

	static class BrandWrapper{
		private Brand addedBrand;
		private Brand deletedBrand;

		public Brand getAddedBrand() {
			return addedBrand;
		}

		public void setAddedBrand(Brand addedBrand) {
			this.addedBrand = addedBrand;
		}

		public Brand getDeletedBrand() {
			return deletedBrand;
		}

		public void setDeletedBrand(Brand deletedBrand) {
			this.deletedBrand = deletedBrand;
		}
	}
}
