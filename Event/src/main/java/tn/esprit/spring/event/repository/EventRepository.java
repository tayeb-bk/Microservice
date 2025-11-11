package tn.esprit.spring.event.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.spring.event.model.Event; // ✅ corrige ici si ton Event est dans le package model

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
}
