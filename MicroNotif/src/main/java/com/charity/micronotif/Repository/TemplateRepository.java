package com.charity.micronotif.Repository;

import com.charity.micronotif.Entity.NotificationTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TemplateRepository extends JpaRepository<NotificationTemplate, Long> {
    Optional<NotificationTemplate> findByName(String name);
    List<NotificationTemplate> findByChannel(NotificationTemplate.Channel channel);
    List<NotificationTemplate> findByActiveTrue();
    Optional<NotificationTemplate> findByNameAndChannel(String name, NotificationTemplate.Channel channel);
}