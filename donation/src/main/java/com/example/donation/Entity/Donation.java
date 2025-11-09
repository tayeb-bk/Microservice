package com.example.donation.Entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Donation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)




    private Long id;

    private String donorName;

    private String email;


    private Double amount;


    private String paymentMethod;


    @Enumerated(EnumType.STRING)
    private DonationStatus  donationStatus;


    private LocalDateTime donationDate;

    private String message;


    private String campaign;

}
