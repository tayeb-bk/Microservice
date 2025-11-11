package tn.esprit.spring.event.controller;

import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;
import tn.esprit.spring.event.model.Event;
import tn.esprit.spring.event.service.EventService;

import java.util.List;

@CrossOrigin(origins = {"http://localhost:4200","http://localhost:8083"}) // Autorise les requêtes depuis Angular
@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;

    // ➕ Ajouter un événement
    @PostMapping("/add")
    public Event addEvent(@RequestBody Event event) {
        return eventService.addEvent(event);
    }

    // 📋 Afficher tous les événements
    @GetMapping("/all")
    public List<Event> getAllEvents() {
        return eventService.getAllEvents();
    }

    // 🔍 Afficher un événement par ID
    @GetMapping("/{id}")
    public Event getEventById(@PathVariable Long id) {
        return eventService.getEventById(id);
    }

    // ✏️ Modifier un événement
    @PutMapping("/update/{id}")
    public Event updateEvent(@PathVariable Long id, @RequestBody Event event) {
        return eventService.updateEvent(id, event);
    }

    // ❌ Supprimer un événement
    @DeleteMapping("/delete/{id}")
    public void deleteEvent(@PathVariable Long id) {
        eventService.deleteEvent(id);
    }
}
