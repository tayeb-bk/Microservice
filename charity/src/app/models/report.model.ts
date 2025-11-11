// Enums matching backend
export enum ReportType {
  DONATION = 'DONATION',
  EVENT = 'EVENT',
  CAMPAIGN = 'CAMPAIGN',
  BLOG = 'BLOG',
  FORMATION = 'FORMATION',
  NOTIFICATION = 'NOTIFICATION',
  GENERAL = 'GENERAL'
}

export enum ReportStatus {
  PENDING = 'PENDING',
  IN_PROGRESS = 'IN_PROGRESS',
  COMPLETED = 'COMPLETED',
  FAILED = 'FAILED',
  CANCELLED = 'CANCELLED'
}

// Report Data DTO
export interface ReportData {
  dataKey: string;
  dataValue: string;
  dataType: string;
  displayOrder?: number;
}

// Report Request DTO
export interface ReportRequest {
  title: string;
  description?: string;
  type: ReportType;
  periodStart?: string; // ISO date string
  periodEnd?: string;   // ISO date string
  generatedBy: string;
}

// Report Response DTO
export interface Report {
  id: number;
  title: string;
  description?: string;
  type: ReportType;
  createdDate: string;
  updatedDate: string;
  periodStart?: string;
  periodEnd?: string;
  generatedBy: string;
  status: ReportStatus;
  filePath?: string;
  reportData?: ReportData[];
}

// Generate Report Request
export interface GenerateReportRequest {
  start: string;  // ISO date string
  end: string;    // ISO date string
  generatedBy: string;
}