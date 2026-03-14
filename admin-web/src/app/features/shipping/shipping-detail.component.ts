import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterLink, ActivatedRoute } from '@angular/router';
import { ApiService } from '../../core/services/api.service';
import { DetailLayoutComponent } from '../../shared/components/detail-layout/detail-layout.component';
import { ToastService } from '../../shared/components/toast/toast.component';

@Component({
  selector: 'app-shipping-detail',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink, DetailLayoutComponent],
  template: `
    <div class="loading" *ngIf="loading">Loading shipment...</div>
    <div class="error-msg" *ngIf="error">{{ error }}</div>

    <app-detail-layout *ngIf="shipment"
      [title]="'Shipment #' + shipment.id"
      [subtitle]="'Order: ' + shipment.orderId"
      [status]="shipment.stateId ?? ''">

      <!-- Shipment Info -->
      <div class="card">
        <h3>Shipment Information</h3>
        <div class="field-grid">
          <div class="field">
            <label>Order ID</label>
            <span><a [routerLink]="['/orders', shipment.orderId]">{{ shipment.orderId }}</a></span>
          </div>
          <div class="field">
            <label>Carrier</label>
            <span>{{ shipment.carrier ?? '---' }}</span>
          </div>
          <div class="field">
            <label>Tracking Number</label>
            <span class="tracking">{{ shipment.trackingNumber ?? '---' }}</span>
          </div>
          <div class="field" *ngIf="shipment.trackingUrl">
            <label>Tracking URL</label>
            <span><a [href]="shipment.trackingUrl" target="_blank" rel="noopener">Track Package</a></span>
          </div>
        </div>
      </div>

      <!-- Dates -->
      <div class="card">
        <h3>Dates</h3>
        <div class="field-grid">
          <div class="field">
            <label>Shipped At</label>
            <span>{{ shipment.shippedAt ? (shipment.shippedAt | date:'medium') : 'Not shipped yet' }}</span>
          </div>
          <div class="field">
            <label>Estimated Delivery</label>
            <span>{{ shipment.estimatedDelivery ? (shipment.estimatedDelivery | date:'mediumDate') : '---' }}</span>
          </div>
          <div class="field">
            <label>Delivered At</label>
            <span>{{ shipment.deliveredAt ? (shipment.deliveredAt | date:'medium') : 'Not delivered' }}</span>
          </div>
        </div>
      </div>

      <!-- Update Tracking -->
      <div class="card">
        <h3>Update Tracking</h3>
        <form (ngSubmit)="updateTracking()" class="edit-form">
          <div class="form-grid">
            <div class="form-group">
              <label for="carrier">Carrier</label>
              <input id="carrier" [(ngModel)]="trackingModel.carrier" name="carrier">
            </div>
            <div class="form-group">
              <label for="trackingNumber">Tracking Number</label>
              <input id="trackingNumber" [(ngModel)]="trackingModel.trackingNumber" name="trackingNumber">
            </div>
            <div class="form-group full-width">
              <label for="trackingUrl">Tracking URL</label>
              <input id="trackingUrl" [(ngModel)]="trackingModel.trackingUrl" name="trackingUrl"
                placeholder="https://...">
            </div>
          </div>
          <div class="form-actions">
            <button type="submit" class="btn btn-primary" [disabled]="saving">
              {{ saving ? 'Updating...' : 'Update Tracking' }}
            </button>
          </div>
        </form>
      </div>

      <!-- Actions -->
      <div class="card">
        <h3>Actions</h3>
        <div class="action-buttons">
          <button class="btn btn-primary" (click)="triggerEvent('ship')"
            [disabled]="actionLoading">Mark Shipped</button>
          <button class="btn btn-success" (click)="triggerEvent('deliver')"
            [disabled]="actionLoading">Mark Delivered</button>
        </div>
      </div>
    </app-detail-layout>
  `,
  styles: [`
    .loading { padding: 3rem; text-align: center; color: #6b7280; }
    .error-msg { padding: 1.5rem; margin: 1.5rem; background: #fee2e2; color: #991b1b; border-radius: 8px; }
    .card { background: #fff; border: 1px solid #e5e7eb; border-radius: 8px; padding: 1.25rem; }
    .card h3 { margin: 0 0 1rem; font-size: 1rem; color: #1a1a2e; }
    .field-grid { display: grid; grid-template-columns: 1fr 1fr; gap: 1rem; }
    .field label { display: block; font-size: 0.75rem; text-transform: uppercase; color: #6b7280; font-weight: 600; letter-spacing: 0.05em; margin-bottom: 0.2rem; }
    .field span { font-size: 0.95rem; color: #111827; }
    .field a { color: #e94560; text-decoration: none; font-weight: 500; }
    .field a:hover { text-decoration: underline; }
    .tracking { font-family: monospace; font-size: 1rem; letter-spacing: 0.05em; }
    .full-width { grid-column: 1 / -1; }
    .form-grid { display: grid; grid-template-columns: 1fr 1fr; gap: 1rem; margin-bottom: 1rem; }
    .form-group label { display: block; font-size: 0.8rem; font-weight: 600; color: #374151; margin-bottom: 0.3rem; }
    .form-group input {
      width: 100%; padding: 0.5rem 0.75rem; border: 1px solid #d1d5db; border-radius: 6px;
      font-size: 0.9rem; color: #111827; box-sizing: border-box;
    }
    .form-group input:focus { outline: none; border-color: #e94560; box-shadow: 0 0 0 2px rgba(233,69,96,0.15); }
    .form-actions { display: flex; justify-content: flex-end; }
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
export class ShippingDetailComponent implements OnInit {
  shipment: any = null;
  trackingModel: any = { carrier: '', trackingNumber: '', trackingUrl: '' };
  loading = true;
  error = '';
  saving = false;
  actionLoading = false;
  private id = '';

  constructor(
    private route: ActivatedRoute,
    private api: ApiService,
    private toast: ToastService,
  ) {}

  ngOnInit(): void {
    this.id = this.route.snapshot.paramMap.get('id')!;
    this.loadShipment();
  }

  loadShipment(): void {
    this.loading = true;
    this.api.get<any>('shipment', this.id).subscribe({
      next: data => {
        this.shipment = data;
        this.trackingModel = {
          carrier: data.carrier ?? '',
          trackingNumber: data.trackingNumber ?? '',
          trackingUrl: data.trackingUrl ?? '',
        };
        this.loading = false;
      },
      error: () => { this.error = 'Failed to load shipment.'; this.loading = false; },
    });
  }

  updateTracking(): void {
    this.saving = true;
    this.api.update<any>('shipment', this.id, this.trackingModel).subscribe({
      next: data => {
        this.shipment = data;
        this.saving = false;
        this.toast.success('Tracking info updated.');
      },
      error: () => { this.saving = false; this.toast.error('Failed to update tracking.'); },
    });
  }

  triggerEvent(event: string): void {
    this.actionLoading = true;
    this.api.triggerEvent<any>('shipment', this.id, event).subscribe({
      next: data => {
        this.shipment = data;
        this.actionLoading = false;
        this.toast.success(`Shipment ${event} successful.`);
      },
      error: () => {
        this.actionLoading = false;
        this.toast.error(`Failed to ${event} shipment.`);
      },
    });
  }
}
