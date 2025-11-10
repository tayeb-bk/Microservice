import { Component, OnInit } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { KeycloakS } from '../../utils/keycloakService/keycloak-s';
import { NotificationService } from '../../services/notification.service';
import { NotificationBadgeComponent } from '../notification-badge/notification-badge';
import { CommonModule } from '@angular/common';

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
  imports: [CommonModule,
    NotificationBadgeComponent,
    RouterOutlet,
  ],
  styleUrls: ['./sidebar.components.css']
})
export class SidebarComponent implements OnInit {

  username: string = 'Utilisateur';
  unreadCount: number = 0; // notifications non lues

  constructor(
    private keycloakS: KeycloakS,
    private notificationService: NotificationService
  ) {}

  ngOnInit(): void {
    // --- Keycloak username ---
    const token = this.keycloakS.keycloak.tokenParsed as KeycloakToken;
    this.username =
      token?.['preferred_username'] ||
      token?.['name'] ||
      `${token?.['given_name'] || ''} ${token?.['family_name'] || ''}`.trim() ||
      'Utilisateur';

    // --- Notifications ---
    this.loadUnreadCount();
  }

  logout() {
    this.keycloakS.logout();
  }

  loadUnreadCount(): void {
    const userId = 123; // remplacer par l'ID réel de l'utilisateur depuis ton auth service
    this.notificationService.getNotificationCount(userId).subscribe({
      next: (count) => {
        this.unreadCount = count;
        console.log('Unread notifications:', count);
      },
      error: (error) => {
        console.error('Error loading notification count:', error);
        this.unreadCount = 0;
      }
    });
  }
}
