package com.gjgs.gjgs.modules.notice.repository.impl;

import com.gjgs.gjgs.modules.notice.dto.NoticeResponse;
import com.gjgs.gjgs.modules.notice.dto.QNoticeResponse;
import com.gjgs.gjgs.modules.notice.enums.NoticeType;
import com.gjgs.gjgs.modules.notice.repository.interfaces.NoticeQueryRepository;
import com.gjgs.gjgs.modules.utils.querydsl.QueryDslUtil;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

import static com.gjgs.gjgs.modules.notice.entity.QNotice.notice;
import static org.springframework.util.ObjectUtils.isEmpty;

@Repository
@RequiredArgsConstructor
public class NoticeQueryRepositoryImpl implements NoticeQueryRepository {

    private final JPAQueryFactory query;

    @Override
    public Page<NoticeResponse> findPagingNotice(Pageable pageable, NoticeType noticeType) {
        List<OrderSpecifier> ORDERS = getAllOrderSpecifiers(pageable);

        QueryResults<NoticeResponse> results = query
                .select(new QNoticeResponse(
                        notice.id.as("noticeId"),
                        notice.title,
                        notice.text,
                        notice.createdDate
                ))
                .from(notice)
                .where(notice.noticeType.eq(noticeType))
                .orderBy(ORDERS.stream().toArray(OrderSpecifier[]::new))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();


        List<NoticeResponse> content = results.getResults();
        long total = results.getTotal();

        return new PageImpl<>(content,pageable,total);
    }

    @Override
    public long deleteById(Long id) {
        return query.delete(notice)
                .where(notice.id.eq(id))
                .execute();
    }

    private List<OrderSpecifier> getAllOrderSpecifiers(Pageable pageable) {
        List<OrderSpecifier> ORDERS = new ArrayList<>();

        if (!isEmpty(pageable.getSort())) {
            for (Sort.Order order : pageable.getSort()) {
                Order direction = order.getDirection().isAscending() ? Order.ASC : Order.DESC;


                switch (order.getProperty()) {
                    case "createdDate":
                        OrderSpecifier<?> createdDate = QueryDslUtil
                                .getSortedColumn(direction, notice, "createdDate");
                        ORDERS.add(createdDate);
                        break;
                    default:
                        break;
                }
            }
        }

        return ORDERS;
    }
}
