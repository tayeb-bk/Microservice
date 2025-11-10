package com.charity.micronotif.Controller;

import com.charity.micronotif.Entity.Notification;
import com.charity.micronotif.Services.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:8083", "http://localhost:4200"})
public class NotificationController {
    private final NotificationService service;

    @PostMapping
    public ResponseEntity<Notification> create(@RequestBody Notification notification) {
        try {
            Notification created = service.create(notification);
            return ResponseEntity.ok(created);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<Notification>> getAll() {
        List<Notification> notifications = service.findAll();
        return ResponseEntity.ok(notifications);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Notification> getById(@PathVariable Long id) {
        Optional<Notification> notification = service.findById(id);
        return notification.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Notification>> getByUserId(@PathVariable Long userId) {
        List<Notification> notifications = service.findByUserId(userId);
        return ResponseEntity.ok(notifications);
    }

    // ADD THIS ENDPOINT - This is what Angular is calling
    @GetMapping("/user/{userId}/count")
    public ResponseEntity<Long> getCountByUserId(@PathVariable Long userId) {
        List<Notification> notifications = service.findByUserId(userId);
        long count = notifications.size();
        return ResponseEntity.ok(count);
    }

    // ADD THIS ENDPOINT - For unread notifications
    @GetMapping(value = "/user/{userId}", params = "unread")
    public ResponseEntity<List<Notification>> getUnreadByUserId(
            @PathVariable Long userId,
            @RequestParam Boolean unread) {
        List<Notification> notifications = service.findByUserId(userId);
        if (unread) {
            List<Notification> unreadNotifications = notifications.stream()
                    .filter(n -> n.getStatus() != Notification.Status.READ)
                    .toList();
            return ResponseEntity.ok(unreadNotifications);
        }
        return ResponseEntity.ok(notifications);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Notification> update(@PathVariable Long id, @RequestBody Notification notification) {
        try {
            notification.setId(id);
            Notification updated = service.update(notification);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}/read")
    public ResponseEntity<Void> markAsRead(@PathVariable Long id) {
        service.markAsRead(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (service.exists(id)) {
            service.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/count")
    public ResponseEntity<Long> count() {
        long count = service.count();
        return ResponseEntity.ok(count);
    }
}