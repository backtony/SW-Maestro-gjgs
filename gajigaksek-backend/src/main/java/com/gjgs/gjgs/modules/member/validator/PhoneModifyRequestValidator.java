package com.gjgs.gjgs.modules.member.validator;


import com.gjgs.gjgs.modules.member.repository.interfaces.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Component
@RequiredArgsConstructor
public class PhoneModifyRequestValidator implements ConstraintValidator<CheckIsDuplicatePhone, String> {

    private final MemberRepository memberRepository;

    @Override
    public boolean isValid(String phone, ConstraintValidatorContext context) {
        if (memberRepository.existsByPhone(phone)) {
            return false;
        }
        return true;
    }

}