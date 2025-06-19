package chloe.godokbang.repository.custom.impl;

import chloe.godokbang.domain.ChatRoom;
import chloe.godokbang.domain.enums.SortType;
import chloe.godokbang.repository.custom.ChatRoomRepositoryCustom;
import com.querydsl.core.types.OrderSpecifier;
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

import static chloe.godokbang.domain.QChatRoom.chatRoom;

@Repository
@RequiredArgsConstructor
public class ChatRoomRepositoryImpl implements ChatRoomRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Slice<ChatRoom> findChatRoomsTitleContainingWithNoOffset(String keyword, LocalDateTime lastAt, UUID lastId, Pageable pageable, SortType sortType) {
        List<ChatRoom> result = queryFactory
                .selectFrom(chatRoom)
                .where(
                        containsIgnoreCase(keyword),
                        createCondition(lastAt, lastId, sortType)
                )
                .orderBy(getOrderSpecifiers(sortType))
                .limit(pageable.getPageSize() + 1)
                .fetch();

        boolean hasNext = result.size() > pageable.getPageSize();
        if (hasNext) {
            result.remove(pageable.getPageSize());
        }

        return new SliceImpl<>(result, pageable, hasNext);
    }

    private BooleanExpression containsIgnoreCase(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return null;
        }
        return chatRoom.title.containsIgnoreCase(keyword);
    }

    private BooleanExpression createCondition(LocalDateTime lastAt, UUID lastId, SortType sortType) {
        if (lastAt == null || lastId == null) {
            return null;
        }

        switch (sortType) {
            case LATEST_MESSAGE -> {
                return chatRoom.latestMsgAt.lt(lastAt)
                        .or(chatRoom.latestMsgAt.eq(lastAt).and(chatRoom.id.lt(lastId)));
            }
            case LATEST -> {
                return chatRoom.createdAt.lt(lastAt)
                        .or(chatRoom.createdAt.eq(lastAt).and(chatRoom.id.lt(lastId)));
            }
            case OLDEST -> {
                return chatRoom.createdAt.gt(lastAt)
                        .or(chatRoom.createdAt.eq(lastAt).and(chatRoom.id.gt(lastId)));
            }
            default -> {
                return null;
            }
        }
    }

    private OrderSpecifier<?>[] getOrderSpecifiers(SortType sortType) {
        switch (sortType) {
            case LATEST_MESSAGE -> {
                return new OrderSpecifier[]{
                        chatRoom.latestMsgAt.desc(),
                        chatRoom.id.desc()
                };
            }
            case LATEST -> {
                return new OrderSpecifier[]{
                        chatRoom.createdAt.desc(),
                        chatRoom.id.desc()
                };
            }
            case OLDEST -> {
                return new OrderSpecifier[]{
                        chatRoom.createdAt.asc(),
                        chatRoom.id.desc()
                };
            }
        }
        return new OrderSpecifier[]{chatRoom.id.desc()};
    }
}
