import { Injectable } from '@angular/core';
import Keycloak from 'keycloak-js';
import {environment} from '../../../environments/environment';
@Injectable({
  providedIn: 'root'
})
export class KeycloakS {
  private _keycloak: Keycloak | undefined;

  constructor() {}

  get keycloak() {
    if (!this._keycloak) {
      this._keycloak = new Keycloak({
        url: environment.keycloakUrl,   // ✅ utilise l’environnement
        realm: environment.realm,
        clientId: environment.clientId,
      });
    }
    return this._keycloak;
  }

  async init() {
    await this.keycloak.init({
      onLoad: 'login-required',
      checkLoginIframe: false
    });
  }

  async login() {
    await this.keycloak.login();
  }

  async logout() {
    return this.keycloak.logout({ redirectUri: window.location.origin });
  }

  get token(): string | undefined {
    return this.keycloak.token;
  }
}
