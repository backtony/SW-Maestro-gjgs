package com.gjgs.gjgs.modules.lecture.embedded;

import com.gjgs.gjgs.modules.lecture.dtos.create.CreateLecture;
import lombok.*;

import javax.persistence.Embeddable;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Builder
@EqualsAndHashCode(of = {"termsOne", "termsTwo", "termsThree", "termsFour"})
public class Terms {

    private boolean termsOne;
    private boolean termsTwo;
    private boolean termsThree;
    private boolean termsFour;

    public static Terms of(CreateLecture.TermsRequest termsRequest) {
        return build(termsRequest);
    }

    private static Terms build(CreateLecture.TermsRequest termsRequest) {
        return Terms.builder()
                .termsOne(termsRequest.isTermsOne())
                .termsTwo(termsRequest.isTermsTwo())
                .termsThree(termsRequest.isTermsThree())
                .termsFour(termsRequest.isTermsFour())
                .build();
    }

    public static Terms of() {
        return Terms.builder()
                .termsOne(true).termsTwo(true).termsThree(true).termsFour(true)
                .build();
    }
}
