package com.gjgs.gjgs.dummy.lecture;

import com.gjgs.gjgs.modules.lecture.document.LectureDocument;
import com.gjgs.gjgs.modules.lecture.entity.FinishedProduct;
import com.gjgs.gjgs.modules.lecture.entity.Lecture;
import com.gjgs.gjgs.modules.lecture.repositories.lecture.LectureRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.ElasticsearchStatusException;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.IndexQueryBuilder;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.util.List;

import static com.gjgs.gjgs.modules.lecture.enums.LectureStatus.ACCEPT;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;
import static org.elasticsearch.client.RequestOptions.DEFAULT;
import static org.elasticsearch.common.xcontent.XContentType.JSON;

@Slf4j
@Component
@RequiredArgsConstructor
@Profile("dev")
public class ElasticsearchLectureDummy {

    private static final String LECTURE = "lecture";

    private final ResourceLoader resourceLoader;
    private final RestHighLevelClient client;
    private final ElasticsearchOperations elasticsearchOperations;
    private final LectureRepository lectureRepository;

    public void setUpLecture() throws IOException {
        setUpIndex();
        setUpAcceptLecture();
    }

    public void setUpIndex() throws IOException {
        try {
            findAndDeleteIndex();
        } catch (ElasticsearchStatusException e) {
            log.info("================ has not index...");
        } finally {
            createIndex();
        }
    }

    private void findAndDeleteIndex() throws IOException {
        client.indices().get(new GetIndexRequest("lecture"), DEFAULT);
        client.indices().delete(new DeleteIndexRequest("lecture"), DEFAULT);
    }

    private void createIndex() throws IOException {
        Resource settingsResource = resourceLoader.getResource("classpath:elasticsearch/lecture-settings.json");
        String settings = new String(FileCopyUtils.copyToByteArray(settingsResource.getInputStream()), UTF_8);

        Resource mappingsResource = resourceLoader.getResource("classpath:elasticsearch/lecture-mappings.json");
        String mappings = new String(FileCopyUtils.copyToByteArray(mappingsResource.getInputStream()), UTF_8);

        CreateIndexRequest request = new CreateIndexRequest("lecture");
        request.settings(settings, JSON);
        request.mapping(mappings, JSON);
        client.indices().create(request, DEFAULT);
    }

    private void setUpAcceptLecture() throws IOException {
        List<Lecture> acceptLectures = lectureRepository.findAll().stream().filter(lecture -> lecture.getLectureStatus().equals(ACCEPT)).collect(toList());
        List<LectureDocument> setUpDocuments = acceptLectures.stream().map(lecture -> LectureDocument.builder()
                .lectureId(lecture.getId())
                .imageUrl(lecture.getThumbnailImageFileUrl())
                .title(lecture.getTitle())
                .description(lecture.getMainText())
                .zoneId(lecture.getZone().getId())
                .categoryId(lecture.getCategory().getId())
                .regularPrice((long)lecture.getPrice().getRegularPrice())
                .priceOne((long)lecture.getPrice().getPriceOne())
                .priceTwo((long)lecture.getPrice().getPriceTwo())
                .priceThree((long)lecture.getPrice().getPriceThree())
                .priceFour((long)lecture.getPrice().getPriceFour())
                .reviewCount(0L)
                .clickCount(0L)
                .score(0.0D)
                .finishedProductText(lecture.getFinishedProductList().stream()
                        .map(FinishedProduct::getText).collect(joining(", ")))
                .build()).collect(toList());
        List<IndexQuery> indexQueryList = setUpDocuments.stream().map(document -> new IndexQueryBuilder()
                .withId(document.getId())
                .withObject(document)
                .build())
                .collect(toList());

        elasticsearchOperations.bulkIndex(indexQueryList, IndexCoordinates.of(LECTURE));
    }
}
