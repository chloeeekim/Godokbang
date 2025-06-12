package chloe.godokbang.controller;

import chloe.godokbang.auth.CustomUserDetails;
import chloe.godokbang.domain.User;
import chloe.godokbang.domain.enums.MessageType;
import chloe.godokbang.dto.request.ChatMessageRequest;
import chloe.godokbang.dto.request.CreateChatRoomRequest;
import chloe.godokbang.dto.response.ChatRoomListResponse;
import chloe.godokbang.dto.response.DiscoverListResponse;
import chloe.godokbang.kafka.producer.KafkaChatProducer;
import chloe.godokbang.service.ChatRoomService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
@RequestMapping("/chat")
public class ChatRoomController {

    private final ChatRoomService chatRoomService;
    private final KafkaChatProducer kafkaChatProducer;
    private final ObjectMapper objectMapper;

    @GetMapping("/discover")
    public String getDiscoverPage(@PageableDefault(page = 0, size = 5, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
                                  @RequestParam(value = "keyword", required = false) String keyword, Model model,
                                  @AuthenticationPrincipal CustomUserDetails userDetails) {
        Page<DiscoverListResponse> rooms;
        if (keyword != null && !keyword.isBlank()) {
            rooms = chatRoomService.searchChatRooms(pageable, keyword, userDetails.getUser().getId());
        } else {
            rooms = chatRoomService.getAllChatRooms(pageable, userDetails.getUser().getId());
        }

        model.addAttribute("rooms", rooms);
        model.addAttribute("keyword", keyword);
        model.addAttribute("discoverSelected", true);
        return "pages/chat/discover";
    }

    @GetMapping("/room/new")
    public String getCreateChatRoomPage(Model model) {
        model.addAttribute("createChatRoomRequest", new CreateChatRoomRequest());
        return "pages/chat/createRoom";
    }

    @PostMapping("/room/new")
    public String createChatRoom(@Valid @ModelAttribute CreateChatRoomRequest request, BindingResult bindingResult,
                                 @AuthenticationPrincipal CustomUserDetails userDetails, Model model) throws JsonProcessingException {
        if (bindingResult.hasErrors()) {
            model.addAttribute("createChatRoomRequest", request);
            return "pages/chat/createRoom";
        }

        UUID roomId = chatRoomService.createChatRoom(request, userDetails.getUser());

        sendSystemMessage(roomId, userDetails.getUser(), MessageType.CREATE);

        return "redirect:/chat/room/" + roomId;
    }

    @PostMapping("/room/{id}/join")
    public String joinChatRoom(@PathVariable(name = "id") UUID roomId, @AuthenticationPrincipal CustomUserDetails userDetails) throws JsonProcessingException {
        if (!chatRoomService.checkAlreadyJoined(roomId, userDetails.getUser().getId())) {
            chatRoomService.joinRoom(roomId, userDetails.getUser());

            sendSystemMessage(roomId, userDetails.getUser(), MessageType.ENTER);
        }
        return "redirect:/chat/room/" + roomId;
    }

    @GetMapping("/rooms")
    public String getChatRoomListPage(Model model, @AuthenticationPrincipal CustomUserDetails userDetails) {
        List<ChatRoomListResponse> rooms = chatRoomService.getChatRoomsOfUser(userDetails.getUser().getId());
        model.addAttribute("rooms", rooms);
        model.addAttribute("chatSelected", true);
        return "pages/chat/roomList";
    }

    private void sendSystemMessage(UUID roomId, User user, MessageType type) throws JsonProcessingException {
        String msg = switch (type) {
            case CREATE -> " just created this chat room.";
            case ENTER -> " just joined the chat.";
            case LEAVE -> " just left the chat.";
            case TEXT, IMAGE -> null;
        };

        ChatMessageRequest request = ChatMessageRequest.builder()
                .roomId(roomId)
                .userEmail(user.getEmail())
                .type(type)
                .message(user.getNickname() + msg)
                .build();

        String jsonMessage = objectMapper.writeValueAsString(request);
        kafkaChatProducer.sendMessage("chat", jsonMessage);
    }
}
