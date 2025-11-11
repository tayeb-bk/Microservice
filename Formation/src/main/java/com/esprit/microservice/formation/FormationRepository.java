package com.esprit.microservice.formation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface FormationRepository extends JpaRepository<Formation, Long> {

    List<Formation> findByAssociationId(Long associationId);
    List<Formation> findByStatus(FormationStatus status);
    List<Formation> findByType(FormationType type);
    List<Formation> findByFormateurContainingIgnoreCase(String formateur);
    List<Formation> findByTitreContainingIgnoreCase(String titre);
    List<Formation> findByLieuContainingIgnoreCase(String lieu);
    List<Formation> findByPlacesRestantesGreaterThan(Integer places);
    List<Formation> findByPublicCibleContainingIgnoreCase(String publicCible);
    Optional<Formation> findByTitre(String titre);
    List<Formation> findByAssociationIdAndStatus(Long associationId, FormationStatus status);
    List<Formation> findByAssociationIdAndType(Long associationId, FormationType type);
    Long countByAssociationId(Long associationId);
    Long countByAssociationIdAndStatus(Long associationId, FormationStatus status);
    Long countByStatus(FormationStatus status);

    @Query("SELECT f FROM Formation f WHERE f.placesRestantes > 0 AND f.status = 'PLANIFIEE'")
    List<Formation> findAvailableFormations();

    @Query("SELECT f FROM Formation f WHERE f.dateDebut BETWEEN :startDate AND :endDate")
    List<Formation> findFormationsBetweenDates(@Param("startDate") LocalDateTime startDate,
                                               @Param("endDate") LocalDateTime endDate);

    @Query("SELECT f FROM Formation f WHERE f.dateDebut > :currentDate")
    List<Formation> findUpcomingFormations(@Param("currentDate") LocalDateTime currentDate);

    @Query("SELECT f FROM Formation f WHERE f.dateDebut <= :currentDate AND f.dateFin >= :currentDate")
    List<Formation> findCurrentFormations(@Param("currentDate") LocalDateTime currentDate);

    @Query("SELECT f FROM Formation f WHERE f.status = :status ORDER BY f.dateCreation DESC")
    List<Formation> findLatestFormationsByStatus(@Param("status") FormationStatus status);

    @Query("SELECT f.type, COUNT(f) FROM Formation f WHERE f.associationId = :associationId GROUP BY f.type")
    List<Object[]> countFormationsByTypeForAssociation(@Param("associationId") Long associationId);

    @Query("SELECT f FROM Formation f WHERE " +
            "(f.dateDebut BETWEEN :startDate AND :endDate) OR " +
            "(f.dateFin BETWEEN :startDate AND :endDate) OR " +
            "(f.dateDebut <= :startDate AND f.dateFin >= :endDate)")
    List<Formation> findOverlappingFormations(@Param("startDate") LocalDateTime startDate,
                                              @Param("endDate") LocalDateTime endDate);

    @Query("SELECT f FROM Formation f WHERE " +
            "(:titre IS NULL OR LOWER(f.titre) LIKE LOWER(CONCAT('%', :titre, '%'))) AND " +
            "(:type IS NULL OR f.type = :type) AND " +
            "(:status IS NULL OR f.status = :status) AND " +
            "(:lieu IS NULL OR LOWER(f.lieu) LIKE LOWER(CONCAT('%', :lieu, '%'))) AND " +
            "(:associationId IS NULL OR f.associationId = :associationId)")
    List<Formation> searchFormations(
            @Param("titre") String titre,
            @Param("type") FormationType type,
            @Param("status") FormationStatus status,
            @Param("lieu") String lieu,
            @Param("associationId") Long associationId);

    @Query("SELECT f FROM Formation f WHERE f.status = 'PLANIFIEE' AND f.dateDebut < :currentDate")
    List<Formation> findFormationsToUpdateStatus(@Param("currentDate") LocalDateTime currentDate);
}