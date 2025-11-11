import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { ReportService } from '../../services/report.service';
import { ReportRequest, ReportType, GenerateReportRequest } from '../../models/report.model';
import { KeycloakS } from '../../utils/keycloakService/keycloak-s';

@Component({
  selector: 'app-report-create',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './report-create.component.html',
  styleUrls: ['./report-create.component.css']
})
export class ReportCreateComponent implements OnInit {
  // Form model
  reportRequest: ReportRequest = {
    title: '',
    description: '',
    type: ReportType.GENERAL,
    generatedBy: ''
  };

  // Generate report model
  generateRequest: GenerateReportRequest = {
    start: '',
    end: '',
    generatedBy: ''
  };

  // Form state
  loading: boolean = false;
  error: string | null = null;
  showGenerateForm: boolean = false;
  selectedGenerateType: 'DONATION' | 'EVENT' | 'CAMPAIGN' = 'DONATION';

  // Enum values for template
  reportTypes = Object.values(ReportType);

  constructor(
    private reportService: ReportService,
    private router: Router,
    private keycloakService: KeycloakS
  ) {}

  ngOnInit(): void {
    // Get logged-in user from Keycloak
    try {
      const tokenParsed = this.keycloakService.keycloak.tokenParsed;
      if (tokenParsed) {
        const username = tokenParsed['preferred_username'] || tokenParsed['name'] || 'unknown-user';
        this.reportRequest.generatedBy = username;
        this.generateRequest.generatedBy = username;
      } else {
        this.reportRequest.generatedBy = 'system-user';
        this.generateRequest.generatedBy = 'system-user';
      }
    } catch (error) {
      console.error('Error getting Keycloak user:', error);
      this.reportRequest.generatedBy = 'system-user';
      this.generateRequest.generatedBy = 'system-user';
    }
  }

  createReport(): void {
    if (!this.validateForm()) {
      return;
    }

    this.loading = true;
    this.error = null;

    // Convert dates to ISO format if provided
    const requestCopy = { ...this.reportRequest };
    if (requestCopy.periodStart) {
      requestCopy.periodStart = new Date(requestCopy.periodStart).toISOString();
    }
    if (requestCopy.periodEnd) {
      requestCopy.periodEnd = new Date(requestCopy.periodEnd).toISOString();
    }

    this.reportService.createReport(requestCopy).subscribe({
      next: (report) => {
        this.loading = false;
        alert('Report created successfully!');
        this.router.navigate(['/reports', report.id]);
      },
      error: (err) => {
        this.loading = false;
        this.error = 'Failed to create report. Please try again.';
        console.error('Error creating report:', err);
      }
    });
  }

  generateReport(): void {
    if (!this.validateGenerateForm()) {
      return;
    }

    this.loading = true;
    this.error = null;

    // Convert dates to ISO format
    const startDate = new Date(this.generateRequest.start).toISOString();
    const endDate = new Date(this.generateRequest.end).toISOString();

    const request: GenerateReportRequest = {
      start: startDate,
      end: endDate,
      generatedBy: this.generateRequest.generatedBy
    };

    let serviceCall;
    switch (this.selectedGenerateType) {
      case 'DONATION':
        serviceCall = this.reportService.generateDonationReport(request);
        break;
      case 'EVENT':
        serviceCall = this.reportService.generateEventReport(request);
        break;
      case 'CAMPAIGN':
        serviceCall = this.reportService.generateCampaignReport(request);
        break;
    }

    serviceCall.subscribe({
      next: (report) => {
        this.loading = false;
        alert(`${this.selectedGenerateType} report generated successfully!`);
        this.router.navigate(['/reports', report.id]);
      },
      error: (err) => {
        this.loading = false;
        this.error = `Failed to generate ${this.selectedGenerateType.toLowerCase()} report. Please try again.`;
        console.error('Error generating report:', err);
      }
    });
  }

  validateForm(): boolean {
    if (!this.reportRequest.title.trim()) {
      this.error = 'Title is required';
      return false;
    }
    if (!this.reportRequest.type) {
      this.error = 'Report type is required';
      return false;
    }
    return true;
  }

  validateGenerateForm(): boolean {
    if (!this.generateRequest.start) {
      this.error = 'Start date is required';
      return false;
    }
    if (!this.generateRequest.end) {
      this.error = 'End date is required';
      return false;
    }
    if (new Date(this.generateRequest.start) > new Date(this.generateRequest.end)) {
      this.error = 'Start date must be before end date';
      return false;
    }
    return true;
  }

  toggleGenerateForm(): void {
    this.showGenerateForm = !this.showGenerateForm;
    this.error = null;
  }

  cancel(): void {
    this.router.navigate(['/reports']);
  }
}
