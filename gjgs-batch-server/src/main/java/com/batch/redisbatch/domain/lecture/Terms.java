package com.batch.redisbatch.domain.lecture;

import lombok.*;

import javax.persistence.Embeddable;

@Embeddable @AllArgsConstructor @NoArgsConstructor(access = AccessLevel.PROTECTED) @Getter @Builder
@EqualsAndHashCode(of = {"termsOne", "termsTwo", "termsThree", "termsFour"})
public class Terms {

    private boolean termsOne;
    private boolean termsTwo;
    private boolean termsThree;
    private boolean termsFour;
}
