package tn.esprit.spring.event.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idEvent;

    private String title;
    private String description;
    private String location;

    private LocalDate dateEvent;

    private float goalAmount;
    private float collectedAmount;

    @Enumerated(EnumType.STRING)
    private EventType type;
}
