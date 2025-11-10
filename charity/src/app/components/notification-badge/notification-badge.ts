import { Component, OnInit, OnDestroy, HostListener } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { Notification, NotificationService } from '../../services/notification.service';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-notification-badge',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './notification-badge.html',
  styleUrls: ['./notification-badge.css']
})
export class NotificationBadgeComponent implements OnInit, OnDestroy {
  unreadCount = 0;
  recentNotifications: Notification[] = [];
  showDropdown = false;
  isLoading = false;
  hasNewNotifications = false;
  private subscriptions: Subscription[] = [];

  // Animation states
  badgeState: 'idle' | 'pulse' | 'bounce' = 'idle';
  dropdownState: 'hidden' | 'showing' | 'hiding' = 'hidden';

  constructor(private notificationService: NotificationService) {}

  ngOnInit(): void {
    this.loadNotifications();

    // Subscribe to real-time updates
    const notificationSub = this.notificationService.notifications$.subscribe(
      notifications => {
        this.handleNewNotifications(notifications);
      }
    );

    const countSub = this.notificationService.unreadCount$.subscribe(
      count => {
        this.handleCountUpdate(count);
      }
    );

    this.subscriptions.push(notificationSub, countSub);
  }

  ngOnDestroy(): void {
    this.subscriptions.forEach(sub => sub.unsubscribe());
  }

  private handleNewNotifications(notifications: Notification[]): void {
    const previousCount = this.unreadCount;
    const newUnreadCount = notifications.filter(n => n.status !== 'READ').length;

    this.recentNotifications = notifications
      .filter(n => n.status !== 'READ')
      .slice(0, 5); // Show only 5 most recent

    if (newUnreadCount > previousCount) {
      this.triggerNewNotificationAnimation();
    }

    this.unreadCount = newUnreadCount;
  }

  private handleCountUpdate(count: number): void {
    if (count > this.unreadCount) {
      this.triggerNewNotificationAnimation();
    }
    this.unreadCount = count;
  }

  private triggerNewNotificationAnimation(): void {
    this.hasNewNotifications = true;
    this.badgeState = 'pulse';

    // Reset animation after 2 seconds
    setTimeout(() => {
      this.badgeState = 'idle';
    }, 2000);

    // Auto-hide new notification indicator after 5 seconds
    setTimeout(() => {
      this.hasNewNotifications = false;
    }, 5000);
  }

  @HostListener('document:click', ['$event'])
  onDocumentClick(event: Event): void {
    const target = event.target as HTMLElement;
    if (!target.closest('.notification-badge-container')) {
      this.hideDropdown();
    }
  }

  toggleDropdown(): void {
    if (this.showDropdown) {
      this.hideDropdown();
    } else {
      this.showDropdownWithAnimation();
    }
  }

  private showDropdownWithAnimation(): void {
    this.showDropdown = true;
    this.dropdownState = 'showing';

    // Load fresh data when opening
    this.loadNotifications();
  }

  private hideDropdown(): void {
    if (this.showDropdown) {
      this.dropdownState = 'hiding';

      // Wait for animation to complete before hiding
      setTimeout(() => {
        this.showDropdown = false;
        this.dropdownState = 'hidden';
      }, 300);
    }
  }

  loadNotifications(): void {
    this.isLoading = true;
    const userId = 123; // Replace with actual user ID

    this.notificationService.getUnreadNotifications(userId).subscribe({
      next: (notifications) => {
        this.recentNotifications = notifications.slice(0, 5);
        this.unreadCount = notifications.length;
        this.isLoading = false;
      },
      error: (error) => {
        console.error('Error loading notifications:', error);
        this.isLoading = false;
      }
    });
  }

  markAsRead(notification: Notification, event?: Event): void {
    if (event) {
      event.stopPropagation();
    }

    if (notification.id) {
      this.notificationService.markAsRead(notification.id).subscribe({
        next: () => {
          notification.status = 'READ';
          this.unreadCount = Math.max(0, this.unreadCount - 1);
          this.recentNotifications = this.recentNotifications.filter(n => n.id !== notification.id);

          // Trigger success animation
          this.badgeState = 'bounce';
          setTimeout(() => this.badgeState = 'idle', 600);
        },
        error: (error) => {
          console.error('Error marking notification as read:', error);
        }
      });
    }
  }

  markAllAsRead(event: Event): void {
    event.stopPropagation();

    this.notificationService.markAllAsRead(123).subscribe({
      next: () => {
        this.recentNotifications = [];
        this.unreadCount = 0;
        this.badgeState = 'bounce';
        setTimeout(() => this.badgeState = 'idle', 600);
      },
      error: (error) => {
        console.error('Error marking all as read:', error);
      }
    });
  }

  getNotificationIcon(type: string): string {
    const icons: { [key: string]: string } = {
      'SYSTEM': '⚙️',
      'ALERT': '🚨',
      'MESSAGE': '💬',
      'SUCCESS': '✅',
      'WARNING': '⚠️',
      'INFO': 'ℹ️',
      'URGENT': '🔴',
      'DONATION': '💰',
      'EVENT': '📅'
    };
    return icons[type] || '📢';
  }

  getNotificationClass(type: string): string {
    return `notification-type-${type.toLowerCase()}`;
  }

  formatTime(dateString: string): string {
    const date = new Date(dateString);
    const now = new Date();
    const diff = now.getTime() - date.getTime();

    if (diff < 60000) return 'Just now';
    if (diff < 3600000) return `${Math.floor(diff / 60000)}m ago`;
    if (diff < 86400000) return `${Math.floor(diff / 3600000)}h ago`;
    return date.toLocaleDateString();
  }

  // Prevent dropdown close when clicking inside
  onDropdownClick(event: Event): void {
    event.stopPropagation();
  }
}
