package com.gjgs.gjgs.health;

import com.gjgs.gjgs.modules.member.repository.interfaces.RefreshTokenRedisRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
public class HealthController {

    private final RefreshTokenRedisRepository refreshTokenRedisRepository;

    @GetMapping("/health")
    public String healthCheck() {
        return "health ok";
    }

}
