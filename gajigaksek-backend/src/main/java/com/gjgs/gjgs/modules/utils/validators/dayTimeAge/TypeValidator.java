package com.gjgs.gjgs.modules.utils.validators.dayTimeAge;

import com.gjgs.gjgs.modules.utils.base.CreateRequest;
import org.springframework.validation.Errors;

public interface TypeValidator {

    void typeValidate(CreateRequest request, Errors errors);
}
