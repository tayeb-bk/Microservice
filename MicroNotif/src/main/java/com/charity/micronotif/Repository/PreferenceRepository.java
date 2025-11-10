package com.charity.micronotif.Repository;

import com.charity.micronotif.Entity.NotificationPreference;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PreferenceRepository extends JpaRepository<NotificationPreference, Long> {
    List<NotificationPreference> findByUserId(Long userId);
    Optional<NotificationPreference> findByUserIdAndChannel(Long userId, NotificationPreference.Channel channel);
    List<NotificationPreference> findByUserIdAndEnabledTrue(Long userId);
}