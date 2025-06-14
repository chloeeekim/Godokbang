package chloe.godokbang.kafka.consumer;

import chloe.godokbang.domain.ChatMessage;
import chloe.godokbang.domain.ChatRoom;
import chloe.godokbang.domain.User;
import chloe.godokbang.dto.request.ChatMessageRequest;
import chloe.godokbang.dto.request.UploadImageRequest;
import chloe.godokbang.dto.response.ChatMessageResponse;
import chloe.godokbang.repository.ChatMessageRepository;
import chloe.godokbang.repository.ChatRoomRepository;
import chloe.godokbang.repository.UserRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import groovy.util.logging.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;

@lombok.extern.slf4j.Slf4j
@Component
@RequiredArgsConstructor
@Slf4j
public class KafkaChatConsumer {

    private final ObjectMapper objectMapper;
    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final UserRepository userRepository;
    private final SimpMessagingTemplate messagingTemplate;

    @KafkaListener(topics = "chat", groupId = "chat-group")
    public void consume(String jsonMessage) {
        try {
            JsonNode rootNode = objectMapper.readTree(jsonMessage);
            String type = rootNode.get("type").asText();
            ChatMessage message;
            ChatRoom chatRoom;

            if (type.equals("IMAGE")) {
                UploadImageRequest request = objectMapper.readValue(jsonMessage, UploadImageRequest.class);
                chatRoom = getChatRoom(request.getRoomId());

                message = request.toEntity(chatRoom, getSender(request.getUserEmail()));
            } else {
                ChatMessageRequest request = objectMapper.readValue(jsonMessage, ChatMessageRequest.class);
                chatRoom = getChatRoom(request.getRoomId());

                message = request.toEntity(chatRoom, getSender(request.getUserEmail()));
            }

            chatMessageRepository.save(message);
            convertAndSend(message.getChatRoom().getId(), message);

            switch (message.getType()) {
                case TEXT -> chatRoom.updateLatest(message.getContent(), message.getSentAt());
                case IMAGE -> chatRoom.updateLatest("<IMAGE>", message.getSentAt());
                case CREATE -> chatRoom.updateLatest("", message.getSentAt());
            }
            chatRoomRepository.save(chatRoom);
        } catch (Exception e) {
            log.error("Failed to consume chat message", e);
        }
    }

    private void convertAndSend(UUID roomId, ChatMessage message) {
        messagingTemplate.convertAndSend("/topic/chatroom." + roomId, ChatMessageResponse.fromEntity(message));
    }

    private ChatRoom getChatRoom(UUID roomId) {
        return chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("Chatroomm not found."));
    }

    private User getSender(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found."));
    }
}
