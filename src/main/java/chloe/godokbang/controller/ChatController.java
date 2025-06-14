package chloe.godokbang.controller;

import chloe.godokbang.auth.CustomUserDetails;
import chloe.godokbang.dto.request.ChatMessageRequest;
import chloe.godokbang.dto.request.UploadImageRequest;
import chloe.godokbang.dto.response.ChatMessageResponse;
import chloe.godokbang.kafka.producer.KafkaChatProducer;
import chloe.godokbang.s3.S3Uploader;
import chloe.godokbang.service.ChatMessageService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
@RequestMapping("/chat")
public class ChatController {

    private final ObjectMapper objectMapper;
    private final KafkaChatProducer kafkaChatProducer;
    private final ChatMessageService chatMessageService;
    private final S3Uploader s3Uploader;

    @GetMapping("/room/{id}")
    public String getChatRoomPage(Model model, @PathVariable(name = "id") UUID id) {
        model.addAttribute("chatRoomId", id);
        return "pages/chat/chatRoom";
    }

    @GetMapping("/{id}/message")
    @ResponseBody
    public List<ChatMessageResponse> getPreviousMessages(@PathVariable(name = "id") UUID id) {
        return chatMessageService.getChatMessagesSaved(id);
    }

    @PostMapping("/message")
    @ResponseBody
    public void sendMessage(@RequestBody ChatMessageRequest request, @AuthenticationPrincipal CustomUserDetails userDetails)
            throws JsonProcessingException {
        request.setUserEmail(userDetails.getUsername());
        String jsonMessage = objectMapper.writeValueAsString(request);
        kafkaChatProducer.sendMessage("chat", jsonMessage);
    }

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseBody
    public void uploadImage(@RequestPart("file") MultipartFile file, @RequestPart("request") UploadImageRequest request, @AuthenticationPrincipal CustomUserDetails userDetails)
            throws IOException {
        String imageUrl = s3Uploader.uploadImage(file, "images");
        request.setImageUrl(imageUrl);
        request.setUserEmail(userDetails.getUsername());
        String jsonMessage = objectMapper.writeValueAsString(request);
        kafkaChatProducer.sendMessage("chat", jsonMessage);
    }
}
