package com.esprit.microservice.formation;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "formations")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Formation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    private String titre;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false, length = 100)
    private String formateur;

    @Column(nullable = false)
    private Integer dureeHeures;

    @Column(nullable = false)
    private Integer placesMax;

    private Integer placesRestantes;

    @Column(nullable = false)
    private LocalDateTime dateDebut;

    @Column(nullable = false)
    private LocalDateTime dateFin;

    @Enumerated(EnumType.STRING)
    @Column(length = 20, nullable = false)
    private FormationStatus status;

    @Enumerated(EnumType.STRING)
    @Column(length = 30, nullable = false)
    private FormationType type;

    @Column(length = 255)
    private String lieu;

    @Column(columnDefinition = "TEXT")
    private String prerequis;

    @Column(length = 255)
    private String publicCible;

    @ElementCollection
    @CollectionTable(name = "formation_competences",
            joinColumns = @JoinColumn(name = "formation_id"))
    @Column(name = "competence")
    private List<String> competencesAcquises;

    @Column(nullable = false)
    private Long associationId;

    @Column(nullable = false)
    private LocalDateTime dateCreation;

    @Column(nullable = false)
    private LocalDateTime dateModification;

    @PrePersist
    protected void onCreate() {
        dateCreation = LocalDateTime.now();
        dateModification = LocalDateTime.now();
        if (placesRestantes == null) {
            placesRestantes = placesMax;
        }
        if (status == null) {
            status = FormationStatus.PLANIFIEE;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        dateModification = LocalDateTime.now();
    }
}