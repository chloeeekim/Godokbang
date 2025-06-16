package chloe.godokbang.controller.api;

import chloe.godokbang.auth.CustomUserDetails;
import chloe.godokbang.dto.response.NotificationResponse;
import chloe.godokbang.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notifications")
public class NotificationApiController {

    private final NotificationService notificationService;

    @GetMapping
    public Slice<NotificationResponse> getNotifications(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)LocalDateTime lastCreatedAt,
            @RequestParam(required = false) Long lastId,
            @RequestParam(defaultValue = "10") int size,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        return notificationService.getUserNotifications(userDetails.getUser().getId(), lastCreatedAt, lastId, size);
    }

    @PatchMapping("/{id}/read")
    public void markAsRead(@PathVariable("id") Long id) {
        notificationService.markAsRead(id);
    }

    @PatchMapping("/{id}/unread")
    public void markAsUnread(@PathVariable("id") Long id) {
        notificationService.markAsUnread(id);
    }

    @DeleteMapping("/{id}")
    public void deleteNotification(@PathVariable("id") Long id) {
        notificationService.deleteNotification(id);
    }
}
