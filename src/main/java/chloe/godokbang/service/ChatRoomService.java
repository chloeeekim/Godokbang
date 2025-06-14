package chloe.godokbang.service;

import chloe.godokbang.domain.ChatMessage;
import chloe.godokbang.domain.ChatRoom;
import chloe.godokbang.domain.ChatRoomUser;
import chloe.godokbang.domain.User;
import chloe.godokbang.domain.enums.ChatRoomRole;
import chloe.godokbang.domain.enums.MessageType;
import chloe.godokbang.dto.request.CreateChatRoomRequest;
import chloe.godokbang.dto.response.ChatRoomListResponse;
import chloe.godokbang.dto.response.ChatRoomResponse;
import chloe.godokbang.dto.response.DiscoverListResponse;
import chloe.godokbang.repository.ChatMessageRepository;
import chloe.godokbang.repository.ChatRoomRepository;
import chloe.godokbang.repository.ChatRoomUserRepository;
import chloe.godokbang.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final UserRepository userRepository;
    private final ChatRoomUserRepository chatRoomUserRepository;
    private final ChatMessageRepository chatMessageRepository;

    public UUID createChatRoom(CreateChatRoomRequest request, User owner) {
        ChatRoom chatRoom = ChatRoom.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .userCount(1)
                .maxUser(request.getMaxUser())
                .build();
        chatRoomRepository.save(chatRoom);
        chatRoomUserRepository.save(new ChatRoomUser(chatRoom, owner, ChatRoomRole.OWNER));

        return chatRoom.getId();
    }

    public Page<DiscoverListResponse> getAllChatRooms(Pageable pageable, UUID userId) {
        Page<ChatRoom> all = chatRoomRepository.findAll(pageable);
        List<UUID> joined = chatRoomUserRepository.findChatRoomIdsByUserId(userId);

        return all.map(room -> {
            return DiscoverListResponse.fromEntity(room, joined.contains(room.getId()));
        });
    }

    public Page<DiscoverListResponse> searchChatRooms(Pageable pageable, String keyword, UUID userId) {
        Page<ChatRoom> rooms = chatRoomRepository.findByTitleContainingIgnoreCase(pageable, keyword);
        List<UUID> joined = chatRoomUserRepository.findChatRoomIdsByUserId(userId);

        return rooms.map(room -> {
            return DiscoverListResponse.fromEntity(room, joined.contains(room.getId()));
        });
    }

    public void joinRoom(UUID chatRoomId, User user) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId).get();
        // TODO 기존 join 여부 확인, maxUser 넘는지 확인
        chatRoomUserRepository.save(new ChatRoomUser(chatRoom, user, ChatRoomRole.USER));
        chatRoom.incrementUserCount();
    }

    public boolean checkAlreadyJoined(UUID chatRoomId, UUID userId) {
        return chatRoomUserRepository.existsByChatRoomIdAndUserId(chatRoomId, userId);
    }

    public List<ChatRoomListResponse> getChatRoomsOfUser(UUID userId) {
        User user = userRepository.findById(userId).get();
        return user.getChatRooms().stream()
                .map(ChatRoomUser::getChatRoom)
                .map(chatRoom -> {
                    Optional<ChatMessage> latest = chatMessageRepository.findLatestMessage(chatRoom.getId());
                    if (latest.isEmpty()) {
                        return ChatRoomListResponse.fromEntity(chatRoom, "");
                    } else {
                        String latestMsg = latest.get().getType() == MessageType.IMAGE ? "<IMAGE>" : latest.get().getContent();
                        return ChatRoomListResponse.fromEntity(chatRoom, latestMsg);
                    }
                })
                .toList();
    }

    public ChatRoomResponse getChatRoomById(UUID chatRoomId) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new IllegalArgumentException("Chatroom not found."));
        return ChatRoomResponse.fromEntity(chatRoom);
    }
}
