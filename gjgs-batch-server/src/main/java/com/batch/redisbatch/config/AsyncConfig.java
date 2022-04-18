package com.batch.redisbatch.config;


import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
@EnableAsync // @Async 사용 알림
@Slf4j
public class AsyncConfig {

    @Bean(name = "cancel")
    public Executor matchingThreadPoolExecutor(){
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        int processors = Runtime.getRuntime().availableProcessors();
        log.info("processors count {}",processors);
        executor.setThreadNamePrefix("MatchingAsync-"); // thread 이름 설정
        executor.setCorePoolSize(processors); // 기본 스레드 수
        executor.setMaxPoolSize(processors*2); // 최대 스레드 개수
        executor.setQueueCapacity(50); // 최대 큐 수
        executor.setKeepAliveSeconds(60); // maxpoolsize로 인해 덤으로 더 돌아다니는 튜브는 60초 후에 수거해서 정리
        executor.initialize(); // 초기화후 반환
        return executor;
    }
}

// 스레드 수 = 사용 가능한 코어 수 * CPU 목표 사용량 * (1+대기 시간/서비스 시간)
//
// POOL생성 과정
//1. 기본 thread(TASK_CORE_POOL_SIZE) 수까지 순차적으로 쌓인다
//2. 기본 thread(TASK_CORE_POOL_SIZE) 크기가 넘어 설 경우 queue에 쌓인다
//3. 큐에 최대치까지 쌓이면 TASK_MAX_POOL_SIZE까지 순차적으로 한개씩 증가시킨다.
