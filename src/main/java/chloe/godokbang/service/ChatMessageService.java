package chloe.godokbang.service;

import chloe.godokbang.dto.response.ChatMessageResponse;
import chloe.godokbang.repository.ChatMessageRepository;
import chloe.godokbang.s3.S3Uploader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class ChatMessageService {

    private final ChatMessageRepository chatMessageRepository;
    private final S3Uploader s3Uploader;

    public List<ChatMessageResponse> getChatMessagesSaved(UUID roomId) {
        return chatMessageRepository.findByChatRoomIdOrderBySentAtAsc(roomId).stream()
                .map(ChatMessageResponse::fromEntity)
                .toList();
    }
}
