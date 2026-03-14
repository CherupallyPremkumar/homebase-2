import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterLink, ActivatedRoute } from '@angular/router';
import { ApiService } from '../../core/services/api.service';
import { DetailLayoutComponent } from '../../shared/components/detail-layout/detail-layout.component';
import { ToastService } from '../../shared/components/toast/toast.component';

@Component({
  selector: 'app-order-detail',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink, DetailLayoutComponent],
  template: `
    <div class="loading" *ngIf="loading">Loading order...</div>
    <div class="error-msg" *ngIf="error">{{ error }}</div>

    <app-detail-layout *ngIf="order"
      [title]="'Order #' + order.id"
      [subtitle]="'Placed ' + (order.createdAt | date:'medium')"
      [status]="order.stateId ?? order.status ?? ''">

      <!-- Summary Card -->
      <div class="card">
        <h3>Order Summary</h3>
        <div class="field-grid">
          <div class="field">
            <label>Customer</label>
            <span>{{ order.userId }}</span>
          </div>
          <div class="field">
            <label>Total Amount</label>
            <span class="amount">{{ order.totalAmount?.currency ?? 'INR' }} {{ order.totalAmount?.amount | number:'1.2-2' }}</span>
          </div>
          <div class="field">
            <label>Created</label>
            <span>{{ order.createdAt | date:'medium' }}</span>
          </div>
          <div class="field">
            <label>Last Updated</label>
            <span>{{ order.updatedAt | date:'medium' }}</span>
          </div>
        </div>
      </div>

      <!-- Shipping Address -->
      <div class="card" *ngIf="order.shippingAddress">
        <h3>Shipping Address</h3>
        <div class="address-block">
          <p>{{ order.shippingAddress.name }}</p>
          <p>{{ order.shippingAddress.line1 }}</p>
          <p *ngIf="order.shippingAddress.line2">{{ order.shippingAddress.line2 }}</p>
          <p>{{ order.shippingAddress.city }}, {{ order.shippingAddress.state }} {{ order.shippingAddress.pincode }}</p>
          <p *ngIf="order.shippingAddress.phone">Phone: {{ order.shippingAddress.phone }}</p>
        </div>
      </div>

      <!-- Items -->
      <div class="card">
        <h3>Order Items</h3>
        <table class="items-table">
          <thead>
            <tr>
              <th>Product</th>
              <th>SKU</th>
              <th>Qty</th>
              <th>Unit Price</th>
              <th>Total</th>
              <th>Status</th>
            </tr>
          </thead>
          <tbody>
            @for (item of order.items ?? []; track $index) {
              <tr>
                <td>{{ item.productName ?? item.productId }}</td>
                <td>{{ item.sku ?? '---' }}</td>
                <td>{{ item.quantity }}</td>
                <td>{{ item.unitPrice?.currency ?? 'INR' }} {{ item.unitPrice?.amount ?? item.price | number:'1.2-2' }}</td>
                <td>{{ item.totalPrice?.currency ?? 'INR' }} {{ item.totalPrice?.amount ?? (item.price * item.quantity) | number:'1.2-2' }}</td>
                <td>
                  <span class="status" [ngClass]="'status-' + (item.stateId ?? item.status ?? '').toLowerCase()">
                    {{ item.stateId ?? item.status ?? '---' }}
                  </span>
                </td>
              </tr>
            }
          </tbody>
        </table>
      </div>

      <!-- Timeline -->
      <div class="card" *ngIf="order.activityLog?.length">
        <h3>Timeline</h3>
        <div class="timeline">
          @for (entry of order.activityLog; track $index) {
            <div class="timeline-entry">
              <div class="timeline-dot"></div>
              <div class="timeline-content">
                <strong>{{ entry.event ?? entry.action }}</strong>
                <span class="timeline-date">{{ entry.timestamp | date:'medium' }}</span>
                <p *ngIf="entry.comment" class="timeline-comment">{{ entry.comment }}</p>
              </div>
            </div>
          }
        </div>
      </div>

      <!-- Actions -->
      <div class="card">
        <h3>Actions</h3>
        <div class="action-buttons">
          <button class="btn btn-primary" (click)="triggerEvent('processPayment')"
            [disabled]="actionLoading">Process Payment</button>
          <button class="btn btn-danger" (click)="triggerEvent('cancelOrder')"
            [disabled]="actionLoading">Cancel Order</button>
        </div>
      </div>
    </app-detail-layout>
  `,
  styles: [`
    .loading { padding: 3rem; text-align: center; color: #6b7280; }
    .error-msg { padding: 1.5rem; margin: 1.5rem; background: #fee2e2; color: #991b1b; border-radius: 8px; }
    .card {
      background: #fff; border: 1px solid #e5e7eb; border-radius: 8px; padding: 1.25rem;
    }
    .card h3 { margin: 0 0 1rem; font-size: 1rem; color: #1a1a2e; }
    .field-grid { display: grid; grid-template-columns: 1fr 1fr; gap: 1rem; }
    .field label { display: block; font-size: 0.75rem; text-transform: uppercase; color: #6b7280; font-weight: 600; letter-spacing: 0.05em; margin-bottom: 0.2rem; }
    .field span { font-size: 0.95rem; color: #111827; }
    .amount { font-weight: 700; font-size: 1.1rem; color: #1a1a2e; }
    .address-block p { margin: 0.15rem 0; font-size: 0.9rem; color: #374151; }
    .items-table { width: 100%; border-collapse: collapse; }
    .items-table th { background: #f9fafb; padding: 0.6rem 0.75rem; text-align: left; font-size: 0.75rem; text-transform: uppercase; color: #6b7280; font-weight: 600; }
    .items-table td { padding: 0.6rem 0.75rem; border-top: 1px solid #f3f4f6; font-size: 0.9rem; }
    .status { padding: 0.2rem 0.5rem; border-radius: 4px; font-size: 0.75rem; font-weight: 600; }
    .status-active, .status-approved, .status-paid, .status-delivered, .status-completed { background: #d1fae5; color: #065f46; }
    .status-pending, .status-processing, .status-shipped { background: #fef3c7; color: #92400e; }
    .status-rejected, .status-cancelled, .status-failed, .status-refunded { background: #fee2e2; color: #991b1b; }
    .timeline { display: flex; flex-direction: column; gap: 0; padding-left: 0.5rem; }
    .timeline-entry { display: flex; gap: 0.75rem; padding-bottom: 1rem; position: relative; }
    .timeline-dot {
      width: 10px; height: 10px; border-radius: 50%; background: #e94560;
      flex-shrink: 0; margin-top: 0.3rem; position: relative; z-index: 1;
    }
    .timeline-entry:not(:last-child)::before {
      content: ''; position: absolute; left: calc(0.5rem + 4px); top: 14px;
      bottom: 0; width: 2px; background: #e5e7eb;
    }
    .timeline-content { flex: 1; }
    .timeline-content strong { font-size: 0.9rem; color: #1a1a2e; }
    .timeline-date { display: block; font-size: 0.8rem; color: #9ca3af; margin-top: 0.1rem; }
    .timeline-comment { font-size: 0.85rem; color: #6b7280; margin: 0.25rem 0 0; }
    .action-buttons { display: flex; gap: 0.75rem; flex-wrap: wrap; }
    .btn {
      padding: 0.5rem 1.2rem; border: none; border-radius: 6px;
      font-size: 0.85rem; font-weight: 600; cursor: pointer; transition: opacity 0.15s;
    }
    .btn:disabled { opacity: 0.5; cursor: not-allowed; }
    .btn-primary { background: #e94560; color: #fff; }
    .btn-primary:hover:not(:disabled) { background: #d63851; }
    .btn-danger { background: #991b1b; color: #fff; }
    .btn-danger:hover:not(:disabled) { background: #b91c1c; }
  `],
})
export class OrderDetailComponent implements OnInit {
  order: any = null;
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
    this.loadOrder();
  }

  loadOrder(): void {
    this.loading = true;
    this.api.get<any>('order', this.id).subscribe({
      next: data => { this.order = data; this.loading = false; },
      error: () => { this.error = 'Failed to load order.'; this.loading = false; },
    });
  }

  triggerEvent(event: string): void {
    this.actionLoading = true;
    this.api.triggerEvent<any>('order', this.id, event).subscribe({
      next: data => {
        this.order = data;
        this.actionLoading = false;
        this.toast.success(`Order ${event} successful.`);
      },
      error: () => {
        this.actionLoading = false;
        this.toast.error(`Failed to ${event} order.`);
      },
    });
  }
}
