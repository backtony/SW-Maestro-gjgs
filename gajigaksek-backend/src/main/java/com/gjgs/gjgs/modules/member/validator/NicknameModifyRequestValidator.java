package com.gjgs.gjgs.modules.member.validator;


import com.gjgs.gjgs.modules.member.repository.interfaces.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Component
@RequiredArgsConstructor
public class NicknameModifyRequestValidator implements ConstraintValidator<CheckIsDuplicateNickname, String> {

    private final MemberRepository memberRepository;


    @Override
    public boolean isValid(String nickname, ConstraintValidatorContext context) {
        if (memberRepository.existsByNickname(nickname)) {
            return false;
        }
        return true;
    }
}
