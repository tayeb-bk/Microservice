package com.charity.micronotif.Services;

import com.charity.micronotif.Entity.NotificationTemplate;
import com.charity.micronotif.Repository.TemplateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TemplateService {
    private final TemplateRepository repository;

    public NotificationTemplate save(NotificationTemplate template) {
        return repository.save(template);
    }

    public List<NotificationTemplate> findAll() {
        return repository.findAll();
    }

    public Optional<NotificationTemplate> findById(Long id) {
        return repository.findById(id);
    }

    public Optional<NotificationTemplate> findByName(String name) {
        return repository.findByName(name);
    }

    public List<NotificationTemplate> findByChannel(NotificationTemplate.Channel channel) {
        return repository.findByChannel(channel);
    }

    public List<NotificationTemplate> findActiveTemplates() {
        return repository.findByActiveTrue();
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}