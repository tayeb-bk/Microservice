package com.charity.micronotif.Controller;

import com.charity.micronotif.Entity.NotificationPreference;
import com.charity.micronotif.Services.PreferenceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/preferences")
@RequiredArgsConstructor
public class PreferenceController {
    private final PreferenceService service;

    @PostMapping
    public ResponseEntity<NotificationPreference> create(@RequestBody NotificationPreference preference) {
        NotificationPreference created = service.save(preference);
        return ResponseEntity.ok(created);
    }

    @GetMapping
    public ResponseEntity<List<NotificationPreference>> getAll() {
        List<NotificationPreference> preferences = service.findAll();
        return ResponseEntity.ok(preferences);
    }

    @GetMapping("/{id}")
    public ResponseEntity<NotificationPreference> getById(@PathVariable Long id) {
        Optional<NotificationPreference> preference = service.findById(id);
        return preference.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<NotificationPreference>> getByUserId(@PathVariable Long userId) {
        List<NotificationPreference> preferences = service.findByUserId(userId);
        return ResponseEntity.ok(preferences);
    }

    @GetMapping("/user/{userId}/channel/{channel}")
    public ResponseEntity<NotificationPreference> getByUserAndChannel(
            @PathVariable Long userId,
            @PathVariable NotificationPreference.Channel channel) {
        Optional<NotificationPreference> preference = service.findByUserIdAndChannel(userId, channel);
        return preference.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.ok().build();
    }
}