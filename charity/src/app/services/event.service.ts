import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../environments/environment';
import { Observable } from 'rxjs';
import { Event } from '../models/event';

@Injectable({ providedIn: 'root' })
export class EventService {
  private http = inject(HttpClient);
  private base = `${environment.eventServiceUrl}/api/events`;

  getAll(): Observable<Event[]> { return this.http.get<Event[]>(`${this.base}/all`); }
  getById(id: number): Observable<Event> { return this.http.get<Event>(`${this.base}/${id}`); }
  create(evt: Event): Observable<Event> { return this.http.post<Event>(`${this.base}/add`, evt); }
  update(id: number, evt: Event): Observable<Event> { return this.http.put<Event>(`${this.base}/update/${id}`, evt); }
  remove(id: number): Observable<void> { return this.http.delete<void>(`${this.base}/delete/${id}`); }
}
