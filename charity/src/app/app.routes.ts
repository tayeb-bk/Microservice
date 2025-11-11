import { Routes } from '@angular/router';
import { DashboardComponent } from './components/dashboard/dashboard.components';
import { NavbarComponent } from './components/navbar/navbar.components';
import { SidebarComponent } from './components/sidebar/sidebar.components';
import { PostComponent } from './components/posts/post.component';
import { DonationComponent } from './components/donation/donation.component';
import { NotificationListComponent } from './components/notification-list/notification-list';
import { ReportListComponent } from './report/report-list/report-list.component';
import { ReportCreateComponent } from './report/report-create/report-create.component';
import { ReportEditComponent } from './report/report-edit/report-edit.component';
import { ReportDetailComponent } from './report/report-detail/report-detail.component';
import { FormationComponent } from './components/formation/formation.component';
import { EventsListComponent } from './components/events/events-list.component';
import { EventDetailComponent } from './components/events/event-detail.component';
import { EventFormComponent } from './components/events/event-form.component';

export const routes: Routes = [
  {
    path: '',
    component: SidebarComponent,
    children: [
      {
        path: 'dashboard',
        component: DashboardComponent
      },
      {
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
      },
      // Report routes
      {
        path: 'reports',
        children: [
          { path: '', component: ReportListComponent },
          { path: 'create', component: ReportCreateComponent },
          { path: 'edit/:id', component: ReportEditComponent }, // ✅ NEW: Edit route
          { path: ':id', component: ReportDetailComponent }
        ]
      },
      {
        path: 'events',
        children: [
          { path: '', pathMatch: 'full', component: EventsListComponent },
          { path: 'new', component: EventFormComponent },
          { path: ':id', component: EventDetailComponent },
          { path: ':id/edit', component: EventFormComponent }
        ]
      },
      {
        path: 'formation',
        component: FormationComponent
      }
    ]
  }
];
