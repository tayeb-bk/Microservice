// user.service.ts
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({ providedIn: 'root' })
export class UserService {
  constructor(private http: HttpClient) {}

  syncCurrentUser() {
    return this.http.get('/api/users/me'); // doit envoyer le JWT automatiquement via l'interceptor
  }
}
