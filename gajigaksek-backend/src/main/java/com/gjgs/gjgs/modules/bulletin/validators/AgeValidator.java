package com.gjgs.gjgs.modules.bulletin.validators;

import com.gjgs.gjgs.modules.bulletin.dto.CreateBulletinRequest;
import com.gjgs.gjgs.modules.bulletin.enums.Age;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class AgeValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return CreateBulletinRequest.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        CreateBulletinRequest request = (CreateBulletinRequest) target;
        String inputAge = request.getAge();
        Age[] ageValues = Age.values();
        for (Age age : ageValues) {
            if (age.name().equals(inputAge)) {
                return ;
            }
        }

        errors.rejectValue("age", "VALIDATE", "지정된 나이대를 입력해주세요");
    }
}
