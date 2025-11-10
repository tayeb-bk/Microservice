import { Component } from '@angular/core';
import {RouterOutlet} from '@angular/router';
import { OnInit } from '@angular/core';
import { NotificationService } from '../../services/notification.service';
import { NotificationBadgeComponent } from '../notification-badge/notification-badge';

@Component({
  selector: 'app-sidebar',
  standalone: true,
  templateUrl: './sidebar.components.html',
  imports: [NotificationBadgeComponent,
    RouterOutlet,
  ],
  styleUrls: ['./sidebar.components.css']
})
export class SidebarComponent implements OnInit {
  unreadCount: number = 0; // Initialize the property

  constructor(private notificationService: NotificationService) {}

  ngOnInit(): void {
    this.loadUnreadCount();
  }

  loadUnreadCount(): void {
    const userId = 123; // Replace with actual user ID from your auth service

    this.notificationService.getNotificationCount(userId).subscribe({
      next: (count) => {
        this.unreadCount = count;
        console.log('Unread notifications:', count);
      },
      error: (error) => {
        console.error('Error loading notification count:', error);
        // Set to 0 if there's an error
        this.unreadCount = 0;
      }
    });
  }
}
