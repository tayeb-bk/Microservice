package com.esprit.microservice.formation;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FormationService implements IFormationService {

    private final FormationRepository formationRepository;

    @Override
    public Formation addFormation(Formation formation) {
        if (formationRepository.findByTitre(formation.getTitre()).isPresent()) {
            throw new RuntimeException("Une formation avec ce titre existe déjà");
        }

        if (formation.getPlacesRestantes() == null) {
            formation.setPlacesRestantes(formation.getPlacesMax());
        }

        if (formation.getStatus() == null) {
            formation.setStatus(FormationStatus.PLANIFIEE);
        }

        return formationRepository.save(formation);
    }

    @Override
    public Formation updateFormation(Long id, Formation formation) {
        Formation existingFormation = formationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Formation non trouvée avec l'ID: " + id));

        formationRepository.findByTitre(formation.getTitre())
                .ifPresent(f -> {
                    if (!f.getId().equals(id)) {
                        throw new RuntimeException("Une formation avec ce titre existe déjà");
                    }
                });

        existingFormation.setTitre(formation.getTitre());
        existingFormation.setDescription(formation.getDescription());
        existingFormation.setFormateur(formation.getFormateur());
        existingFormation.setDureeHeures(formation.getDureeHeures());
        existingFormation.setPlacesMax(formation.getPlacesMax());
        existingFormation.setDateDebut(formation.getDateDebut());
        existingFormation.setDateFin(formation.getDateFin());
        existingFormation.setType(formation.getType());
        existingFormation.setLieu(formation.getLieu());
        existingFormation.setPrerequis(formation.getPrerequis());
        existingFormation.setPublicCible(formation.getPublicCible());
        existingFormation.setCompetencesAcquises(formation.getCompetencesAcquises());

        return formationRepository.save(existingFormation);
    }

    @Override
    public void deleteFormation(Long id) {
        Formation formation = formationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Formation non trouvée avec l'ID: " + id));
        formationRepository.delete(formation);
    }

    @Override
    public Formation getFormation(Long id) {
        return formationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Formation non trouvée avec l'ID: " + id));
    }

    @Override
    public List<Formation> getAllFormations() {
        return formationRepository.findAll();
    }

    @Override
    public List<Formation> getFormationsByAssociation(Long associationId) {
        return formationRepository.findByAssociationId(associationId);
    }

    @Override
    public List<Formation> getAvailableFormations() {
        return formationRepository.findAvailableFormations();
    }

    @Override
    public Formation updateFormationStatus(Long id, FormationStatus status) {
        Formation formation = formationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Formation non trouvée avec l'ID: " + id));

        formation.setStatus(status);
        return formationRepository.save(formation);
    }

    @Override
    public List<Formation> getFormationsByType(String type) {
        try {
            FormationType formationType = FormationType.valueOf(type.toUpperCase());
            return formationRepository.findByType(formationType);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Type de formation non valide: " + type);
        }
    }

    @Override
    public Long getFormationStatistics(Long associationId, FormationStatus status) {
        return formationRepository.countByAssociationIdAndStatus(associationId, status);
    }

    @Override
    public List<Formation> searchFormations(String titre, FormationType type, FormationStatus status, String lieu, Long associationId) {
        // CORRECTION : Utiliser la méthode du repository au lieu du filtrage manuel
        return formationRepository.searchFormations(titre, type, status, lieu, associationId);
    }

    @Override
    public List<Formation> getUpcomingFormations() {
        return formationRepository.findByStatus(FormationStatus.PLANIFIEE);
    }

    @Override
    public List<Formation> getCurrentFormations() {
        return formationRepository.findByStatus(FormationStatus.EN_COURS);
    }
    @Override
    public Formation updatePlacesRestantes(Long id, Integer placesRestantes) {
        Formation formation = formationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Formation non trouvée avec l'ID: " + id));

        if (placesRestantes < 0 || placesRestantes > formation.getPlacesMax()) {
            throw new RuntimeException("Nombre de places restantes invalide");
        }

        formation.setPlacesRestantes(placesRestantes);
        return formationRepository.save(formation);
    }

    @Override
    public List<Formation> getFormationsByPublicCible(String publicCible) {
        return formationRepository.findByPublicCibleContainingIgnoreCase(publicCible);
    }

    @Override
    public List<Formation> getFormationsByFormateur(String formateur) {
        return formationRepository.findByFormateurContainingIgnoreCase(formateur);
    }

    @Override
    public boolean existsByTitre(String titre) {
        return formationRepository.findByTitre(titre).isPresent();
    }

    @Override
    public List<Formation> getFormationsBetweenDates(LocalDateTime startDate, LocalDateTime endDate) {
        return formationRepository.findFormationsBetweenDates(startDate, endDate);
    }

    @Override
    public Map<String, Object> getAdvancedStatistics(Long associationId) {
        Map<String, Object> stats = new HashMap<>();

        List<Formation> formations;
        if (associationId != null) {
            formations = formationRepository.findByAssociationId(associationId);
        } else {
            formations = formationRepository.findAll();
        }

        long totalFormations = formations.size();
        long formationsPlanifiees = formations.stream().filter(f -> f.getStatus() == FormationStatus.PLANIFIEE).count();
        long formationsEnCours = formations.stream().filter(f -> f.getStatus() == FormationStatus.EN_COURS).count();
        long formationsTerminees = formations.stream().filter(f -> f.getStatus() == FormationStatus.TERMINEE).count();
        long totalPlaces = formations.stream().mapToInt(Formation::getPlacesMax).sum();
        long placesOccupees = formations.stream().mapToInt(f -> f.getPlacesMax() - f.getPlacesRestantes()).sum();

        stats.put("totalFormations", totalFormations);
        stats.put("formationsPlanifiees", formationsPlanifiees);
        stats.put("formationsEnCours", formationsEnCours);
        stats.put("formationsTerminees", formationsTerminees);
        stats.put("totalPlaces", totalPlaces);
        stats.put("placesOccupees", placesOccupees);
        stats.put("tauxOccupation", totalPlaces > 0 ? (double) placesOccupees / totalPlaces * 100 : 0);

        Map<FormationType, Long> statsByType = formations.stream()
                .collect(Collectors.groupingBy(Formation::getType, Collectors.counting()));
        stats.put("statistiquesParType", statsByType);

        return stats;
    }

    @Override
    public void updateFormationsStatusAutomatically() {
        LocalDateTime now = LocalDateTime.now();

        List<Formation> formationsToStart = formationRepository.findFormationsToUpdateStatus(now);
        formationsToStart.forEach(formation -> {
            formation.setStatus(FormationStatus.EN_COURS);
            formationRepository.save(formation);
        });

        List<Formation> formationsEnCours = formationRepository.findByStatus(FormationStatus.EN_COURS);
        formationsEnCours.stream()
                .filter(formation -> formation.getDateFin().isBefore(now))
                .forEach(formation -> {
                    formation.setStatus(FormationStatus.TERMINEE);
                    formationRepository.save(formation);
                });
    }

    @Override
    public List<Formation> getSimilarFormations(Long formationId) {
        Formation referenceFormation = formationRepository.findById(formationId)
                .orElseThrow(() -> new RuntimeException("Formation non trouvée avec l'ID: " + formationId));

        return formationRepository.findByType(referenceFormation.getType()).stream()
                .filter(formation -> !formation.getId().equals(formationId))
                .filter(formation -> formation.getStatus() == FormationStatus.PLANIFIEE)
                .limit(5)
                .collect(Collectors.toList());
    }

    @Override
    public Formation duplicateFormation(Long id) {
        Formation originalFormation = formationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Formation non trouvée avec l'ID: " + id));

        Formation duplicatedFormation = Formation.builder()
                .titre(originalFormation.getTitre() + " (Copie)")
                .description(originalFormation.getDescription())
                .formateur(originalFormation.getFormateur())
                .dureeHeures(originalFormation.getDureeHeures())
                .placesMax(originalFormation.getPlacesMax())
                .placesRestantes(originalFormation.getPlacesMax())
                .dateDebut(originalFormation.getDateDebut().plusMonths(1))
                .dateFin(originalFormation.getDateFin().plusMonths(1))
                .status(FormationStatus.PLANIFIEE)
                .type(originalFormation.getType())
                .lieu(originalFormation.getLieu())
                .prerequis(originalFormation.getPrerequis())
                .publicCible(originalFormation.getPublicCible())
                .competencesAcquises(originalFormation.getCompetencesAcquises())
                .associationId(originalFormation.getAssociationId())
                .build();

        return formationRepository.save(duplicatedFormation);
    }
}