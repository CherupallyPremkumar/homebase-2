import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterLink, ActivatedRoute } from '@angular/router';
import { ApiService } from '../../core/services/api.service';
import { DetailLayoutComponent } from '../../shared/components/detail-layout/detail-layout.component';
import { ToastService } from '../../shared/components/toast/toast.component';

@Component({
  selector: 'app-settlement-detail',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink, DetailLayoutComponent],
  template: `
    <div class="loading" *ngIf="loading">Loading settlement...</div>
    <div class="error-msg" *ngIf="error">{{ error }}</div>

    <app-detail-layout *ngIf="settlement"
      [title]="'Settlement #' + settlement.id"
      [subtitle]="'Period: ' + settlement.periodMonth + '/' + settlement.periodYear"
      [status]="settlement.stateId ?? ''">

      <!-- Settlement Info -->
      <div class="card">
        <h3>Settlement Details</h3>
        <div class="field-grid">
          <div class="field">
            <label>Supplier ID</label>
            <span><a [routerLink]="['/suppliers', settlement.supplierId]">{{ settlement.supplierId }}</a></span>
          </div>
          <div class="field">
            <label>Period</label>
            <span>{{ settlement.periodMonth }}/{{ settlement.periodYear }}</span>
          </div>
          <div class="field">
            <label>Currency</label>
            <span>{{ settlement.currency ?? 'INR' }}</span>
          </div>
        </div>
      </div>

      <!-- Financial Summary -->
      <div class="card">
        <h3>Financial Summary</h3>
        <div class="money-grid">
          <div class="money-card">
            <span class="money-label">Total Sales</span>
            <span class="money-value">{{ settlement.currency ?? 'INR' }} {{ settlement.totalSalesAmount | number:'1.2-2' }}</span>
          </div>
          <div class="money-card">
            <span class="money-label">Commission</span>
            <span class="money-value commission">{{ settlement.currency ?? 'INR' }} {{ settlement.commissionAmount | number:'1.2-2' }}</span>
          </div>
          <div class="money-card highlight">
            <span class="money-label">Net Payout</span>
            <span class="money-value payout">{{ settlement.currency ?? 'INR' }} {{ settlement.netPayoutAmount | number:'1.2-2' }}</span>
          </div>
        </div>
      </div>

      <!-- Actions -->
      <div class="card">
        <h3>Actions</h3>
        <div class="action-buttons">
          <button class="btn btn-success" (click)="triggerEvent('approvePayout')"
            [disabled]="actionLoading">Approve Payout</button>
          <button class="btn btn-primary" (click)="triggerEvent('markPaid')"
            [disabled]="actionLoading">Mark Paid</button>
        </div>
      </div>
    </app-detail-layout>
  `,
  styles: [`
    .loading { padding: 3rem; text-align: center; color: #6b7280; }
    .error-msg { padding: 1.5rem; margin: 1.5rem; background: #fee2e2; color: #991b1b; border-radius: 8px; }
    .card { background: #fff; border: 1px solid #e5e7eb; border-radius: 8px; padding: 1.25rem; }
    .card h3 { margin: 0 0 1rem; font-size: 1rem; color: #1a1a2e; }
    .field-grid { display: grid; grid-template-columns: 1fr 1fr 1fr; gap: 1rem; }
    .field label { display: block; font-size: 0.75rem; text-transform: uppercase; color: #6b7280; font-weight: 600; letter-spacing: 0.05em; margin-bottom: 0.2rem; }
    .field span { font-size: 0.95rem; color: #111827; }
    .field a { color: #e94560; text-decoration: none; font-weight: 500; }
    .field a:hover { text-decoration: underline; }
    .money-grid { display: grid; grid-template-columns: 1fr 1fr 1fr; gap: 1rem; }
    .money-card {
      background: #f9fafb; border: 1px solid #e5e7eb; border-radius: 8px;
      padding: 1rem; text-align: center;
    }
    .money-card.highlight { background: #1a1a2e; border-color: #1a1a2e; }
    .money-card.highlight .money-label { color: #9ca3af; }
    .money-card.highlight .money-value { color: #fff; }
    .money-label { display: block; font-size: 0.75rem; text-transform: uppercase; color: #6b7280; font-weight: 600; margin-bottom: 0.3rem; }
    .money-value { display: block; font-size: 1.25rem; font-weight: 700; color: #1a1a2e; }
    .commission { color: #e94560; }
    .payout { color: #10b981; }
    .action-buttons { display: flex; gap: 0.75rem; flex-wrap: wrap; }
    .btn {
      padding: 0.5rem 1.2rem; border: none; border-radius: 6px;
      font-size: 0.85rem; font-weight: 600; cursor: pointer; transition: opacity 0.15s;
    }
    .btn:disabled { opacity: 0.5; cursor: not-allowed; }
    .btn-primary { background: #e94560; color: #fff; }
    .btn-primary:hover:not(:disabled) { background: #d63851; }
    .btn-success { background: #065f46; color: #fff; }
    .btn-success:hover:not(:disabled) { background: #047857; }
  `],
})
export class SettlementDetailComponent implements OnInit {
  settlement: any = null;
  loading = true;
  error = '';
  actionLoading = false;
  private id = '';

  constructor(
    private route: ActivatedRoute,
    private api: ApiService,
    private toast: ToastService,
  ) {}

  ngOnInit(): void {
    this.id = this.route.snapshot.paramMap.get('id')!;
    this.loadSettlement();
  }

  loadSettlement(): void {
    this.loading = true;
    this.api.get<any>('settlement', this.id).subscribe({
      next: data => { this.settlement = data; this.loading = false; },
      error: () => { this.error = 'Failed to load settlement.'; this.loading = false; },
    });
  }

  triggerEvent(event: string): void {
    this.actionLoading = true;
    this.api.triggerEvent<any>('settlement', this.id, event).subscribe({
      next: data => {
        this.settlement = data;
        this.actionLoading = false;
        this.toast.success(`Settlement ${event} successful.`);
      },
      error: () => {
        this.actionLoading = false;
        this.toast.error(`Failed to ${event} settlement.`);
      },
    });
  }
}
