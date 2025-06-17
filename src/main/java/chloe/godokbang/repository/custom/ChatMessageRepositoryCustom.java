package chloe.godokbang.repository.custom;

import chloe.godokbang.domain.ChatMessage;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.time.LocalDateTime;
import java.util.UUID;

public interface ChatMessageRepositoryCustom {

    public Slice<ChatMessage> findChatMessageByChatRoomIdWithNoOffset(UUID roomId, LocalDateTime lastSentAt, Long lastId, Pageable pageable);
}
