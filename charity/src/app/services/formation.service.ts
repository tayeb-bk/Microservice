import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface Formation {
  id: number;
  titre: string;
  description: string;
  formateur: string;
  dureeHeures: number;
  placesMax: number;
  placesRestantes: number;
  dateDebut: string;
  dateFin: string;
  status: string;
  type: string;
  lieu: string;
  prerequis: string;
  publicCible: string;
  competencesAcquises: string[];
  associationId: number;
  dateCreation: string;
  dateModification?: string;
}

export interface FormationToAdd {
  titre: string;
  description: string;
  formateur: string;
  dureeHeures: number;
  placesMax: number;
  dateDebut: string;
  dateFin: string;
  type: string;
  lieu: string;
  prerequis: string;
  publicCible: string;
  competencesAcquises: string[];
  associationId: number;
}

@Injectable({ providedIn: 'root' })
export class FormationService {
  private formationUrl = 'http://localhost:1920/api/formations';

  constructor(private http: HttpClient) {}

  getAll(): Observable<Formation[]> {
    return this.http.get<Formation[]>(this.formationUrl);
  }

  getById(id: number): Observable<Formation> {
    return this.http.get<Formation>(`${this.formationUrl}/${id}`);
  }

  add(formation: FormationToAdd): Observable<Formation> {
    return this.http.post<Formation>(`${this.formationUrl}`, formation);
  }

  update(id: number, formation: Formation): Observable<Formation> {
    return this.http.put<Formation>(`${this.formationUrl}/${id}`, formation);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.formationUrl}/${id}`);
  }

  search(
    titre?: string,
    type?: string,
    status?: string,
    lieu?: string,
    associationId?: number
  ): Observable<Formation[]> {
    let url = `${this.formationUrl}/search`;
    const params: string[] = [];
    if (titre) params.push(`titre=${titre}`);
    if (type) params.push(`type=${type}`);
    if (status) params.push(`status=${status}`);
    if (lieu) params.push(`lieu=${lieu}`);
    if (associationId) params.push(`associationId=${associationId}`);
    if (params.length > 0) url += '?' + params.join('&');
    return this.http.get<Formation[]>(url);
  }
}
