package tn.esprit.spring.event.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.spring.event.model.Event;
import tn.esprit.spring.event.repository.EventRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;

    @Override
    public Event addEvent(Event event) {
        return eventRepository.save(event);
    }

    @Override
    public Event updateEvent(Long id, Event event) {
        // Vérifie si l'événement existe avant de le modifier
        Event existingEvent = eventRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Event not found with id " + id));

        existingEvent.setTitle(event.getTitle());
        existingEvent.setDescription(event.getDescription());
        existingEvent.setLocation(event.getLocation());
        existingEvent.setDateEvent(event.getDateEvent());
        existingEvent.setGoalAmount(event.getGoalAmount());
        existingEvent.setCollectedAmount(event.getCollectedAmount());
        existingEvent.setType(event.getType());

        return eventRepository.save(existingEvent);
    }

    @Override
    public void deleteEvent(Long id) {
        if (!eventRepository.existsById(id)) {
            throw new RuntimeException("Cannot delete — Event not found with id " + id);
        }
        eventRepository.deleteById(id);
    }

    @Override
    public Event getEventById(Long id) {
        return eventRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Event not found with id " + id));
    }

    @Override
    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }
}
