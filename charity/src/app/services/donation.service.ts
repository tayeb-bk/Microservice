// src/app/services/donation.service.ts
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface Donation {
  id?: number;
  donorName: string;
  email: string;
  amount: number;
  paymentMethod: string;
  donationStatus?: string;
  donationDate?: string;
  message?: string;
  campaign?: string;
}

@Injectable({ providedIn: 'root' })
export class DonationService {
  private apiUrl = 'http://localhost:8282/api/donations';

  constructor(private http: HttpClient) {}

  getAll(): Observable<Donation[]> {
    return this.http.get<Donation[]>(`${this.apiUrl}`);
  }

  getById(id: number): Observable<Donation> {
    return this.http.get<Donation>(`${this.apiUrl}/${id}`);
  }

  add(donation: Donation): Observable<Donation> {
    return this.http.post<Donation>(`${this.apiUrl}`, donation);
  }

  update(id: number, donation: Donation): Observable<Donation> {
    return this.http.put<Donation>(`${this.apiUrl}/${id}`, donation);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  getByEmail(email: string): Observable<Donation[]> {
    return this.http.get<Donation[]>(`${this.apiUrl}/email/${email}`);
  }

  getByDonorName(name: string): Observable<Donation[]> {
    return this.http.get<Donation[]>(`${this.apiUrl}/donor/${name}`);
  }

  getTotalByStatus(status: string): Observable<number> {
    return this.http.get<number>(`${this.apiUrl}/total/status/${status}`);
  }

  getTotalByCampaign(campaign: string): Observable<number> {
    return this.http.get<number>(`${this.apiUrl}/total/campaign/${campaign}`);
  }
}
