import { Component, EventEmitter, Output } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ReportType, ReportStatus } from '../../models/report.model';

@Component({
  selector: 'app-report-filter',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './report-filter.component.html',
  styleUrls: ['./report-filter.component.css']
})
export class ReportFilterComponent {
  selectedType: ReportType | 'ALL' = 'ALL';
  selectedStatus: ReportStatus | 'ALL' = 'ALL';
  searchTerm: string = '';
  dateFrom: string = '';
  dateTo: string = '';

  // Enum values for template
  reportTypes = Object.values(ReportType);
  reportStatuses = Object.values(ReportStatus);

  // Emit filter changes to parent component
  @Output() filterChange = new EventEmitter<any>();

  onFilterChange(): void {
    const filters = {
      type: this.selectedType,
      status: this.selectedStatus,
      searchTerm: this.searchTerm,
      dateFrom: this.dateFrom,
      dateTo: this.dateTo
    };
    this.filterChange.emit(filters);
  }

  resetFilters(): void {
    this.selectedType = 'ALL';
    this.selectedStatus = 'ALL';
    this.searchTerm = '';
    this.dateFrom = '';
    this.dateTo = '';
    this.onFilterChange();
  }
}
