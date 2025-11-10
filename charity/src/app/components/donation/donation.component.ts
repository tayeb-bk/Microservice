// src/app/components/donation/donation.component.ts
import { Component, OnInit } from '@angular/core';
import { Donation, DonationService } from '../../services/donation.service';
import { FormsModule } from '@angular/forms';
import { CurrencyPipe, CommonModule } from '@angular/common';
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
  newDonation: Donation = this.initializeForm();

  isEditMode: boolean = false;
  editingDonationId: number | null = null;
  isLoading: boolean = false;
  successMessage: string = '';
  errorMessage: string = '';

  constructor(private donationService: DonationService) {}

  ngOnInit(): void {
    this.loadDonations();
  }

  loadDonations(): void {
    this.isLoading = true;
    this.donationService.getAll().subscribe({
      next: (data) => {
        this.donations = data;
        this.isLoading = false;
      },
      error: (err) => {
        console.error('Erreur chargement dons:', err);
        this.errorMessage = 'Erreur lors du chargement des donations';
        this.isLoading = false;
      }
    });
  }

  addDonation(): void {
    if (!this.validateForm()) return;

    this.isLoading = true;
    this.errorMessage = '';
    this.successMessage = '';

    if (this.isEditMode && this.editingDonationId) {
      this.updateDonation();
    } else {
      this.createDonation();
    }
  }

  private createDonation(): void {
    this.donationService.add(this.newDonation).subscribe({
      next: (donation) => {
        this.donations.push(donation);
        this.successMessage = '✅ Donation ajoutée avec succès!';
        this.resetForm();
        this.isLoading = false;
        this.clearMessages();
      },
      error: (err) => {
        console.error('Erreur ajout donation:', err);
        this.errorMessage = 'Erreur lors de l\'ajout de la donation';
        this.isLoading = false;
      }
    });
  }

  private updateDonation(): void {
    if (!this.editingDonationId) return;

    this.donationService.update(this.editingDonationId, this.newDonation).subscribe({
      next: (updatedDonation) => {
        const index = this.donations.findIndex(d => d.id === this.editingDonationId);
        if (index !== -1) {
          this.donations[index] = updatedDonation;
        }
        this.successMessage = '✅ Donation mise à jour avec succès!';
        this.resetForm();
        this.isLoading = false;
        this.clearMessages();
      },
      error: (err) => {
        console.error('Erreur mise à jour donation:', err);
        this.errorMessage = 'Erreur lors de la mise à jour de la donation';
        this.isLoading = false;
      }
    });
  }

  editDonation(donation: Donation): void {
    this.isEditMode = true;
    this.editingDonationId = donation.id!;
    this.newDonation = { ...donation };
    this.errorMessage = '';
    this.successMessage = '';
    window.scrollTo({ top: 0, behavior: 'smooth' });
  }

  cancelEdit(): void {
    this.resetForm();
  }

  deleteDonation(id: number): void {
    if (!confirm('Êtes-vous sûr de vouloir supprimer cette donation?')) return;

    this.isLoading = true;
    this.donationService.delete(id).subscribe({
      next: () => {
        this.donations = this.donations.filter((d) => d.id !== id);
        this.successMessage = '✅ Donation supprimée avec succès!';
        this.isLoading = false;
        this.clearMessages();
      },
      error: (err) => {
        console.error('Erreur suppression:', err);
        this.errorMessage = 'Erreur lors de la suppression de la donation';
        this.isLoading = false;
      }
    });
  }

  private validateForm(): boolean {
    if (!this.newDonation.donorName || this.newDonation.donorName.trim() === '') {
      this.errorMessage = '⚠️ Le nom du donateur est requis';
      return false;
    }
    if (!this.newDonation.amount || this.newDonation.amount <= 0) {
      this.errorMessage = '⚠️ Le montant doit être supérieur à 0';
      return false;
    }
    if (this.newDonation.email && !this.isValidEmail(this.newDonation.email)) {
      this.errorMessage = '⚠️ L\'email n\'est pas valide';
      return false;
    }
    return true;
  }

  private isValidEmail(email: string): boolean {
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return emailRegex.test(email);
  }

  private initializeForm(): Donation {
    return {
      donorName: '',
      email: '',
      amount: 0,
      paymentMethod: '',
      message: '',
      campaign: ''
    };
  }

  private resetForm(): void {
    this.newDonation = this.initializeForm();
    this.isEditMode = false;
    this.editingDonationId = null;
  }

  private clearMessages(): void {
    setTimeout(() => {
      this.successMessage = '';
      this.errorMessage = '';
    }, 3000);
  }
}
