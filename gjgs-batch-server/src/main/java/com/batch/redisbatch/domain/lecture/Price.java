package com.batch.redisbatch.domain.lecture;

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
}
