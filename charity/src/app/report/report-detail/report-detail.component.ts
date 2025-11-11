import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';
import { ReportService } from '../../services/report.service';
import { Report, ReportStatus } from '../../models/report.model';

@Component({
  selector: 'app-report-detail',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './report-detail.component.html',
  styleUrls: ['./report-detail.component.css']
})
export class ReportDetailComponent implements OnInit {
  report: Report | null = null;
  loading: boolean = false;
  error: string | null = null;
  reportId: number = 0;

  // Enum for template
  ReportStatus = ReportStatus;

  constructor(
    private reportService: ReportService,
    private route: ActivatedRoute,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.route.params.subscribe(params => {
      this.reportId = +params['id'];
      this.loadReport();
    });
  }

  loadReport(): void {
    this.loading = true;
    this.error = null;

    this.reportService.getReportById(this.reportId).subscribe({
      next: (data) => {
        this.report = data;
        this.loading = false;
      },
      error: (err) => {
        this.error = 'Failed to load report details. Please try again.';
        this.loading = false;
        console.error('Error loading report:', err);
      }
    });
  }

  updateStatus(newStatus: ReportStatus): void {
    if (!this.report) return;

    if (confirm(`Are you sure you want to change status to ${newStatus}?`)) {
      this.reportService.updateReportStatus(this.report.id, newStatus).subscribe({
        next: (updatedReport) => {
          this.report = updatedReport;
          alert('Status updated successfully!');
        },
        error: (err) => {
          alert('Failed to update status');
          console.error('Error updating status:', err);
        }
      });
    }
  }

  deleteReport(): void {
    if (!this.report) return;

    if (confirm('Are you sure you want to delete this report? This action cannot be undone.')) {
      this.reportService.deleteReport(this.report.id).subscribe({
        next: () => {
          alert('Report deleted successfully!');
          this.router.navigate(['/reports']);
        },
        error: (err) => {
          alert('Failed to delete report');
          console.error('Error deleting report:', err);
        }
      });
    }
  }

  backToList(): void {
    this.router.navigate(['/reports']);
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
    return date.toLocaleDateString('en-US', {
      year: 'numeric',
      month: 'long',
      day: 'numeric',
      hour: '2-digit',
      minute: '2-digit'
    });
  }

  downloadReport(): void {
    if (!this.report) return;

    this.reportService.downloadReportPdf(this.report.id).subscribe({
      next: (blob) => {
        // Create a blob URL
        const url = window.URL.createObjectURL(blob);

        // Create a temporary anchor element
        const link = document.createElement('a');
        link.href = url;
        link.download = `report-${this.report!.id}-${this.report!.title}.pdf`;

        // Trigger download
        document.body.appendChild(link);
        link.click();

        // Cleanup
        document.body.removeChild(link);
        window.URL.revokeObjectURL(url);
      },
      error: (err) => {
        alert('Failed to download PDF');
        console.error('Error downloading PDF:', err);
      }
    });
  }

  editReport(): void {
    if (this.report) {
      this.router.navigate(['/reports/edit', this.report.id]);
    }
  }
}
