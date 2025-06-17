package chloe.godokbang.repository.custom.impl;

import chloe.godokbang.domain.ChatMessage;
import chloe.godokbang.repository.custom.ChatMessageRepositoryCustom;
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

import static chloe.godokbang.domain.QChatMessage.chatMessage;

@Repository
@RequiredArgsConstructor
public class ChatMessageRepositoryImpl implements ChatMessageRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Slice<ChatMessage> findChatMessageByChatRoomIdWithNoOffset(UUID roomId, LocalDateTime lastSentAt, Long lastId, Pageable pageable) {
        List<ChatMessage> result = queryFactory
                .selectFrom(chatMessage)
                .where(
                        chatMessage.chatRoom.id.eq(roomId),
                        ltSentAtAndId(lastSentAt, lastId)
                )
                .orderBy(chatMessage.sentAt.desc(), chatMessage.id.desc())
                .limit(pageable.getPageSize() + 1)
                .fetch();

        boolean hasNext = result.size() > pageable.getPageSize();
        if (hasNext) {
            result.remove(pageable.getPageSize());
        }

        return new SliceImpl<>(result, pageable, hasNext);
    }

    private BooleanExpression ltSentAtAndId(LocalDateTime lastSentAt, Long lastId) {
        if (lastSentAt == null || lastId == null) {
            return null;
        }
        return chatMessage.sentAt.lt(lastSentAt)
                .or(chatMessage.sentAt.eq(lastSentAt).and(chatMessage.id.lt(lastId)));
    }
}
