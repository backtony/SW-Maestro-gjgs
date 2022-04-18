package com.gjgs.gjgs.modules.payment.service.discountpolicy;

import com.gjgs.gjgs.modules.exception.payment.InvalidPriceException;
import com.gjgs.gjgs.modules.lecture.entity.Schedule;
import com.gjgs.gjgs.modules.member.entity.Member;
import com.gjgs.gjgs.modules.payment.dto.PaymentRequest;
import com.gjgs.gjgs.modules.payment.entity.Order;

public interface DiscountPolicy {

    default void validApplyPrice(int lecturePrice, PaymentRequest paymentRequest) {
        if (lecturePrice != paymentRequest.getOriginalPrice()) {
            throw new InvalidPriceException();
        }
    }

    DiscountType getDiscountType();

    Long applyPayPersonal(Member member, Schedule schedule, PaymentRequest paymentRequest);

    Long applyPayTeamMember(Member member, Order order, PaymentRequest paymentRequest);

    void refund(Member exitMember, Order order, int refundPrice);
}
