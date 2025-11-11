package com.esprit.microservice.formation;


import com.esprit.microservice.formation.Formation;
import com.esprit.microservice.formation.FormationStatus;
import com.esprit.microservice.formation.FormationType;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface IFormationService {

    /**
     * Créer une nouvelle formation
     * @param formation l'objet formation à créer
     * @return la formation créée
     */
    Formation addFormation(Formation formation);

    /**
     * Modifier une formation existante
     * @param id l'ID de la formation à modifier
     * @param formation les nouvelles données de la formation
     * @return la formation mise à jour
     */
    Formation updateFormation(Long id, Formation formation);

    /**
     * Supprimer une formation
     * @param id l'ID de la formation à supprimer
     */
    void deleteFormation(Long id);

    /**
     * Récupérer une formation par son ID
     * @param id l'ID de la formation
     * @return la formation trouvée
     */
    Formation getFormation(Long id);

    /**
     * Récupérer toutes les formations
     * @return la liste de toutes les formations
     */
    List<Formation> getAllFormations();

    /**
     * Récupérer les formations d'une association
     * @param associationId l'ID de l'association
     * @return la liste des formations de l'association
     */
    List<Formation> getFormationsByAssociation(Long associationId);

    /**
     * Récupérer les formations disponibles (avec places restantes)
     * @return la liste des formations disponibles
     */
    List<Formation> getAvailableFormations();

    /**
     * Mettre à jour le statut d'une formation
     * @param id l'ID de la formation
     * @param status le nouveau statut
     * @return la formation mise à jour
     */
    Formation updateFormationStatus(Long id, FormationStatus status);

    /**
     * Récupérer les formations par type
     * @param type le type de formation
     * @return la liste des formations du type spécifié
     */
    List<Formation> getFormationsByType(String type);

    /**
     * Obtenir des statistiques sur les formations
     * @param associationId l'ID de l'association (optionnel)
     * @param status le statut des formations (optionnel)
     * @return le nombre de formations correspondant aux critères
     */
    Long getFormationStatistics(Long associationId, FormationStatus status);

    /**
     * Rechercher des formations avec multiples critères
     * @param titre titre de la formation (optionnel)
     * @param type type de formation (optionnel)
     * @param status statut de la formation (optionnel)
     * @param lieu lieu de la formation (optionnel)
     * @param associationId ID de l'association (optionnel)
     * @return liste des formations correspondant aux critères
     */
    List<Formation> searchFormations(String titre, FormationType type, FormationStatus status, String lieu, Long associationId);

    /**
     * Récupérer les formations à venir
     * @return liste des formations dont la date de début est dans le futur
     */
    List<Formation> getUpcomingFormations();

    /**
     * Récupérer les formations en cours
     * @return liste des formations actuellement en cours
     */
    List<Formation> getCurrentFormations();

    /**
     * Mettre à jour le nombre de places restantes d'une formation
     * @param id l'ID de la formation
     * @param placesRestantes le nouveau nombre de places restantes
     * @return la formation mise à jour
     */
    Formation updatePlacesRestantes(Long id, Integer placesRestantes);

    /**
     * Récupérer les formations par public cible
     * @param publicCible le public cible
     * @return liste des formations pour le public cible
     */
    List<Formation> getFormationsByPublicCible(String publicCible);

    /**
     * Récupérer les formations par formateur
     * @param formateur le nom du formateur
     * @return liste des formations du formateur
     */
    List<Formation> getFormationsByFormateur(String formateur);

    /**
     * Vérifier si une formation existe par son titre
     * @param titre le titre de la formation
     * @return true si la formation existe, false sinon
     */
    boolean existsByTitre(String titre);

    /**
     * Récupérer les formations entre deux dates
     * @param startDate date de début
     * @param endDate date de fin
     * @return liste des formations dans l'intervalle de dates
     */
    List<Formation> getFormationsBetweenDates(LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Obtenir des statistiques avancées pour une association
     * @param associationId l'ID de l'association
     * @return map contenant les statistiques
     */
    Map<String, Object> getAdvancedStatistics(Long associationId);

    /**
     * Mettre à jour automatiquement les statuts des formations
     * Met à jour les statuts basés sur les dates actuelles
     */
    void updateFormationsStatusAutomatically();

    /**
     * Récupérer les formations similaires
     * @param formationId l'ID de la formation de référence
     * @return liste des formations similaires
     */
    List<Formation> getSimilarFormations(Long formationId);

    /**
     * Dupliquer une formation existante
     * @param id l'ID de la formation à dupliquer
     * @return la nouvelle formation créée
     */
    Formation duplicateFormation(Long id);
}
