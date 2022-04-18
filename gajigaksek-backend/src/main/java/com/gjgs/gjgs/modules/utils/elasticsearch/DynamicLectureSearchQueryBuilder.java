package com.gjgs.gjgs.modules.utils.elasticsearch;

import com.gjgs.gjgs.modules.lecture.dtos.search.LectureSearchCondition;
import com.gjgs.gjgs.modules.lecture.dtos.search.SearchPriceCondition;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;

public class DynamicLectureSearchQueryBuilder {

    public static BoolQueryBuilder makeQuery(LectureSearchCondition condition) {
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        keywordBuilder(boolQuery, condition);
        priceBuilder(boolQuery, condition);
        zoneAndCategoryBuilder(boolQuery, condition);
        return boolQuery;
    }

    private static void keywordBuilder(BoolQueryBuilder boolQuery, LectureSearchCondition condition) {
        if (condition.getKeyword() != null) {
            boolQuery.must(QueryBuilders.multiMatchQuery(condition.getKeyword(), "title", "description", "finishedProductText"));
        }
    }

    private static void priceBuilder(BoolQueryBuilder boolQuery, LectureSearchCondition condition) {
        if (condition.getSearchPriceCondition() != null) {
            SearchPriceCondition priceCondition = condition.getSearchPriceCondition();
            int low = priceCondition.getLow();
            int high = priceCondition.getHigh();

            RangeQueryBuilder rangeQueryBuilder = QueryBuilders.rangeQuery("regularPrice");
            rangeQueryBuilder.gte(low);
            if (high != 0) {
                rangeQueryBuilder.lte(high);
            }

            boolQuery.must(rangeQueryBuilder);
        }
    }

    private static void zoneAndCategoryBuilder(BoolQueryBuilder boolQuery, LectureSearchCondition condition) {
        if (condition.getCategoryIdList() != null
                && !condition.getCategoryIdList().isEmpty()) {
            boolQuery.must(QueryBuilders.termsQuery("categoryId", condition.getCategoryIdList()));
        }

        if (condition.getZoneId() != null) {
            boolQuery.must(QueryBuilders.matchQuery("zoneId", condition.getZoneId()));
        }
    }
}
