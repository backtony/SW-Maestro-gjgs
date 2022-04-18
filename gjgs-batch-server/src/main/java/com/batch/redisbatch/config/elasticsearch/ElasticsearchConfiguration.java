package com.batch.redisbatch.config.elasticsearch;

import lombok.RequiredArgsConstructor;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.boot.autoconfigure.elasticsearch.ElasticsearchRestClientProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.RestClients;
import org.springframework.data.elasticsearch.config.AbstractElasticsearchConfiguration;
import org.springframework.data.elasticsearch.config.EnableElasticsearchAuditing;

import java.util.List;

@Configuration
@RequiredArgsConstructor
@EnableElasticsearchAuditing
public class ElasticsearchConfiguration extends AbstractElasticsearchConfiguration {

    private final ElasticsearchRestClientProperties properties;

    @Bean
    @Override
    public RestHighLevelClient elasticsearchClient() {

        List<String> uris = properties.getUris();

        ClientConfiguration clientConfig = ClientConfiguration.builder()
                .connectedTo(uris.toArray(new String[uris.size()]))
                .withBasicAuth(properties.getUsername(), properties.getPassword())
                .build();

        return RestClients.create(clientConfig).rest();
    }
}
