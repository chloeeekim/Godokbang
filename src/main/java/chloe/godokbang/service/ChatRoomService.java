package chloe.godokbang.service;

import chloe.godokbang.domain.ChatRoom;
import chloe.godokbang.domain.User;
import chloe.godokbang.dto.request.CreateChatRoomRequest;
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
                .owner(owner)
                .build();
        return chatRoomRepository.save(chatRoom);
    }

    public List<ChatRoom> getAllChatRoomsList() {
        return chatRoomRepository.findAll();
    }

    public List<ChatRoom> searchChatRooms(String keyword) {
        return chatRoomRepository.findByTitleContainingIgnoreCase(keyword);
    }

    public void joinRoom() {

    }
}
