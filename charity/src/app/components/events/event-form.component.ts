import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { EventService } from '../../services/event.service';
import { Event, EventType } from '../../models/event';

@Component({
  selector: 'app-event-form',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './event-form.component.html',
  styleUrls: ['./event-form.component.css']
})
export class EventFormComponent implements OnInit {
  private route = inject(ActivatedRoute);
  private router = inject(Router);
  private svc = inject(EventService);

  isEdit = false;
  id?: number;
  types: EventType[] = ['DONATION','EDUCATION','ENVIRONMENT','HEALTH','OTHER'];
  model: Event = {
    title: '',
    description: '',
    location: '',
    dateEvent: new Date().toISOString().slice(0,10),
    goalAmount: 0,
    collectedAmount: 0,
    type: 'OTHER'
  };

  ngOnInit(): void {
    const idParam = this.route.snapshot.paramMap.get('id');
    this.isEdit = !!idParam;
    if (this.isEdit) {
      this.id = Number(idParam);
      this.svc.getById(this.id).subscribe(evt => this.model = evt);
    }
  }

  submit() {
    if (this.isEdit && this.id != null) {
      this.svc.update(this.id, this.model).subscribe({
        next: () => { alert('Event updated'); this.router.navigate(['/events']); },
        error: () => alert('Update failed')
      });
    } else {
      this.svc.create(this.model).subscribe({
        next: () => { alert('Event created'); this.router.navigate(['/events']); },
        error: () => alert('Create failed')
      });
    }
  }
}
