package com.gjgs.gjgs.modules.lecture.validators.yyyyMMdd;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class YyyyMMddValidator implements ConstraintValidator<CheckYyyyMMdd, String> {

    @Override
    public boolean isValid(String yyyyMMdd, ConstraintValidatorContext context) {
        try {
            LocalDate.parse(yyyyMMdd, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        } catch(DateTimeParseException e) {
            return false;
        }
        return true;
    }
}
