package com.charity.micronotif.Repository;

import com.charity.micronotif.Entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByRecipientId(Long recipientId);
    List<Notification> findByStatus(Notification.Status status);
    long countByRecipientId(Long recipientId);
}