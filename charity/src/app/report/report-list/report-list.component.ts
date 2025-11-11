// report-list.component.ts - Enhanced with better delete handling
import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { ReportService } from '../../services/report.service';
import { Report, ReportType, ReportStatus } from '../../models/report.model';

@Component({
  selector: 'app-report-list',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './report-list.component.html',
  styleUrls: ['./report-list.component.css']
})
export class ReportListComponent implements OnInit {
  reports: Report[] = [];
  filteredReports: Report[] = [];
  loading: boolean = false;
  error: string | null = null;
  deletingId: number | null = null; // Track which report is being deleted

  // Filter properties
  selectedType: ReportType | 'ALL' = 'ALL';
  selectedStatus: ReportStatus | 'ALL' = 'ALL';

  // Enum values for template
  reportTypes = Object.values(ReportType);
  reportStatuses = Object.values(ReportStatus);

  constructor(
    private reportService: ReportService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.loadReports();
  }

  loadReports(): void {
    this.loading = true;
    this.error = null;

    this.reportService.getAllReports().subscribe({
      next: (data) => {
        this.reports = data;
        this.filteredReports = data;
        this.loading = false;
      },
      error: (err) => {
        this.error = 'Failed to load reports. Please try again.';
        this.loading = false;
        console.error('Error loading reports:', err);
      }
    });
  }

  filterByType(type: ReportType | 'ALL'): void {
    this.selectedType = type;
    this.applyFilters();
  }

  filterByStatus(status: ReportStatus | 'ALL'): void {
    this.selectedStatus = status;
    this.applyFilters();
  }

  applyFilters(): void {
    this.filteredReports = this.reports.filter(report => {
      const typeMatch = this.selectedType === 'ALL' || report.type === this.selectedType;
      const statusMatch = this.selectedStatus === 'ALL' || report.status === this.selectedStatus;
      return typeMatch && statusMatch;
    });
  }

  viewReport(id: number): void {
    this.router.navigate(['/reports', id]);
  }

  editReport(id: number): void {
    this.router.navigate(['/reports/edit', id]);
  }

  createNewReport(): void {
    this.router.navigate(['/reports/create']);
  }

  deleteReport(report: Report, event: Event): void {
    event.stopPropagation(); // Prevent row click if you have one

    const confirmMessage = `Are you sure you want to delete "${report.title}"?\n\nThis action cannot be undone.`;

    if (confirm(confirmMessage)) {
      this.deletingId = report.id; // Show loading state on this specific button

      this.reportService.deleteReport(report.id).subscribe({
        next: () => {
          // Remove from local array for instant UI feedback
          this.reports = this.reports.filter(r => r.id !== report.id);
          this.applyFilters();
          this.deletingId = null;

          // Show success message (optional)
          this.showSuccessMessage('Report deleted successfully');
        },
        error: (err) => {
          this.deletingId = null;
          this.showErrorMessage('Failed to delete report. Please try again.');
          console.error('Error deleting report:', err);
        }
      });
    }
  }

  // Optional: Add success/error message handlers
  private showSuccessMessage(message: string): void {
    // You can implement a toast/snackbar here
    console.log('Success:', message);
    // For now, just using alert - replace with your toast service
    // alert(message);
  }

  private showErrorMessage(message: string): void {
    alert(message);
  }

  getStatusClass(status: ReportStatus): string {
    switch (status) {
      case ReportStatus.COMPLETED:
        return 'status-completed';
      case ReportStatus.IN_PROGRESS:
        return 'status-in-progress';
      case ReportStatus.PENDING:
        return 'status-pending';
      case ReportStatus.FAILED:
        return 'status-failed';
      case ReportStatus.CANCELLED:
        return 'status-cancelled';
      default:
        return '';
    }
  }

  formatDate(dateString: string): string {
    const date = new Date(dateString);
    return date.toLocaleDateString() + ' ' + date.toLocaleTimeString();
  }

  // Helper to check if a specific report is being deleted
  isDeleting(reportId: number): boolean {
    return this.deletingId === reportId;
  }
}
