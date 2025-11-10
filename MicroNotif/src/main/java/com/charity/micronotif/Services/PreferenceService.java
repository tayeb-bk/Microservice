package com.charity.micronotif.Services;

import com.charity.micronotif.Entity.NotificationPreference;
import com.charity.micronotif.Repository.PreferenceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PreferenceService {
    private final PreferenceRepository repository;

    public NotificationPreference save(NotificationPreference preference) {
        return repository.save(preference);
    }

    public List<NotificationPreference> findAll() {
        return repository.findAll();
    }

    public Optional<NotificationPreference> findById(Long id) {
        return repository.findById(id);
    }

    public List<NotificationPreference> findByUserId(Long userId) {
        return repository.findByUserId(userId);
    }

    public Optional<NotificationPreference> findByUserIdAndChannel(Long userId, NotificationPreference.Channel channel) {
        return repository.findByUserIdAndChannel(userId, channel);
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}