package com.gjgs.gjgs.modules.lecture.embedded;

import com.gjgs.gjgs.modules.lecture.dtos.create.CreateLecture;
import lombok.*;

import javax.persistence.Embeddable;

@Embeddable
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor
@Getter
@Builder
@EqualsAndHashCode(of = {"priceOne", "priceTwo", "priceThree", "priceFour", "regularPrice"})
public class Price {

    private int priceOne;

    private int priceTwo;

    private int priceThree;

    private int priceFour;

    private int regularPrice;

    public static Price of(CreateLecture.PriceDto price) {
        return build(price);
    }

    private static Price build(CreateLecture.PriceDto price) {
        return Price.builder()
                .regularPrice(price.getRegularPrice())
                .priceOne(price.getPriceOne())
                .priceTwo(price.getPriceTwo())
                .priceThree(price.getPriceThree())
                .priceFour(price.getPriceFour())
                .build();
    }

    public int getPricePerMember(int memberSize) {
        switch (memberSize) {
            case 1:
                return priceOne;
            case 2:
                return priceTwo;
            case 3:
                return priceThree;
            default:
                return priceFour;
        }
    }
}
