import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { EventService } from '../../services/event.service';
import { Event, EventType } from '../../models/event';

@Component({
  selector: 'app-events-list',
  standalone: true,
  imports: [CommonModule, RouterLink, FormsModule],
  templateUrl: './events-list.component.html',
  styleUrls: ['./events-list.component.css']
})
export class EventsListComponent implements OnInit {
  private svc = inject(EventService);
  private router = inject(Router);

  events: Event[] = [];
  loading = false;
  searchTerm = '';
  filterType: '' | EventType = '';
  dateFrom: string = '';
  dateTo: string = '';
  sortKey: 'title' | 'type' | 'dateEvent' | 'goalAmount' | 'collectedAmount' | 'progress' = 'dateEvent';
  sortDir: 'asc' | 'desc' = 'desc';

  // Advanced options
  showTitle = true;
  showType = true;
  showDate = true;
  showGoal = true;
  showCollected = true;
  showProgress = true;
  page = 1;
  pageSize = 10;
  pageSizes = [10, 25, 50, 100];
  private settingsKey = 'eventsListSettings';

  ngOnInit(): void {
    this.loadSettings();
    this.load();
  }

  load() {
    this.loading = true;
    this.svc.getAll().subscribe({
      next: res => { this.events = res; this.loading = false; },
      error: () => { this.loading = false; }
    });
  }

  goNew() { this.router.navigate(['/events/new']); }

  remove(e: Event) {
    if (!e.idEvent) return;
    if (!confirm('Delete this event?')) return;
    this.svc.remove(e.idEvent).subscribe({
      next: () => { this.load(); alert('Event deleted'); },
      error: () => alert('Delete failed')
    });
  }

  get filtered(): Event[] {
    const q = this.searchTerm.trim().toLowerCase();
    if (!q) return this.events;
    return this.events.filter(e => (e.title || '').toLowerCase().includes(q));
  }

  progress(e: Event): number {
    const goal = Number(e.goalAmount) || 0;
    const collected = Number(e.collectedAmount) || 0;
    if (goal <= 0) return 0;
    const p = Math.floor((collected / goal) * 100);
    return Math.max(0, Math.min(100, p));
  }

  private inDateRange(e: Event): boolean {
    if (!this.dateFrom && !this.dateTo) return true;
    const d = new Date(e.dateEvent as any);
    if (this.dateFrom) {
      const from = new Date(this.dateFrom);
      if (d < from) return false;
    }
    if (this.dateTo) {
      const to = new Date(this.dateTo);
      if (d > to) return false;
    }
    return true;
  }

  get view(): Event[] {
    const bySearch = this.filtered;
    const byType = this.filterType ? bySearch.filter(e => e.type === this.filterType) : bySearch;
    const byDate = byType.filter(e => this.inDateRange(e));
    const key = this.sortKey; const dir = this.sortDir === 'asc' ? 1 : -1;
    const val = (e: Event) => {
      switch (key) {
        case 'title': return (e.title || '').toLowerCase();
        case 'type': return (e.type || '').toString();
        case 'dateEvent': return new Date(e.dateEvent as any).getTime();
        case 'goalAmount': return Number(e.goalAmount) || 0;
        case 'collectedAmount': return Number(e.collectedAmount) || 0;
        case 'progress': return this.progress(e);
      }
    };
    return [...byDate].sort((a,b) => {
      const va = val(a); const vb = val(b);
      if (va < vb) return -1 * dir;
      if (va > vb) return 1 * dir;
      return 0;
    });
  }

  // Pagination helpers based on current view
  get pageCount(): number { return Math.max(1, Math.ceil(this.view.length / this.pageSize) || 1); }
  get paged(): Event[] {
    const start = (this.page - 1) * this.pageSize;
    return this.view.slice(start, start + this.pageSize);
  }
  goPage(n: number) { this.page = Math.min(this.pageCount, Math.max(1, n)); this.saveSettings(); }
  prevPage() { this.goPage(this.page - 1); }
  nextPage() { this.goPage(this.page + 1); }

