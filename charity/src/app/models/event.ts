export type EventType = 'DONATION' | 'EDUCATION' | 'ENVIRONMENT' | 'HEALTH' | 'OTHER';

export interface Event {
  idEvent?: number;
  title: string;
  description: string;
  location: string;
  dateEvent: string; // YYYY-MM-DD
  goalAmount: number;
  collectedAmount: number;
  type: EventType;
}
