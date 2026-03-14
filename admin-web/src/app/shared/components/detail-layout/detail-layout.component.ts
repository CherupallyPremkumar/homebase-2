import { Component, Input } from '@angular/core';
import { CommonModule, Location } from '@angular/common';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-detail-layout',
  standalone: true,
  imports: [CommonModule, RouterLink],
  template: `
    <div class="detail-page">
      <div class="detail-header">
        <button class="back-btn" (click)="goBack()">
          <span class="back-arrow">&larr;</span> Back
        </button>
        <div class="title-row">
          <h2>{{ title }}</h2>
          <span *ngIf="status" class="status-badge" [ngClass]="'status-' + status.toLowerCase()">
            {{ status }}
          </span>
        </div>
        <p class="subtitle" *ngIf="subtitle">{{ subtitle }}</p>
      </div>
      <div class="detail-content">
        <ng-content></ng-content>
      </div>
    </div>
  `,
  styles: [`
    .detail-page { padding: 1.5rem; max-width: 1100px; }
    .detail-header { margin-bottom: 1.5rem; }
    .back-btn {
      display: inline-flex; align-items: center; gap: 0.4rem;
      background: none; border: 1px solid #d1d5db; border-radius: 6px;
      padding: 0.4rem 0.9rem; font-size: 0.85rem; color: #4b5563;
      cursor: pointer; margin-bottom: 1rem; transition: all 0.15s;
    }
    .back-btn:hover { background: #f3f4f6; border-color: #9ca3af; }
    .back-arrow { font-size: 1rem; }
    .title-row { display: flex; align-items: center; gap: 0.75rem; flex-wrap: wrap; }
    .title-row h2 { margin: 0; font-size: 1.4rem; color: #1a1a2e; }
    .subtitle { margin: 0.25rem 0 0; font-size: 0.85rem; color: #6b7280; }
    .status-badge {
      padding: 0.25rem 0.7rem; border-radius: 20px; font-size: 0.75rem;
      font-weight: 600; text-transform: uppercase; letter-spacing: 0.03em;
    }
    .status-active, .status-approved, .status-paid, .status-delivered,
    .status-completed, .status-in_stock, .status-stock_approved { background: #d1fae5; color: #065f46; }
    .status-pending, .status-processing, .status-stock_pending,
    .status-checkout_in_progress, .status-in_transit, .status-shipped { background: #fef3c7; color: #92400e; }
    .status-rejected, .status-cancelled, .status-failed,
    .status-stock_rejected, .status-discontinued, .status-expired { background: #fee2e2; color: #991b1b; }
    .status-draft, .status-inactive, .status-suspended { background: #e5e7eb; color: #4b5563; }
    .detail-content { display: flex; flex-direction: column; gap: 1.25rem; }
  `],
})
export class DetailLayoutComponent {
  @Input() title = '';
  @Input() subtitle = '';
  @Input() status = '';

  constructor(private location: Location) {}

  goBack(): void {
    this.location.back();
  }
}
