package com.example.donation.Service;

import com.example.donation.Entity.Donation;
import com.example.donation.Entity.DonationStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface IService {

    List<Donation> getAllDonations();

    Donation getDonationById(Long id);

    Donation addDonation(Donation donation);

    Donation updateDonation(Long id, Donation donationDetails);

    void deleteDonation(Long id);

    List<Donation> findByEmail(String email);

    List<Donation> findByDonorName(String donorName);

    Double getTotalDonationsByStatus(DonationStatus status);

    Double getTotalAmountByCampaign(String campaign);

    List<Donation> findDonationsBetweenDates(LocalDateTime start, LocalDateTime end);

}
