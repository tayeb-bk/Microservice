package com.charity.micronotif.Services;

import com.charity.micronotif.Entity.Notification;
import com.charity.micronotif.Repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationRepository repository;

    @Transactional
    public Notification create(Notification notification) {
        notification.setCreatedAt(LocalDateTime.now());
        if (notification.getStatus() == null) {
            notification.setStatus(Notification.Status.PENDING);
        }
        return repository.save(notification);
    }

    public List<Notification> findAll() {
        return repository.findAll();
    }

    public Optional<Notification> findById(Long id) {
        return repository.findById(id);
    }

    public List<Notification> findByUserId(Long userId) {
        return repository.findByRecipientId(userId);
    }

    @Transactional
    public Notification update(Notification notification) {
        if (!repository.existsById(notification.getId())) {
            throw new RuntimeException("Notification not found with id: " + notification.getId());
        }
        return repository.save(notification);
    }

    @Transactional
    public void markAsRead(Long id) {
        repository.findById(id).ifPresent(notification -> {
            notification.setReadAt(LocalDateTime.now());
            notification.setStatus(Notification.Status.READ);
            repository.save(notification);
        });
    }

    @Transactional
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    public long count() {
        return repository.count();
    }

    public boolean exists(Long id) {
        return repository.existsById(id);
    }

    public long countByUserId(Long userId) {
        List<Notification> userNotifications = repository.findByRecipientId(userId);
        return userNotifications.size();
    }

    // Add this method for getting notifications by user ID

}