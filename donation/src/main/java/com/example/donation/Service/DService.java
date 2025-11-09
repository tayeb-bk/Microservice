package com.example.donation.Service;

import com.example.donation.Entity.Donation;
import com.example.donation.Entity.DonationStatus;
import com.example.donation.Repository.DonationRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.FluentQuery;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;


@Service
@AllArgsConstructor
public class DService implements IService {


      DonationRepository donationRepository;

    @Override
    public List<Donation> getAllDonations() {
        return donationRepository.findAll();
    }


    @Override
    public Donation getDonationById(Long id) {
        return donationRepository.findById(id).orElse(null);
    }
    @Override
    public Donation addDonation(Donation donation) {
        donation.setDonationDate(LocalDateTime.now());
        donation.setDonationStatus(DonationStatus.PENDING); // Par défaut PENDING
        return donationRepository.save(donation);
    }

    @Override
    public Donation updateDonation(Long id, Donation donationDetails) {
        return donationRepository.findById(id)
                .map(donation -> {
                    donation.setDonorName(donationDetails.getDonorName());
                    donation.setEmail(donationDetails.getEmail());
                    donation.setAmount(donationDetails.getAmount());
                    donation.setPaymentMethod(donationDetails.getPaymentMethod());
                    donation.setDonationStatus(donationDetails.getDonationStatus());
                    donation.setMessage(donationDetails.getMessage());
                    donation.setCampaign(donationDetails.getCampaign());
                    return donationRepository.save(donation);
                })
                .orElse(null);
    }

    @Override
    public void deleteDonation(Long id) {
        donationRepository.deleteById(id);
    }

    @Override
    public List<Donation> findByEmail(String email) {
        return donationRepository.findByEmail(email);
    }

    @Override
    public List<Donation> findByDonorName(String donorName) {
        return donationRepository.findByDonorNameContainingIgnoreCase(donorName);
    }

    @Override
    public Double getTotalDonationsByStatus(DonationStatus status) {
        return donationRepository.getTotalDonationsAmountByStatus(status).orElse(0.0);
    }

    @Override
    public Double getTotalAmountByCampaign(String campaign) {
        return 0.0;
    }


//    @Override
//    public Double getTotalAmountByCampaign(String campaign) {
//        // On calcule toujours pour les donations APPROVED
//        return donationRepository.getTotalAmountByCampaignAndStatus(campaign, DonationStatus.APPROVED)
//                .orElse(0.0);
//    }

    @Override
    public List<Donation> findDonationsBetweenDates(LocalDateTime start, LocalDateTime end) {
        return donationRepository.findByDonationDateBetween(start, end);
    }

















}
