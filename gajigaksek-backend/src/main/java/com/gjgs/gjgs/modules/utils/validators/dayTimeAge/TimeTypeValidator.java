package com.gjgs.gjgs.modules.utils.validators.dayTimeAge;

import com.gjgs.gjgs.modules.team.enums.TimeType;
import com.gjgs.gjgs.modules.utils.base.CreateRequest;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import java.util.Arrays;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Component
public class TimeTypeValidator implements TypeValidator {

    @Override
    public void typeValidate(CreateRequest request, Errors errors) {
        List<String> timeTypeNames = Arrays.stream(TimeType.values())
                .map(Enum::name).collect(toList());

        String timeType = request.getTimeType();
        if (timeType != null && !timeType.equals("")) {
            String[] inputTimes = timeType.split("\\|");
            for (String inputTime : inputTimes) {
                if (!timeTypeNames.contains(inputTime)) {
                    errors.rejectValue("timeType",
                            "TEAM-400",
                            "시간 형식에 맞지 않습니다.");
                    break;
                }
            }
        }
    }
}
