package com.gjgs.gjgs.modules.utils.converter;

import com.gjgs.gjgs.modules.exception.lecture.InvalidApplyTypeException;
import com.gjgs.gjgs.modules.lecture.services.apply.timepolicy.CheckTimeType;
import com.gjgs.gjgs.modules.payment.dto.PayType;
import org.springframework.stereotype.Component;

@Component
public class CheckTimeTypeConverter {

    public CheckTimeType getTimeTypeFromPayType(PayType payType) {
        switch (payType) {
            case TEAM:
                return CheckTimeType.TEAM;
            case PERSONAL:
                return CheckTimeType.PERSONAL;
        }
        throw new InvalidApplyTypeException();
    }
}
