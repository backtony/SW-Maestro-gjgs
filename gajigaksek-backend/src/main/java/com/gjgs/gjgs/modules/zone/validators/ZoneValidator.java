package com.gjgs.gjgs.modules.zone.validators;

import com.gjgs.gjgs.modules.team.dtos.CreateTeamRequest;
import com.gjgs.gjgs.modules.zone.repositories.interfaces.ZoneRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
public class ZoneValidator implements Validator {

    private final ZoneRepository zoneRepository;

    @Override
    public boolean supports(Class<?> clazz) {
        return CreateTeamRequest.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        CreateTeamRequest request = (CreateTeamRequest) target;
        if (!zoneRepository.existsById(request.getZoneId())) {
            errors.rejectValue("zoneId", "invalid.zone", "유효하지 않은 지역입니다.");
        }
    }
}
