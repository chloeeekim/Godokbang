package chloe.godokbang.controller.api;

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
import org.springframework.data.domain.Slice;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chat")
public class ChatMessageApiController {

    private final ObjectMapper objectMapper;
    private final KafkaChatProducer kafkaChatProducer;
    private final ChatMessageService chatMessageService;
    private final S3Uploader s3Uploader;

    @GetMapping("/{id}")
    public Slice<ChatMessageResponse> getChatMessages(
            @PathVariable(name = "id") UUID roomId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)LocalDateTime lastSentAt,
            @RequestParam(required = false) Long lastId) {
        return chatMessageService.getChatMessages(roomId, lastSentAt, lastId);
    }

    @GetMapping("/{id}/message")
    public List<ChatMessageResponse> getPreviousMessages(@PathVariable(name = "id") UUID id) {
        return chatMessageService.getChatMessagesSaved(id);
    }

    @PostMapping("/message")
    public void sendMessage(@RequestBody ChatMessageRequest request, @AuthenticationPrincipal CustomUserDetails userDetails)
            throws JsonProcessingException {
        request.setUserEmail(userDetails.getUsername());
        String jsonMessage = objectMapper.writeValueAsString(request);
        kafkaChatProducer.sendMessage(jsonMessage);
    }

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void uploadImage(@RequestPart("file") MultipartFile file, @RequestPart("request") UploadImageRequest request, @AuthenticationPrincipal CustomUserDetails userDetails)
            throws IOException {
        String imageUrl = s3Uploader.uploadImage(file, "images");
        request.setImageUrl(imageUrl);
        request.setUserEmail(userDetails.getUsername());
        String jsonMessage = objectMapper.writeValueAsString(request);
        kafkaChatProducer.sendMessage(jsonMessage);
    }
}
