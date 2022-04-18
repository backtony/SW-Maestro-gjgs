package com.gjgs.gjgs.modules.member.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = CategoryModifyRequestValidator.class)
@Target({ElementType.PARAMETER, ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface CheckIsExistCategory {
    String message() default "존재하지 않는 categoryId 입니다.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
