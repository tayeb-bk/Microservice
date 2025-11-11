import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, BehaviorSubject, interval } from 'rxjs';
import { switchMap, startWith, tap } from 'rxjs/operators';
import { environment } from '../../environments/environment';

export interface Notification {
  id?: number;
  type: 'SYSTEM' | 'ALERT' | 'MESSAGE' | 'SUCCESS' | 'WARNING' | 'INFO';
  title: string;
  message: string;
  recipientId: number;
  recipientEmail?: string;
  status?: 'PENDING' | 'SENT' | 'READ' | 'FAILED';
  createdAt?: string;
  readAt?: string;
  priority?: 'LOW' | 'MEDIUM' | 'HIGH' | 'URGENT';
}

@Injectable({
  providedIn: 'root'
})
export class NotificationService {
  // Use API Gateway instead of direct service call
  private apiUrl = `${environment.notificationServiceUrl};/api/notifications`;
  private notificationsSubject = new BehaviorSubject<Notification[]>([]);
  public notifications$ = this.notificationsSubject.asObservable();
  private unreadCountSubject = new BehaviorSubject<number>(0);
  public unreadCount$ = this.unreadCountSubject.asObservable();

  constructor(private http: HttpClient) {
    this.startAutoRefresh();
  }

  // Auto-refresh notifications every 30 seconds
  private startAutoRefresh(): void {
    interval(30000).pipe(
      startWith(0),
      switchMap(() => this.getUserNotifications(123)) // Replace with actual user ID
    ).subscribe({
      next: (notifications) => {
        this.notificationsSubject.next(notifications);
        this.updateUnreadCount(notifications);
      },
      error: (error) => {
        console.error('Auto-refresh error:', error);
      }
    });
  }

  private updateUnreadCount(notifications: Notification[]): void {
    const unreadCount = notifications.filter(n => n.status !== 'READ').length;
    this.unreadCountSubject.next(unreadCount);
  }

  // Get all notifications for a user
  getUserNotifications(userId: number): Observable<Notification[]> {
    console.log('Calling API Gateway for user notifications:', userId);
    return this.http.get<Notification[]>(`${this.notoificationServiceUrl}/user/${userId}`).pipe(
      tap(notifications => console.log('Received notifications:', notifications))
    );
  }

  // Get notification by ID
  getNotification(id: number): Observable<Notification> {
    return this.http.get<Notification>(`${this.notoificationServiceUrl}/${id}`);
  }

  // Create new notification
  createNotification(notification: Notification): Observable<Notification> {
    return this.http.post<Notification>(this.notoificationServiceUrl, notification);
  }

  // Mark notification as read
  markAsRead(id: number): Observable<void> {
    return this.http.put<void>(`${this.notoificationServiceUrl}/${id}/read`, {});
  }

  // Mark all as read
  markAllAsRead(userId: number): Observable<void> {
    return this.http.put<void>(`${this.notoificationServiceUrl}/user/${userId}/read-all`, {});
  }

  // Delete notification
  deleteNotification(id: number): Observable<void> {
    return this.http.delete<void>(`${this.notoificationServiceUrl}/${id}`);
  }

  // Delete all notifications
  deleteAllNotifications(userId: number): Observable<void> {
    return this.http.delete<void>(`${this.notoificationServiceUrl}/user/${userId}/all`);
  }

  // Get notification count for user
  getNotificationCount(userId: number): Observable<number> {
    return this.http.get<number>(`${this.notoificationServiceUrl}/user/${userId}/count`);
  }

  // Get unread notifications
  getUnreadNotifications(userId: number): Observable<Notification[]> {
    return this.http.get<Notification[]>(`${this.notoificationServiceUrl}/user/${userId}/unread`);
  }

  // Refresh notifications manually
  refreshNotifications(userId: number): void {
    this.getUserNotifications(userId).subscribe({
      next: (notifications) => {
        this.notificationsSubject.next(notifications);
        this.updateUnreadCount(notifications);
      },
      error: (error) => {
        console.error('Manual refresh error:', error);
      }
    });
  }
}
