import { Routes } from '@angular/router';
import { DashboardComponent } from './components/dashboard/dashboard.components';
import {NavbarComponent} from './components/navbar/navbar.components';
import {SidebarComponent} from './components/sidebar/sidebar.components';
import {PostComponent} from './components/posts/post.component';
import {DonationComponent} from './components/donation/donation.component';
import { NotificationListComponent } from './components/notification-list/notification-list';

export const routes: Routes = [
  {
    path: '',
    component: SidebarComponent,
    children: [
      {
        path: 'dashboard',
        component: DashboardComponent
      },{
        path: 'Post',
        component: PostComponent
      },
      {
        path: 'notifications',
        component: NotificationListComponent
      },
      {
        path: 'Don',
        component: DonationComponent
      }
    ]
  }
];
