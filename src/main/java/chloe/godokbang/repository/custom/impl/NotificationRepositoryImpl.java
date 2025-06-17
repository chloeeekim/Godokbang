package chloe.godokbang.repository.custom.impl;

import chloe.godokbang.domain.Notification;
import chloe.godokbang.repository.custom.NotificationRepositoryCustom;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static chloe.godokbang.domain.QNotification.notification;

@Repository
@RequiredArgsConstructor
public class NotificationRepositoryImpl implements NotificationRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Slice<Notification> findNotificationsByUserWithNoOffset(UUID userId, LocalDateTime lastCreatedAt, Long lastId, Pageable pageable) {
        List<Notification> result = queryFactory
                .selectFrom(notification)
                .where(
                        notification.receiver.id.eq(userId),
                        ltCreatedAtAndId(lastCreatedAt, lastId)
                )
                .orderBy(notification.createdAt.desc(), notification.id.desc())
                .limit(pageable.getPageSize() + 1)
                .fetch();

        boolean hasNext = result.size() > pageable.getPageSize();
        if (hasNext) {
            result.remove(pageable.getPageSize());
        }

        return new SliceImpl<>(result, pageable, hasNext);
    }

    private BooleanExpression ltCreatedAtAndId(LocalDateTime lastCreatedAt, Long lastId) {
        if (lastCreatedAt == null || lastId == null) {
            return null;
        }
        return notification.createdAt.lt(lastCreatedAt)
                .or(notification.createdAt.eq(lastCreatedAt).and(notification.id.lt(lastId)));
    }
}
