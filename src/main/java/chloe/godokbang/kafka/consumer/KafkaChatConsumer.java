package chloe.godokbang.kafka.consumer;

import chloe.godokbang.domain.ChatMessage;
import chloe.godokbang.domain.ChatRoom;
import chloe.godokbang.domain.User;
import chloe.godokbang.dto.request.ChatMessageRequest;
import chloe.godokbang.dto.response.ChatMessageResponse;
import chloe.godokbang.repository.ChatMessageRepository;
import chloe.godokbang.repository.ChatRoomRepository;
import chloe.godokbang.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import groovy.util.logging.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

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
            ChatMessageRequest request = objectMapper.readValue(jsonMessage, ChatMessageRequest.class);

            ChatRoom chatRoom = chatRoomRepository.findById(request.getRoomId())
                    .orElseThrow(() -> new IllegalArgumentException("Chatroom not found"));

            User sender = userRepository.findByEmail(request.getUserEmail())
                    .orElseThrow(() -> new IllegalArgumentException("User not found"));

            ChatMessage message = request.toEntity(chatRoom, sender);
            chatMessageRepository.save(message);

            System.out.println("message : " + message.getMessage() + " / type : " + message.getType().toString());

            messagingTemplate.convertAndSend("/topic/chatroom." + request.getRoomId(), ChatMessageResponse.fromEntity(message));
        } catch (Exception e) {
            log.error("Failed to consume chat message", e);
        }
    }
}
