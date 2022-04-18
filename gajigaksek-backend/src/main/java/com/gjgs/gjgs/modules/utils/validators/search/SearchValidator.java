package com.gjgs.gjgs.modules.utils.validators.search;

import org.springframework.util.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class SearchValidator implements ConstraintValidator<CheckKeyword, SearchKeyword> {

    @Override
    public boolean isValid(SearchKeyword value, ConstraintValidatorContext context) {
        String keyword = value.getKeyword();

        if(keyword == null) {
            keyword = "";
            return true;
        } else if (keyword.isBlank()) {
            return false;
        } else {
            keyword = StringUtils.trimWhitespace(keyword);
            return true;
        }
    }
}
