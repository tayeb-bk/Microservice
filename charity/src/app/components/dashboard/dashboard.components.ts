import { Component } from '@angular/core';
import { NavbarComponent } from '../navbar/navbar.components';
import { SidebarComponent } from '../sidebar/sidebar.components';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  templateUrl: './dashboard.components.html',
  imports: [
    ],
  styleUrls: ['./dashboard.components.css']
})
export class DashboardComponent {

  title = 'Charity Dashboard';
}
