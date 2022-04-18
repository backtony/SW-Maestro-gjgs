package com.gjgs.gjgs.modules.member.validator;


import com.gjgs.gjgs.modules.zone.repositories.interfaces.ZoneRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Component
@RequiredArgsConstructor
public class ZoneModifyRequestValidator implements ConstraintValidator<CheckIsExistZone, Long> {

    private final ZoneRepository zoneRepository;


    @Override
    public boolean isValid(Long zoneId, ConstraintValidatorContext context) {
       if(!zoneRepository.existsById(zoneId)){
           return false;
        }
        return true;
    }
}

