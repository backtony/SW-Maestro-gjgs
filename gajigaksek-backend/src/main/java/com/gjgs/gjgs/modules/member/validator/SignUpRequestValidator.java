package com.gjgs.gjgs.modules.member.validator;

import com.gjgs.gjgs.modules.category.repositories.CategoryRepository;
import com.gjgs.gjgs.modules.member.dto.login.SignUpRequest;
import com.gjgs.gjgs.modules.member.repository.interfaces.MemberRepository;
import com.gjgs.gjgs.modules.zone.repositories.interfaces.ZoneRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
public class SignUpRequestValidator implements Validator {

    private final MemberRepository memberRepository;
    private final ZoneRepository zoneRepository;
    private final CategoryRepository categoryRepository;


    @Override
    public boolean supports(Class<?> clazz) {
        return SignUpRequest.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        SignUpRequest signUpRequest = (SignUpRequest) target;


        if (memberRepository.existsByNickname(signUpRequest.getNickname())) {
            errors.rejectValue("nickname", "invalid.nickname", "닉네임이 중복되었습니다.");
        }

        if (memberRepository.existsByPhone(signUpRequest.getPhone())) {
            errors.rejectValue("phone", "invalid.phone", "휴대폰 번호가 중복되었습니다.");
        }

        if (signUpRequest.getRecommendNickname() != null && !signUpRequest.getRecommendNickname().isBlank()
                && !memberRepository.existsByNickname(signUpRequest.getRecommendNickname())){
            errors.rejectValue("recommendNickname", "invalid.recommendNickname",
                    "해당 recommendNickname을 가진 회원이 존재하지 않습니다.");
        }

        if (memberRepository.existsByUsername(String.valueOf(signUpRequest.getId()))) {
            errors.rejectValue("Id","invalid.Id","이미 해당 카카오 Id로 가입된 회원이 존재합니다.");
        }

        if(signUpRequest.getZoneId() != null && !zoneRepository.existsById(signUpRequest.getZoneId())){
            errors.rejectValue("zoneId","invalid.zoneId","해당 zoneId는 존재하지 않는 zoneId 입니다.");
        }

        if(!signUpRequest.getCategoryIdList().isEmpty()
                && categoryRepository.countCategoryByIdList(signUpRequest.getCategoryIdList()) != signUpRequest.getCategoryIdList().size()){
            errors.rejectValue("categoryIdList","invalid.categoryIdList","categoryIdList 중 존재하지 않는 categoryId가 있습니다.");
        }


    }
}
