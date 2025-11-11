package com.charity.micronotif.Services;

import com.charity.micronotif.Entity.Notification;
import com.charity.micronotif.Repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationRepository repository;

    public Notification create(Notification notification) {
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

    public List<Notification> findByStatus(String status) {
        Notification.Status statusEnum = Notification.Status.valueOf(status.toUpperCase());
        return repository.findByStatus(statusEnum);
    }

    public Notification update(Notification notification) {
        if (!repository.existsById(notification.getId())) {
            throw new RuntimeException("Notification not found with id: " + notification.getId());
        }
        return repository.save(notification);
    }

    public Notification markAsRead(Long id) {
        Notification notification = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notification not found with id: " + id));
        notification.setStatus(Notification.Status.READ);
        notification.setReadAt(LocalDateTime.now());
        return repository.save(notification);
    }

    public Notification markAsSent(Long id) {
        Notification notification = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notification not found with id: " + id));
        notification.setStatus(Notification.Status.SENT);
        notification.setSentAt(LocalDateTime.now());
        return repository.save(notification);
    }

    public void markAllAsRead(Long userId) {
        List<Notification> notifications = repository.findByRecipientId(userId);
        notifications.forEach(notification -> {
            if (notification.getStatus() != Notification.Status.READ) {
                notification.setStatus(Notification.Status.READ);
                notification.setReadAt(LocalDateTime.now());
            }
        });
        repository.saveAll(notifications);
    }

    public void deleteAllByUserId(Long userId) {
        List<Notification> notifications = repository.findByRecipientId(userId);
        repository.deleteAll(notifications);
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    public boolean exists(Long id) {
        return repository.existsById(id);
    }

    public long count() {
        return repository.count();
    }
}