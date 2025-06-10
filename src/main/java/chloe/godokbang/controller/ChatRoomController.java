package chloe.godokbang.controller;

import chloe.godokbang.auth.CustomUserDetails;
import chloe.godokbang.domain.ChatRoom;
import chloe.godokbang.dto.request.CreateChatRoomRequest;
import chloe.godokbang.dto.response.ChatRoomListResponse;
import chloe.godokbang.service.ChatRoomService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/chat")
public class ChatRoomController {

    private final ChatRoomService chatRoomService;

    @GetMapping("/rooms")
    public String getChatRoomsPage(@RequestParam(value = "keyword", required = false) String keyword, Model model) {
        List<ChatRoom> rooms;
        if (keyword != null && !keyword.isBlank()) {
            rooms = chatRoomService.searchChatRooms(keyword);
        } else {
            rooms = chatRoomService.getAllChatRoomsList();
        }

        List<ChatRoomListResponse> list = rooms.stream()
                .map(ChatRoomListResponse::fromEntity)
                .toList();
        model.addAttribute("rooms", list);
        model.addAttribute("keyword", keyword);
        model.addAttribute("chatSelected", true);
        return "pages/chat/rooms";
    }

    @GetMapping("/room/new")
    public String getCreateChatRoomPage(Model model) {
        model.addAttribute("createChatRoomRequest", new CreateChatRoomRequest());
        return "pages/chat/createRoom";
    }

    @PostMapping("/room/new")
    public String createChatRoom(@Valid @ModelAttribute CreateChatRoomRequest request,
                                 @AuthenticationPrincipal CustomUserDetails userDetails) {
        chatRoomService.createChatRoom(request, userDetails.getUser());
        return "redirect:/chat/rooms";
    }
}
