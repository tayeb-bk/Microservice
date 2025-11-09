package com.example.donation.Controller;

import com.example.donation.Entity.Donation;
import com.example.donation.Entity.DonationStatus;
import com.example.donation.Service.DService;
import com.example.donation.Service.IService;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
@RestController
@AllArgsConstructor
@RequestMapping("/api/donations")
@CrossOrigin(origins = {
        "http://localhost:4200",   // ✅ mode développement Angular
        "http://localhost:8083"    // ✅ mode docker Angular via Nginx
})
public class DonationController {
    private  IService iService;


    @GetMapping
    public List<Donation> getAllDonations() {
        return iService.getAllDonations();
    }

    @GetMapping("/{id}")
    public Donation getDonationById(@PathVariable Long id) {
        return iService.getDonationById(id);
    }

    @PostMapping
    public Donation addDonation(@RequestBody Donation donation) {
        return iService.addDonation(donation);
    }

    @PutMapping("/{id}")
    public Donation updateDonation(@PathVariable Long id, @RequestBody Donation donation) {
        return iService.updateDonation(id, donation);
    }

    @DeleteMapping("/{id}")
    public void deleteDonation(@PathVariable Long id) {
        iService.deleteDonation(id);
    }

    // -------------------- Recherche --------------------

    @GetMapping("/email/{email}")
    public List<Donation> getDonationsByEmail(@PathVariable String email) {
        return iService.findByEmail(email);
    }


    @GetMapping("/donor/{name}")
    public List<Donation> getDonationsByDonorName(@PathVariable String name) {
        return iService.findByDonorName(name);
    }

    @GetMapping("/between")
    public List<Donation> getDonationsBetweenDates(@RequestParam("start") String start,
                                                   @RequestParam("end") String end) {
        LocalDateTime startDate = LocalDateTime.parse(start);
        LocalDateTime endDate = LocalDateTime.parse(end);
        return iService.findDonationsBetweenDates(startDate, endDate);
    }



    @GetMapping("/total/status/{status}")
    public Double getTotalByStatus(@PathVariable DonationStatus status) {
        return iService.getTotalDonationsByStatus(status);
    }

    @GetMapping("/total/campaign/{campaign}")
    public Double getTotalByCampaign(@PathVariable String campaign) {
        return iService.getTotalAmountByCampaign(campaign);
    }

}