  onSort(key: typeof this.sortKey) {
    if (this.sortKey === key) {
      this.sortDir = this.sortDir === 'asc' ? 'desc' : 'asc';
    } else {
      this.sortKey = key;
      this.sortDir = key === 'title' ? 'asc' : 'desc';
    }
    this.saveSettings();
  }

  // Stats on current view
  get totalEvents(): number { return this.view.length; }
  get totalGoal(): number { return this.view.reduce((s, e) => s + (Number(e.goalAmount) || 0), 0); }
  get totalCollected(): number { return this.view.reduce((s, e) => s + (Number(e.collectedAmount) || 0), 0); }
  get avgProgress(): number {
    if (!this.view.length) return 0;
    const sum = this.view.reduce((s, e) => s + this.progress(e), 0);
    return Math.round(sum / this.view.length);
  }
  get achievedCount(): number { return this.view.filter(e => (Number(e.goalAmount) > 0) && (Number(e.collectedAmount) >= Number(e.goalAmount))).length; }

  // Available types for filter (merge known list with those present in data)
  get types(): EventType[] {
    const base: EventType[] = ['DONATION','EDUCATION','ENVIRONMENT','HEALTH','OTHER'];
    const found = Array.from(new Set(this.events.map(e => e.type).filter(Boolean))) as EventType[];
    return Array.from(new Set([...(base as string[]), ...(found as string[])])) as EventType[];
  }

  exportCsv() {
    const rows = [
      ['Titre','Type','Date','Lieu','Objectif','Collecté','Progression'] as const,
      ...this.view.map(e => [
        e.title ?? '',
        String(e.type ?? ''),
        String(e.dateEvent ?? ''),
        e.location ?? '',
        String(e.goalAmount ?? ''),
        String(e.collectedAmount ?? ''),
        `${this.progress(e)}%`
      ])
    ];
    const csv = rows.map(r => r.map(v => {
      const s = String(v ?? '');
      return /[",\n]/.test(s) ? '"' + s.replace(/"/g,'""') + '"' : s;
    }).join(',')).join('\n');
    const blob = new Blob([csv], { type: 'text/csv;charset=utf-8;' });
    const url = URL.createObjectURL(blob);
    const a = document.createElement('a');
    a.href = url;
    a.download = 'events.csv';
    a.click();
    URL.revokeObjectURL(url);
  }

  // Persistence
  saveSettings() {
    const data = {
      searchTerm: this.searchTerm,
      filterType: this.filterType,
      dateFrom: this.dateFrom,
      dateTo: this.dateTo,
      sortKey: this.sortKey,
      sortDir: this.sortDir,
      showTitle: this.showTitle,
      showType: this.showType,
      showDate: this.showDate,
      showGoal: this.showGoal,
      showCollected: this.showCollected,
      showProgress: this.showProgress,
      page: this.page,
      pageSize: this.pageSize
    };
    try { localStorage.setItem(this.settingsKey, JSON.stringify(data)); } catch {}
  }

  loadSettings() {
    try {
      const raw = localStorage.getItem(this.settingsKey);
      if (!raw) return;
      const s = JSON.parse(raw);
      this.searchTerm = s.searchTerm ?? this.searchTerm;
      this.filterType = s.filterType ?? this.filterType;
      this.dateFrom = s.dateFrom ?? this.dateFrom;
      this.dateTo = s.dateTo ?? this.dateTo;
      this.sortKey = s.sortKey ?? this.sortKey;
      this.sortDir = s.sortDir ?? this.sortDir;
      this.showTitle = s.showTitle ?? this.showTitle;
      this.showType = s.showType ?? this.showType;
      this.showDate = s.showDate ?? this.showDate;
      this.showGoal = s.showGoal ?? this.showGoal;
      this.showCollected = s.showCollected ?? this.showCollected;
      this.showProgress = s.showProgress ?? this.showProgress;
      this.page = s.page ?? this.page;
      this.pageSize = s.pageSize ?? this.pageSize;
    } catch {}
  }
}
