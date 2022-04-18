package com.gjgs.gjgs.config.repository;

import com.gjgs.gjgs.config.EmbeddedRedisConfig;
import org.springframework.boot.test.autoconfigure.data.redis.DataRedisTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited

@DataRedisTest
@Import(EmbeddedRedisConfig.class)
@ActiveProfiles("test")
public @interface CustomDataRedisTest {
}
