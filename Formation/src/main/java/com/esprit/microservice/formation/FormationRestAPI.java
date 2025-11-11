package com.esprit.microservice.formation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/formations")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Tag(name = "Formation API", description = "API de gestion des formations pour la plateforme Solidarité+")
public class FormationRestAPI {

    private final IFormationService formationService;

    @PostMapping
    @Operation(summary = "Créer une nouvelle formation")
    public ResponseEntity<Formation> createFormation(@RequestBody Formation formation) {
        try {
            Formation newFormation = formationService.addFormation(formation);
            return new ResponseEntity<>(newFormation, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Modifier une formation")
    public ResponseEntity<Formation> updateFormation(@PathVariable Long id, @RequestBody Formation formation) {
        try {
            Formation updatedFormation = formationService.updateFormation(id, formation);
            return ResponseEntity.ok(updatedFormation);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer une formation")
    public ResponseEntity<Void> deleteFormation(@PathVariable Long id) {
        try {
            formationService.deleteFormation(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Récupérer une formation par ID")
    public ResponseEntity<Formation> getFormationById(@PathVariable Long id) {
        try {
            Formation formation = formationService.getFormation(id);
            return ResponseEntity.ok(formation);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping
    @Operation(summary = "Lister toutes les formations")
    public ResponseEntity<List<Formation>> getAllFormations() {
        try {
            List<Formation> formations = formationService.getAllFormations();
            return ResponseEntity.ok(formations);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/association/{associationId}")
    @Operation(summary = "Formations par association")
    public ResponseEntity<List<Formation>> getFormationsByAssociation(@PathVariable Long associationId) {
        try {
            List<Formation> formations = formationService.getFormationsByAssociation(associationId);
            return ResponseEntity.ok(formations);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/disponibles")
    @Operation(summary = "Formations disponibles")
    public ResponseEntity<List<Formation>> getAvailableFormations() {
        try {
            List<Formation> formations = formationService.getAvailableFormations();
            return ResponseEntity.ok(formations);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/type/{type}")
    @Operation(summary = "Formations par type")
    public ResponseEntity<List<Formation>> getFormationsByType(@PathVariable String type) {
        try {
            List<Formation> formations = formationService.getFormationsByType(type);
            return ResponseEntity.ok(formations);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Changer le statut d'une formation")
    public ResponseEntity<Formation> updateFormationStatus(@PathVariable Long id, @RequestParam FormationStatus status) {
        try {
            Formation formation = formationService.updateFormationStatus(id, status);
            return ResponseEntity.ok(formation);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/statistics")
    @Operation(summary = "Statistiques des formations")
    public ResponseEntity<Map<String, Object>> getFormationStatistics(@RequestParam Long associationId, @RequestParam FormationStatus status) {
        try {
            Long count = formationService.getFormationStatistics(associationId, status);
            return ResponseEntity.ok(Map.of(
                    "associationId", associationId,
                    "status", status,
                    "count", count
            ));
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/search")
    @Operation(summary = "Recherche avancée")
    public ResponseEntity<List<Formation>> searchFormations(
            @RequestParam(required = false) String titre,
            @RequestParam(required = false) FormationType type,
            @RequestParam(required = false) FormationStatus status,
            @RequestParam(required = false) String lieu,
            @RequestParam(required = false) Long associationId) {
        try {
            // CORRECTION : Utiliser directement la méthode du service
            List<Formation> formations = formationService.searchFormations(titre, type, status, lieu, associationId);
            return ResponseEntity.ok(formations);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/upcoming")
    @Operation(summary = "Formations à venir")
    public ResponseEntity<List<Formation>> getUpcomingFormations() {
        try {
            List<Formation> formations = formationService.getUpcomingFormations();
            return ResponseEntity.ok(formations);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/current")
    @Operation(summary = "Formations en cours")
    public ResponseEntity<List<Formation>> getCurrentFormations() {
        try {
            List<Formation> formations = formationService.getCurrentFormations();
            return ResponseEntity.ok(formations);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PatchMapping("/{id}/places")
    @Operation(summary = "Mettre à jour les places")
    public ResponseEntity<Formation> updatePlaces(@PathVariable Long id, @RequestParam Integer placesRestantes) {
        try {
            // CORRECTION : Utiliser la méthode dédiée du service
            Formation formation = formationService.updatePlacesRestantes(id, placesRestantes);
            return ResponseEntity.ok(formation);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/public-cible/{publicCible}")
    @Operation(summary = "Formations par public cible")
    public ResponseEntity<List<Formation>> getFormationsByPublicCible(@PathVariable String publicCible) {
        try {
            List<Formation> formations = formationService.getFormationsByPublicCible(publicCible);
            return ResponseEntity.ok(formations);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/health")
    @Operation(summary = "Health Check")
    public ResponseEntity<Map<String, String>> healthCheck() {
        return ResponseEntity.ok(Map.of(
                "status", "UP",
                "service", "Formation API",
                "timestamp", LocalDateTime.now().toString()
        ));
    }
}