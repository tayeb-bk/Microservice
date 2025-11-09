// src/app/components/donation/donation.component.ts
import { Component, OnInit } from '@angular/core';
import { Donation, DonationService } from '../../services/donation.service';
import {FormsModule} from '@angular/forms';
import {CurrencyPipe} from '@angular/common';
import { CommonModule } from '@angular/common'; // ✅ AJOUTE CECI
import { HttpClientModule } from '@angular/common/http';

@Component({
  selector: 'app-donation',
  templateUrl: './donation.component.html',
  imports: [
    FormsModule,
    CurrencyPipe,
    CommonModule,
    HttpClientModule
  ],
  styleUrls: ['./donation.component.css']
})
export class DonationComponent implements OnInit {
  donations: Donation[] = [];
  newDonation: Donation = {
    donorName: '',
    email: '',
    amount: 0,
    paymentMethod: '',
    message: '',
    campaign: ''
  };

  constructor(private donationService: DonationService) {}

  ngOnInit(): void {
    this.loadDonations();
  }

  loadDonations(): void {
    this.donationService.getAll().subscribe({
      next: (data) => (this.donations = data),
      error: (err) => console.error('Erreur chargement dons:', err)
    });
  }

  addDonation(): void {
    if (!this.newDonation.donorName || !this.newDonation.amount) return;

    this.donationService.add(this.newDonation).subscribe({
      next: (d) => {
        this.donations.push(d);
        this.newDonation = {
          donorName: '',
          email: '',
          amount: 0,
          paymentMethod: '',
          message: '',
          campaign: ''
        };
      },
      error: (err) => console.error('Erreur ajout donation:', err)
    });
  }

  deleteDonation(id: number): void {
    this.donationService.delete(id).subscribe({
      next: () => (this.donations = this.donations.filter((d) => d.id !== id)),
      error: (err) => console.error('Erreur suppression:', err)
    });
  }
}
