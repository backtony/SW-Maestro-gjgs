package com.batch.redisbatch.config.elasticsearch;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Getter @Setter
@ConfigurationProperties("spring.elasticsearch.rest")
public class ElasticsearchProperty {
    private String username;
    private String password;
    private String host;
    private String port;
}
