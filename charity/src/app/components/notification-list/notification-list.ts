import { Component, OnInit, OnDestroy, computed } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { Notification, NotificationService } from '../../services/notification.service';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-notification-list',
  standalone: true,
  imports: [CommonModule, RouterModule, FormsModule],
  templateUrl: './notification-list.html',
  styleUrls: ['./notification-list.css']
})
export class NotificationListComponent implements OnInit, OnDestroy {
  notifications: Notification[] = [];
  filteredNotifications: Notification[] = [];
  loading = false;
  error = '';
  selectedFilter: string = 'all';
  searchTerm: string = '';
  private subscriptions: Subscription[] = [];

  // Computed properties for template bindings
  get hasUnreadNotifications(): boolean {
    return this.notifications.some(n => n.status !== 'READ');
  }

  get unreadCount(): number {
    return this.notifications.filter(n => n.status !== 'READ').length;
  }

  get totalCount(): number {
    return this.notifications.length;
  }

  // Filter options
  filters = [
    { value: 'all', label: 'All Notifications', icon: '📬' },
    { value: 'unread', label: 'Unread', icon: '🔔' },
    { value: 'system', label: 'System', icon: '⚙️' },
    { value: 'alert', label: 'Alerts', icon: '🚨' },
    { value: 'message', label: 'Messages', icon: '💬' },
    { value: 'success', label: 'Success', icon: '✅' }
  ];

  constructor(private notificationService: NotificationService) {}

  ngOnInit(): void {
    this.loadNotifications();

    // Subscribe to real-time updates
    const notificationSub = this.notificationService.notifications$.subscribe(
      notifications => {
        this.notifications = notifications;
        this.applyFilters();
      }
    );

    this.subscriptions.push(notificationSub);
  }

  ngOnDestroy(): void {
    this.subscriptions.forEach(sub => sub.unsubscribe());
  }

  loadNotifications(): void {
    this.loading = true;
    const userId = 123; // Replace with actual user ID

    this.notificationService.getUserNotifications(userId).subscribe({
      next: (data) => {
        this.notifications = data;
        this.applyFilters();
        this.loading = false;
      },
      error: (error) => {
        this.error = 'Failed to load notifications';
        this.loading = false;
        console.error('Error loading notifications:', error);
      }
    });
  }

  applyFilters(): void {
    let filtered = [...this.notifications];

    // Apply status filter
    if (this.selectedFilter === 'unread') {
      filtered = filtered.filter(n => n.status !== 'READ');
    } else if (this.selectedFilter !== 'all') {
      filtered = filtered.filter(n => n.type.toLowerCase() === this.selectedFilter.toLowerCase());
    }

    // Apply search filter
    if (this.searchTerm) {
      const term = this.searchTerm.toLowerCase();
      filtered = filtered.filter(n =>
        n.title.toLowerCase().includes(term) ||
        n.message.toLowerCase().includes(term)
      );
    }

    this.filteredNotifications = filtered;
  }

  onFilterChange(filter: string): void {
    this.selectedFilter = filter;
    this.applyFilters();
  }

  onSearchChange(term: string): void {
    this.searchTerm = term;
    this.applyFilters();
  }

  markAsRead(notification: Notification): void {
    if (notification.id) {
      this.notificationService.markAsRead(notification.id).subscribe({
        next: () => {
          notification.status = 'READ';
          notification.readAt = new Date().toISOString();
          this.applyFilters();
          this.notificationService.refreshNotifications(123);
        },
        error: (error) => {
          console.error('Error marking notification as read:', error);
        }
      });
    }
  }

  markAllAsRead(): void {
    this.notificationService.markAllAsRead(123).subscribe({
      next: () => {
        this.notifications.forEach(n => {
          n.status = 'READ';
          n.readAt = new Date().toISOString();
        });
        this.applyFilters();
        this.notificationService.refreshNotifications(123);
      },
      error: (error) => {
        console.error('Error marking all as read:', error);
      }
    });
  }

  deleteNotification(id: number): void {
    if (confirm('Are you sure you want to delete this notification?')) {
      this.notificationService.deleteNotification(id).subscribe({
        next: () => {
          this.notifications = this.notifications.filter(n => n.id !== id);
          this.applyFilters();
        },
        error: (error) => {
          console.error('Error deleting notification:', error);
        }
      });
    }
  }

  clearAllNotifications(): void {
    if (confirm('Are you sure you want to delete all notifications?')) {
      this.notificationService.deleteAllNotifications(123).subscribe({
        next: () => {
          this.notifications = [];
          this.applyFilters();
        },
        error: (error) => {
          console.error('Error clearing all notifications:', error);
        }
      });
    }
  }

  getNotificationIcon(type: string): string {
    const icons: { [key: string]: string } = {
      'SYSTEM': '⚙️',
      'ALERT': '🚨',
      'MESSAGE': '💬',
      'SUCCESS': '✅',
      'WARNING': '⚠️',
      'INFO': 'ℹ️'
    };
    return icons[type] || '📢';
  }

  getPriorityClass(priority: string = 'MEDIUM'): string {
    return `priority-${priority.toLowerCase()}`;
  }

  formatDate(dateString: string): string {
    const date = new Date(dateString);
    const now = new Date();
    const diff = now.getTime() - date.getTime();

    if (diff < 60000) return 'Just now';
    if (diff < 3600000) return `${Math.floor(diff / 60000)}m ago`;
    if (diff < 86400000) return `${Math.floor(diff / 3600000)}h ago`;
    if (diff < 604800000) return `${Math.floor(diff / 86400000)}d ago`;

    return date.toLocaleDateString();
  }
}
