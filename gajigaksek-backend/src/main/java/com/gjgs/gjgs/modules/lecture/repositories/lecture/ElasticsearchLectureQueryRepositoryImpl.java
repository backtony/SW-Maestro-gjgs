package com.gjgs.gjgs.modules.lecture.repositories.lecture;

import com.gjgs.gjgs.modules.lecture.document.LectureDocument;
import com.gjgs.gjgs.modules.lecture.dtos.search.LectureSearchCondition;
import com.gjgs.gjgs.modules.lecture.dtos.search.LectureSearchResponse;
import com.gjgs.gjgs.modules.utils.elasticsearch.DynamicLectureSearchQueryBuilder;
import com.gjgs.gjgs.modules.utils.elasticsearch.ResponseMapper;
import lombok.RequiredArgsConstructor;
import org.elasticsearch.search.sort.SortBuilders;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Repository;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Repository
@RequiredArgsConstructor
public class ElasticsearchLectureQueryRepositoryImpl implements ElasticsearchLectureQueryRepository {

    private final ElasticsearchOperations elasticsearchOperations;
    private final ResponseMapper<LectureDocument, LectureSearchResponse> responseMapper;

    @Override
    public Page<LectureSearchResponse> getLectures(Pageable pageable, LectureSearchCondition condition) {
        NativeSearchQuery query = new NativeSearchQueryBuilder()
                .withQuery(DynamicLectureSearchQueryBuilder.makeQuery(condition))
                .withSort(SortBuilders.scoreSort())
                .build();

        if (pageable.getPageSize() <= 4) {
            query.setMaxResults(pageable.getPageSize());
            SearchHits<LectureDocument> searchResult = getSearchResult(query);
            return new PageImpl<>(getContents(searchResult));
        } else {
            query.setPageable(pageable);
            SearchHits<LectureDocument> searchResult = getSearchResult(query);
            long totalCount = searchResult.getTotalHits();
            return new PageImpl<>(getContents(searchResult), pageable, totalCount);
        }
    }

    private List<LectureSearchResponse> getContents(SearchHits<LectureDocument> searchResult) {
        return responseMapper.toResponse(searchResult.getSearchHits().stream().map(SearchHit::getContent).collect(toList()));
    }

    private SearchHits<LectureDocument> getSearchResult(NativeSearchQuery query) {
        return elasticsearchOperations.search(query, LectureDocument.class);
    }
}
