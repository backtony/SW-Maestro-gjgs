package com.gjgs.gjgs.modules.utils.validators.dayTimeAge;


import com.gjgs.gjgs.modules.utils.base.CreateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CreateRequestTimeDayAgeValidator implements Validator {

    private final List<TypeValidator> typeValidatorList;

    @Override
    public boolean supports(Class<?> clazz) {
        return CreateRequest.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        CreateRequest request = (CreateRequest) target;
        typeValidatorList.forEach(validator -> validator.typeValidate(request, errors));
    }
}
