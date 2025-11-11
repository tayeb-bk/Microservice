import { environment } from '../../environments/environment';
import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError, retry } from 'rxjs/operators';
import { Report, ReportRequest, ReportType, ReportStatus, GenerateReportRequest } from '../models/report.model';


@Injectable({
  providedIn: 'root'
})
export class ReportService {
  private apiUrl = `${environment.reportServiceUrl}/api/reports`;

  constructor(private http: HttpClient) {}

  // Get all reports
  getAllReports(): Observable<Report[]> {
    return this.http.get<Report[]>(this.apiUrl)
      .pipe(
        retry(1),
        catchError(this.handleError)
      );
  }

  // Get report by ID
  getReportById(id: number): Observable<Report> {
    return this.http.get<Report>(`${this.apiUrl}/${id}`)
      .pipe(
        retry(1),
        catchError(this.handleError)
      );
  }

  // Get reports by type
  getReportsByType(type: ReportType): Observable<Report[]> {
    return this.http.get<Report[]>(`${this.apiUrl}/type/${type}`)
      .pipe(
        retry(1),
        catchError(this.handleError)
      );
  }

  // Get reports by user
  getReportsByUser(user: string): Observable<Report[]> {
    return this.http.get<Report[]>(`${this.apiUrl}/user/${user}`)
      .pipe(
        retry(1),
        catchError(this.handleError)
      );
  }

  // Get recent reports
  getRecentReports(): Observable<Report[]> {
    return this.http.get<Report[]>(`${this.apiUrl}/recent`)
      .pipe(
        retry(1),
        catchError(this.handleError)
      );
  }

  // Create a new report
  createReport(request: ReportRequest): Observable<Report> {
    return this.http.post<Report>(this.apiUrl, request)
      .pipe(
        catchError(this.handleError)
      );
  }

  // Update report
  updateReport(id: number, request: ReportRequest): Observable<Report> {
    return this.http.put<Report>(`${this.apiUrl}/${id}`, request)
      .pipe(
        catchError(this.handleError)
      );
  }

  // Generate donation report
  generateDonationReport(request: GenerateReportRequest): Observable<Report> {
    const params = new HttpParams()
      .set('start', request.start)
      .set('end', request.end)
      .set('generatedBy', request.generatedBy);

    return this.http.post<Report>(`${this.apiUrl}/generate/donation`, null, { params })
      .pipe(
        catchError(this.handleError)
      );
  }

  // Generate event report
  generateEventReport(request: GenerateReportRequest): Observable<Report> {
    const params = new HttpParams()
      .set('start', request.start)
      .set('end', request.end)
      .set('generatedBy', request.generatedBy);

    return this.http.post<Report>(`${this.apiUrl}/generate/event`, null, { params })
      .pipe(
        catchError(this.handleError)
      );
  }

  // Generate campaign report
  generateCampaignReport(request: GenerateReportRequest): Observable<Report> {
    const params = new HttpParams()
      .set('start', request.start)
      .set('end', request.end)
      .set('generatedBy', request.generatedBy);

    return this.http.post<Report>(`${this.apiUrl}/generate/campaign`, null, { params })
      .pipe(
        catchError(this.handleError)
      );
  }

  // Update report status
  updateReportStatus(id: number, status: ReportStatus): Observable<Report> {
    const params = new HttpParams().set('status', status);
    return this.http.put<Report>(`${this.apiUrl}/${id}/status`, null, { params })
      .pipe(
        catchError(this.handleError)
      );
  }

  // Delete report
  deleteReport(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`)
      .pipe(
        catchError(this.handleError)
      );
  }

  // Get report count by status
  getReportCountByStatus(status: ReportStatus): Observable<number> {
    return this.http.get<number>(`${this.apiUrl}/stats/count/${status}`)
      .pipe(
        retry(1),
        catchError(this.handleError)
      );
  }

  // Health check
  healthCheck(): Observable<string> {
    return this.http.get(`${this.apiUrl}/health`, { responseType: 'text' })
      .pipe(
        catchError(this.handleError)
      );
  }

  // Download PDF
  downloadReportPdf(id: number): Observable<Blob> {
    return this.http.get(`${this.apiUrl}/${id}/download-pdf`, {
      responseType: 'blob'
    }).pipe(
      catchError(this.handleError)
    );
  }

  // Error handling
  private handleError(error: any) {
    let errorMessage = '';
    if (error.error instanceof ErrorEvent) {
      // Client-side error
      errorMessage = `Error: ${error.error.message}`;
    } else {
      // Server-side error
      errorMessage = `Error Code: ${error.status}\nMessage: ${error.message}`;
    }
    console.error(errorMessage);
    return throwError(() => new Error(errorMessage));
  }
}
