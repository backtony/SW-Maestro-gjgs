package com.batch.redisbatch.batch.config.elasticsearch;


import com.batch.redisbatch.document.LectureDocument;
import com.batch.redisbatch.domain.lecture.Lecture;
import com.batch.redisbatch.util.mapper.LectureToDocumentMapper;
import lombok.RequiredArgsConstructor;
import org.hibernate.SessionFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.*;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.HibernatePagingItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;

import javax.persistence.EntityManagerFactory;
import java.time.LocalDateTime;
import java.util.HashMap;

import static com.batch.redisbatch.domain.lecture.LectureStatus.ACCEPT;
import static java.time.LocalDateTime.now;

@Configuration
@EnableBatchProcessing
@RequiredArgsConstructor
public class ElasticsearchSyncPerMinuteJobConfig {

    public static final String ELASTICSEARCH_SYNC_PER_MINUTE_JOB = "elasticsearchSyncPerMinuteJob";
    public static final String ELASTICSEARCH_SYNC_PER_MINUTE_STEP = "elasticsearchSyncPerMinuteStep";
    public static final String LECTURE_ITEM_PER_MINUTE_READER = "lectureItemPerMinuteReader";
    private static final int chunkPageSize = 50;

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final EntityManagerFactory entityManagerFactory;
    private final ElasticsearchOperations elasticsearchOperations;

    @Bean
    public Job elasticsearchSyncPerMinuteJob() throws Exception {
        return jobBuilderFactory.get(ELASTICSEARCH_SYNC_PER_MINUTE_JOB)
                .start(elasticsearchSyncPerMinuteStep())
                .incrementer(new RunIdIncrementer())
                .build();
    }

    @Bean
    @JobScope
    public Step elasticsearchSyncPerMinuteStep() throws Exception {
        return stepBuilderFactory.get(ELASTICSEARCH_SYNC_PER_MINUTE_STEP)
                .<Lecture, LectureDocument>chunk(chunkPageSize)
                .reader(LectureDatabaseReader())
                .processor(toDocumentProcessor())
                .writer(indexWriter())
                .build();
    }

    @Bean
    @StepScope
    public HibernatePagingItemReader<Lecture> LectureDatabaseReader() throws Exception {
        HibernatePagingItemReader<Lecture> reader = new HibernatePagingItemReader<>();

        LocalDateTime now = now();
        LocalDateTime previous70Second = now.minusSeconds(70);

        HashMap<String, Object> params = new HashMap<>();
        params.put("now", now);
        params.put("previous70Second", previous70Second);
        params.put("accept", ACCEPT);

        reader.setQueryString("select l from Lecture l " +
                "join fetch l.zone z " +
                "join fetch l.category c " +
                "left join fetch l.reviewList r " +
                "left join fetch l.finishedProductList f " +
                "where l.lectureStatus = :accept " +
                "and l.lastModifiedDate > :previous70Second " +
                "and l.lastModifiedDate < :now ");
        reader.setPageSize(chunkPageSize);
        reader.setName(LECTURE_ITEM_PER_MINUTE_READER);
        reader.setParameterValues(params);
        SessionFactory sessionFactory = entityManagerFactory.unwrap(SessionFactory.class);
        reader.setSessionFactory(sessionFactory);
        return reader;
    }

    @Bean
    @StepScope
    public ItemProcessor<Lecture, LectureDocument> toDocumentProcessor() throws Exception {
        return LectureToDocumentMapper::toDocument;
    }

    @Bean
    @StepScope
    public ElasticsearchSyncPerMinuteItemWriter indexWriter() {
        return new ElasticsearchSyncPerMinuteItemWriter(elasticsearchOperations);
    }
}
