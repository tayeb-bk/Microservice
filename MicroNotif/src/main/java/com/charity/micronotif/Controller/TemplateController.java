package com.charity.micronotif.Controller;

import com.charity.micronotif.Entity.NotificationTemplate;
import com.charity.micronotif.Services.TemplateService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/templates")
@RequiredArgsConstructor
public class TemplateController {
    private final TemplateService service;

    @PostMapping
    public ResponseEntity<NotificationTemplate> create(@RequestBody NotificationTemplate template) {
        NotificationTemplate created = service.save(template);
        return ResponseEntity.ok(created);
    }

    @GetMapping
    public ResponseEntity<List<NotificationTemplate>> getAll() {
        List<NotificationTemplate> templates = service.findAll();
        return ResponseEntity.ok(templates);
    }

    @GetMapping("/{id}")
    public ResponseEntity<NotificationTemplate> getById(@PathVariable Long id) {
        Optional<NotificationTemplate> template = service.findById(id);
        return template.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<NotificationTemplate> getByName(@PathVariable String name) {
        Optional<NotificationTemplate> template = service.findByName(name);
        return template.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/channel/{channel}")
    public ResponseEntity<List<NotificationTemplate>> getByChannel(@PathVariable NotificationTemplate.Channel channel) {
        List<NotificationTemplate> templates = service.findByChannel(channel);
        return ResponseEntity.ok(templates);
    }

    @GetMapping("/active")
    public ResponseEntity<List<NotificationTemplate>> getActiveTemplates() {
        List<NotificationTemplate> templates = service.findActiveTemplates();
        return ResponseEntity.ok(templates);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.ok().build();
    }
}