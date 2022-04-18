package com.gjgs.gjgs.modules.lecture.validators.minute;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class MinuteValidator implements ConstraintValidator<CheckMinuteDivideThirty, Integer> {

    @Override
    public boolean isValid(Integer minute, ConstraintValidatorContext context) {

        final int THIRTY = 30;
        return Math.floorMod(minute, THIRTY) == 0;
    }
}
