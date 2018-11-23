package pl.cba.gibcode.apiQuery.model;

import lombok.Value;

import java.math.BigDecimal;

@Value(staticConstructor = "of")
public class BrandResponseDto {
    private String name;
    private String imgUrl;
    private Double maxDiscount;
}
