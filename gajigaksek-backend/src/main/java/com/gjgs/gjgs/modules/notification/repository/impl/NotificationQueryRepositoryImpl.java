package com.gjgs.gjgs.modules.notification.repository.impl;

import com.gjgs.gjgs.modules.notification.dto.NotificationDto;
import com.gjgs.gjgs.modules.notification.dto.QNotificationDto;
import com.gjgs.gjgs.modules.notification.entity.Notification;
import com.gjgs.gjgs.modules.notification.repository.interfaces.NotificationQueryRepository;
import com.gjgs.gjgs.modules.utils.querydsl.RepositorySliceHelper;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.gjgs.gjgs.modules.member.entity.QMember.member;
import static com.gjgs.gjgs.modules.notification.entity.QNotification.notification;

@Repository
@RequiredArgsConstructor
public class NotificationQueryRepositoryImpl implements NotificationQueryRepository {

    private final JPAQueryFactory query;

    @Override
    public Optional<Notification> findNotificationByUuidAndUsername(String uuid, String username) {
        return Optional.ofNullable(
                query
                    .select(notification)
                    .from(notification)
                    .join(notification.member,member)
                    .where(notification.uuid.eq(uuid),notification.member.username.eq(username))
                    .fetchOne());
    }

    @Override
    public Slice<NotificationDto> findNotificationDtoListPaginationNoOffsetByUsername(String username, Long lastNotificationId, Pageable pageable) {

        List<NotificationDto> content = query
                .select(new QNotificationDto(
                        notification.id.as("notificationId"),
                        notification.title,
                        notification.message,
                        notification.checked,
                        notification.notificationType,
                        notification.uuid,
                        notification.TeamId,
                        notification.createdDate
                ))
                .from(notification)
                .join(notification.member, member)
                .where(ltNotificationId(lastNotificationId),
                        notification.member.username.eq(username))
                .orderBy(notification.id.desc())
                .limit(pageable.getPageSize()+1)
                .fetch();

        return RepositorySliceHelper.toSlice(content,pageable);

    }

    private BooleanExpression ltNotificationId(Long lastNotificationId) {
        if (lastNotificationId == null){
            return null;
        }
        return notification.id.lt(lastNotificationId);
    }


}
