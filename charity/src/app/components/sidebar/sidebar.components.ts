import { Component, OnInit } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { KeycloakS } from '../../utils/keycloakService/keycloak-s'; // adapte le chemin

interface KeycloakToken {
  preferred_username?: string;
  name?: string;
  given_name?: string;
  family_name?: string;
}

@Component({
  selector: 'app-sidebar',
  standalone: true,
  templateUrl: './sidebar.components.html',
  imports: [
    RouterOutlet
  ],
  styleUrls: ['./sidebar.components.css']
})
export class SidebarComponent implements OnInit {

  username: string = 'Utilisateur';

  constructor(private keycloakS: KeycloakS) {}

  ngOnInit(): void {
    const token = this.keycloakS.keycloak.tokenParsed as KeycloakToken;

    // 🤵 Récupération du nom (avec fallback si un champs n'existe pas)
    this.username =
      token?.preferred_username ||
      token?.name ||
      `${token?.given_name || ''} ${token?.family_name || ''}`.trim() ||
      'Utilisateur';
  }

  logout() {
    this.keycloakS.logout();
  }
}
