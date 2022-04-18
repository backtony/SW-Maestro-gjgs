package com.batch.redisbatch.batch.config.elasticsearch;

import com.batch.redisbatch.TestBatchConfig;
import com.batch.redisbatch.domain.lecture.FinishedProduct;
import com.batch.redisbatch.domain.lecture.Lecture;
import com.batch.redisbatch.domain.lecture.Price;
import com.batch.redisbatch.domain.lecture.Terms;
import com.batch.redisbatch.repository.interfaces.FinishedProductRepository;
import com.batch.redisbatch.repository.interfaces.LectureRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityManager;
import java.util.List;

import static com.batch.redisbatch.domain.lecture.LectureStatus.ACCEPT;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.batch.core.ExitStatus.COMPLETED;

@SpringBootTest(classes = {
        TestBatchConfig.class,
        ElasticsearchSyncPerMinuteJobConfig.class,
        RunIdIncrementer.class
})
@SpringBatchTest
class ElasticsearchSyncPerMinuteJobConfigTest {

    @Autowired JobLauncherTestUtils jobLauncherTestUtils;
    @Autowired LectureRepository lectureRepository;
    @Autowired FinishedProductRepository finishedProductRepository;
    @Autowired EntityManager entityManager;

    @Test
    @DisplayName("1분 마다 엘라스틱서치와 동기화 테스트")
    void elasticsearchSyncPerMinuteTest() throws Exception {

        // given
        for(int i = 0; i < 51; i++) {
            Lecture lecture = lectureRepository.save(createTestLecture());
            finishedProductRepository.saveAll(createTestFinishedProduct(lecture));
        }

        // when
        JobExecution jobExecution = jobLauncherTestUtils.launchJob();

        // then
        assertEquals(COMPLETED, jobExecution.getExitStatus());
    }

    private List<FinishedProduct> createTestFinishedProduct(Lecture lecture) {
        FinishedProduct finishedProduct1 = FinishedProduct.builder()
                .lecture(lecture).orders(1).text("test")
                .build();
        FinishedProduct finishedProduct2 = FinishedProduct.builder()
                .lecture(lecture).orders(2).text("test")
                .build();
        FinishedProduct finishedProduct3 = FinishedProduct.builder()
                .lecture(lecture).orders(3).text("test")
                .build();

        return List.of(finishedProduct1, finishedProduct2, finishedProduct3);
    }

    private Lecture createTestLecture() {
        return Lecture.builder()
                .thumbnailImageFileUrl("test").title("test").price(Price.builder()
                        .regularPrice(1000).priceOne(1000).priceTwo(1000).priceThree(1000).priceFour(1000).build())
                .terms(Terms.builder().termsOne(true).termsTwo(true).termsThree(true).termsFour(true).build())
                .lectureStatus(ACCEPT)
                .fullAddress("test").build();
    }
}