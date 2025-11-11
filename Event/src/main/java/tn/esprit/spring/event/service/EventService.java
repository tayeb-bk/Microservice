package tn.esprit.spring.event.service;

import tn.esprit.spring.event.model.Event;
import java.util.List;

public interface EventService {
    Event addEvent(Event event);
    Event updateEvent(Long id, Event event);
    void deleteEvent(Long id);
    Event getEventById(Long id);
    List<Event> getAllEvents();
}
