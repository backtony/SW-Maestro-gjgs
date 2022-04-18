package com.gjgs.gjgs.modules.matching.validator;

import com.gjgs.gjgs.modules.bulletin.enums.TimeType;
import com.gjgs.gjgs.modules.category.repositories.CategoryRepository;
import com.gjgs.gjgs.modules.matching.dto.MatchingRequest;
import com.gjgs.gjgs.modules.team.enums.DayType;
import com.gjgs.gjgs.modules.zone.repositories.interfaces.ZoneRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class MatchingRequestValidator implements Validator {

    private final ZoneRepository zoneRepository;
    private final CategoryRepository categoryRepository;

    @Override
    public boolean supports(Class<?> clazz) {
        return MatchingRequest.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        MatchingRequest matchingRequest = (MatchingRequest) target;

        String timeType = matchingRequest.getTimeType();
        List<String> timeTypeNameList = Arrays.stream(TimeType.values()).map(Enum::name).collect(Collectors.toList());
        if (!timeTypeNameList.contains(timeType)) {
            errors.rejectValue("timeType", "invalid.timeType", timeType + " 은 올바른 시간이 아닙니다.");
        }


        List<String> dayTypeNameList = Arrays.stream(DayType.values()).map(Enum::name).collect(Collectors.toList());
        String[] dayTypeList = matchingRequest.getDayType().split("\\|");
        for (String dayType : dayTypeList) {
            if (!dayTypeNameList.contains(dayType)) {
                errors.rejectValue("dayType", "invalid.dayType", dayType + " 은 올바른 요일이 아닙니다.");
            }
        }

        if(!zoneRepository.existsById(matchingRequest.getZoneId())){
            errors.rejectValue("zoneId","invalid.zoneId","존재하지 않는 zoneId 입니다.");
        }

        if(!categoryRepository.existsById(matchingRequest.getCategoryId())){
            errors.rejectValue("categoryId","invalid.categoryId","존재하지 않는 categoryId 입니다.");
        }


    }
}
