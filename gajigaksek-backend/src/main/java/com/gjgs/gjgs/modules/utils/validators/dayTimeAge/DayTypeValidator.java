package com.gjgs.gjgs.modules.utils.validators.dayTimeAge;

import com.gjgs.gjgs.modules.team.enums.DayType;
import com.gjgs.gjgs.modules.utils.base.CreateRequest;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import java.util.Arrays;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Component
public class DayTypeValidator implements TypeValidator {

    @Override
    public void typeValidate(CreateRequest request, Errors errors) {
        List<String> dayTypeNames = Arrays.stream(DayType.values())
                .map(Enum::name).collect(toList());

        String dayType = request.getDayType();
        if (dayType != null && !dayType.equals("")) {
            String[] inputDays = dayType.split("\\|");
            for (String inputday : inputDays) {
                if (!dayTypeNames.contains(inputday)) {
                    errors.rejectValue("dayType",
                            "TEAM-400",
                            "요일 형식에 맞지 않습니다.");
                    break;
                }
            }
        }
    }
}
