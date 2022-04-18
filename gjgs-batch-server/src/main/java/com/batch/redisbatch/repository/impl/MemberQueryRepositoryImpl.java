package com.batch.redisbatch.repository.impl;

import com.batch.redisbatch.dto.MemberFcmDto;
import com.batch.redisbatch.dto.QMemberFcmDto;
import com.batch.redisbatch.repository.interfaces.MemberQueryRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static com.batch.redisbatch.domain.QMember.member;

@Repository
@RequiredArgsConstructor
public class MemberQueryRepositoryImpl implements MemberQueryRepository {

    private final JPAQueryFactory query;

    @Override
    public Optional<MemberFcmDto> findOnlyFcmById(Long Id) {
        return Optional.ofNullable(
                query
                    .select(new QMemberFcmDto(
                            member.id,
                            member.fcmToken
                    ))
                    .from(member)
                    .where(member.id.eq(Id))
                    .fetchOne()
        );
    }
}
