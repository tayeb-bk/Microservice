package com.example.donation.Repository;

import com.example.donation.Entity.Donation;
import com.example.donation.Entity.DonationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface DonationRepository extends JpaRepository<Donation, Long> {


    // 🔹 Trouver les donations par email du donateur
    List<Donation> findByEmail(String email);

    // 🔹 Trouver les donations par nom de donateur (insensible à la casse)
    List<Donation> findByDonorNameContainingIgnoreCase(String donorName);

    // 🔹 Calculer le total des donations par statut
    @Query("SELECT SUM(d.amount) FROM Donation d WHERE d.donationStatus = :status")
    Optional<Double> getTotalDonationsAmountByStatus(@Param("status") DonationStatus status);

    // 🔹 Calculer le total par campagne et par statut
    @Query("SELECT SUM(d.amount) FROM Donation d WHERE d.campaign = :campaign AND d.donationStatus = :status")
    Optional<Double> getTotalAmountByCampaignAndStatus(@Param("campaign") String campaign,
                                                       @Param("status") DonationStatus status);

    // 🔹 Trouver les donations faites entre deux dates
    List<Donation> findByDonationDateBetween(LocalDateTime start, LocalDateTime end);
}










