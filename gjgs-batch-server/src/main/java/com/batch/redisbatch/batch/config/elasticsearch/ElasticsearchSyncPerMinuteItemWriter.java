package com.batch.redisbatch.batch.config.elasticsearch;

import com.batch.redisbatch.document.LectureDocument;
import lombok.RequiredArgsConstructor;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.batch.item.ItemWriter;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.IndexQueryBuilder;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;

import java.util.List;

import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
public class ElasticsearchSyncPerMinuteItemWriter implements ItemWriter<LectureDocument> {

    private final ElasticsearchOperations elasticsearchOperations;

    @Override
    public void write(List<? extends LectureDocument> items) throws Exception {
        List<LectureDocument> savedLectureDocument = findSavedDocuments(items);
        items.forEach(lectureDocument -> {
            savedLectureDocument.stream()
                    .filter(saved -> saved.getLectureId().equals(lectureDocument.getLectureId()))
                    .findFirst()
                    .ifPresent(saved -> lectureDocument.setDocumentId(saved.getId()));
        });

        List<IndexQuery> bulkIndexQueries = items.stream()
                .map(lectureDocument -> new IndexQueryBuilder()
                        .withId(lectureDocument.getId())
                        .withObject(lectureDocument)
                        .build())
                .collect(toList());
        elasticsearchOperations.bulkIndex(bulkIndexQueries, LectureDocument.class);
    }

    private List<LectureDocument> findSavedDocuments(List<? extends LectureDocument> items) {
        List<Long> lectureIdList = items.stream().map(LectureDocument::getLectureId).collect(toList());
        return elasticsearchOperations.search(createFindLectureDocumentQuery(lectureIdList), LectureDocument.class).stream()
                .map(SearchHit::getContent).collect(toList());
    }

    private NativeSearchQuery createFindLectureDocumentQuery(List<Long> lectureIdList) {
        return new NativeSearchQueryBuilder().withQuery(QueryBuilders.boolQuery().must(QueryBuilders.termsQuery("lectureId", lectureIdList))).build();
    }
}
