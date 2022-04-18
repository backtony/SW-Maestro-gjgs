package com.gjgs.gjgs.modules.lecture.services.admin;

import com.gjgs.gjgs.modules.exception.lecture.InvalidDecideLectureTypeException;
import com.gjgs.gjgs.modules.lecture.dtos.admin.DecideLectureType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
@Transactional
public class LectureDecideStrategyFactory {

    private final List<DecideLecture> strategies;

    public DecideLecture getStrategy(DecideLectureType type) {
        return strategies
                .stream()
                .filter(strategy -> strategy.getType().equals(type))
                .findFirst()
                .orElseThrow(() -> new InvalidDecideLectureTypeException());
    }
}
