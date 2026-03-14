import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { OrderService } from '../../core/services/order.service';
import { Order } from '../../shared/models/api.models';

@Component({
  selector: 'app-order-list',
  standalone: true,
  imports: [CommonModule, RouterLink],
  template: `
    <div class="container">
      <h1>My Orders</h1>

      @if (loading) {
        <p class="loading">Loading orders...</p>
      } @else if (orders.length === 0) {
        <div class="empty">
          <p>No orders yet.</p>
          <a routerLink="/products" class="btn-primary">Start Shopping</a>
        </div>
      } @else {
        @for (order of orders; track order.id) {
          <div class="order-card">
            <div class="order-header">
              <div>
                <span class="order-id">Order #{{ order.id | slice:0:8 }}</span>
                <span class="order-date">{{ order.createdAt | date:'mediumDate' }}</span>
              </div>
              <span class="status" [class]="'status-' + order.status.toLowerCase()">
                {{ order.status }}
              </span>
            </div>
            <div class="order-items">
              @for (item of order.items; track item.id) {
                <div class="order-item">
                  <span>{{ item.productName }} x{{ item.quantity }}</span>
                  <span>{{ item.totalPrice.amount | currency:'INR' }}</span>
                </div>
              }
            </div>
            <div class="order-footer">
              <span class="total">Total: {{ order.totalAmount.amount | currency:'INR' }}</span>
              <a [routerLink]="['/orders', order.id]" class="btn-link">View Details</a>
            </div>
          </div>
        }
      }
    </div>
  `,
  styles: [`
    .container { max-width: 800px; margin: 2rem auto; padding: 0 1rem; }
    h1 { color: #1a1a2e; margin-bottom: 1.5rem; }
    .order-card {
      border: 1px solid #e5e7eb;
      border-radius: 12px;
      margin-bottom: 1rem;
      overflow: hidden;
    }
    .order-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      padding: 1rem 1.5rem;
      background: #f9fafb;
    }
    .order-id { font-weight: 600; margin-right: 1rem; }
    .order-date { color: #6b7280; font-size: 0.85rem; }
    .status {
      padding: 0.25rem 0.75rem;
      border-radius: 20px;
      font-size: 0.8rem;
      font-weight: 600;
    }
    .status-created, .status-pending { background: #fef3c7; color: #92400e; }
    .status-confirmed, .status-payment_confirmed { background: #d1fae5; color: #065f46; }
    .status-processing, .status-shipped { background: #dbeafe; color: #1e40af; }
    .status-delivered { background: #d1fae5; color: #065f46; }
    .status-cancelled { background: #fee2e2; color: #991b1b; }
    .order-items { padding: 1rem 1.5rem; }
    .order-item {
      display: flex;
      justify-content: space-between;
      padding: 0.25rem 0;
      font-size: 0.95rem;
    }
    .order-footer {
      display: flex;
      justify-content: space-between;
      align-items: center;
      padding: 1rem 1.5rem;
      border-top: 1px solid #e5e7eb;
    }
    .total { font-weight: 700; }
    .btn-link { color: #1a1a2e; font-weight: 600; text-decoration: none; }
    .btn-link:hover { text-decoration: underline; }
    .loading, .empty { text-align: center; color: #6b7280; padding: 3rem; }
    .btn-primary {
      background: #1a1a2e; color: #fff; padding: 0.75rem 2rem;
      border-radius: 8px; text-decoration: none; font-weight: 600;
    }
  `],
})
export class OrderListComponent implements OnInit {
  orders: Order[] = [];
  loading = true;

  constructor(private orderService: OrderService) {}

  ngOnInit(): void {
    this.orderService.getMyOrders().subscribe({
      next: (res) => {
        this.orders = res.items ?? [];
        this.loading = false;
      },
      error: () => (this.loading = false),
    });
  }
}
