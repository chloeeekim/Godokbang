package chloe.godokbang.service;

import chloe.godokbang.config.PaginationProperties;
import chloe.godokbang.dto.response.ChatMessageResponse;
import chloe.godokbang.repository.ChatMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class ChatMessageService {

    private final ChatMessageRepository chatMessageRepository;
    private final PaginationProperties paginationProperties;

    /**
     * 채팅방 내 이전 메시지 불러오기
     * @param roomId 채팅방 id
     * @param lastSentAt 마지막 데이터의 sentAt
     * @param lastId 마지막 데이터의 id
     * @return ChatMessageResponse dto 목록
     * @see ChatMessageResponse
     */
    public Slice<ChatMessageResponse> getChatMessages(UUID roomId, LocalDateTime lastSentAt, Long lastId) {
        PageRequest pageRequest = PageRequest.of(0, paginationProperties.getPageSize());
        return chatMessageRepository.findChatMessageByChatRoomIdWithNoOffset(roomId, lastSentAt, lastId, pageRequest)
                .map(ChatMessageResponse::fromEntity);
    }
}
