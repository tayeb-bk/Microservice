import { Component } from '@angular/core';
import {RouterOutlet} from '@angular/router';

@Component({
  selector: 'app-sidebar',
  standalone: true,
  templateUrl: './sidebar.components.html',
  imports: [
    RouterOutlet
  ],
  styleUrls: ['./sidebar.components.css']
})
export class SidebarComponent { }
