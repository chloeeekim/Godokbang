package chloe.godokbang.service;

import chloe.godokbang.domain.ChatRoom;
import chloe.godokbang.domain.ChatRoomUser;
import chloe.godokbang.domain.User;
import chloe.godokbang.domain.enums.ChatRoomRole;
import chloe.godokbang.dto.request.CreateChatRoomRequest;
import chloe.godokbang.dto.response.DiscoverListResponse;
import chloe.godokbang.repository.ChatRoomRepository;
import chloe.godokbang.repository.ChatRoomUserRepository;
import chloe.godokbang.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final UserRepository userRepository;
    private final ChatRoomUserRepository chatRoomUserRepository;

    public ChatRoom createChatRoom(CreateChatRoomRequest request, User owner) {
        ChatRoom chatRoom = ChatRoom.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .userCount(1)
                .maxUser(request.getMaxUser())
                .build();
        chatRoomRepository.save(chatRoom);
        chatRoomUserRepository.save(new ChatRoomUser(chatRoom, owner, ChatRoomRole.OWNER));

        return chatRoom;
    }

    public List<DiscoverListResponse> getAllChatRoomsList() {
        return chatRoomRepository.findAll().stream()
                .map(DiscoverListResponse::fromEntity)
                .toList();
    }

    public List<DiscoverListResponse> searchChatRooms(String keyword) {
        return chatRoomRepository.findByTitleContainingIgnoreCase(keyword).stream()
                .map(DiscoverListResponse::fromEntity)
                .toList();
    }

    public void joinRoom() {

    }
}
