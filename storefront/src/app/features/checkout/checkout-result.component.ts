import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, RouterLink } from '@angular/router';

@Component({
  selector: 'app-checkout-result',
  standalone: true,
  imports: [CommonModule, RouterLink],
  template: `
    <div class="container">
      @if (isSuccess) {
        <div class="result-card success">
          <div class="result-icon success-icon">&#10003;</div>
          <h1>Order Confirmed!</h1>
          <p class="result-msg">Thank you for your purchase. Your order has been placed successfully.</p>
          @if (orderId) {
            <p class="order-id">Order ID: <strong>#{{ orderId | slice:0:8 }}</strong></p>
          }
          <p class="result-sub">You will receive a confirmation email shortly with your order details.</p>
          <div class="result-actions">
            @if (orderId) {
              <a [routerLink]="['/orders', orderId]" class="btn-primary">View Order Details</a>
            }
            <a routerLink="/orders" class="btn-secondary">My Orders</a>
            <a routerLink="/products" class="btn-link">Continue Shopping</a>
          </div>
        </div>
      } @else {
        <div class="result-card failure">
          <div class="result-icon failure-icon">&#10007;</div>
          <h1>Payment Failed</h1>
          <p class="result-msg">
            @if (reason === 'cancelled') {
              You cancelled the payment. No charges were made.
            } @else if (reason === 'verification_failed') {
              Payment verification failed. If you were charged, please contact support.
            } @else {
              Something went wrong with your payment. Please try again.
            }
          </p>
          <div class="result-actions">
            <a routerLink="/checkout" class="btn-primary">Try Again</a>
            <a routerLink="/cart" class="btn-secondary">Back to Cart</a>
          </div>
          <p class="support-note">
            Need help? Contact us at support&#64;homebase.com
          </p>
        </div>
      }
    </div>
  `,
  styles: [`
    .container {
      max-width: 600px;
      margin: 3rem auto;
      padding: 0 1rem;
    }
    .result-card {
      text-align: center;
      padding: 3rem 2rem;
      border-radius: 16px;
      border: 1px solid #e5e7eb;
    }
    .result-icon {
      width: 64px;
      height: 64px;
      border-radius: 50%;
      display: flex;
      align-items: center;
      justify-content: center;
      margin: 0 auto 1.5rem;
      font-size: 2rem;
      font-weight: 700;
    }
    .success-icon {
      background: #d1fae5;
      color: #065f46;
    }
    .failure-icon {
      background: #fee2e2;
      color: #991b1b;
    }
    h1 {
      margin: 0 0 0.75rem;
      color: #1a1a2e;
      font-size: 1.75rem;
    }
    .result-msg {
      color: #4b5563;
      font-size: 1.05rem;
      margin-bottom: 0.5rem;
      line-height: 1.6;
    }
    .order-id {
      color: #374151;
      font-size: 1rem;
      margin: 1rem 0 0.5rem;
    }
    .result-sub {
      color: #9ca3af;
      font-size: 0.9rem;
      margin-bottom: 2rem;
    }
    .result-actions {
      display: flex;
      flex-direction: column;
      gap: 0.75rem;
      align-items: center;
    }
    .btn-primary {
      background: #1a1a2e;
      color: #fff;
      padding: 0.75rem 2rem;
      border-radius: 8px;
      text-decoration: none;
      font-weight: 600;
      min-width: 220px;
      text-align: center;
    }
    .btn-secondary {
      background: #f3f4f6;
      color: #374151;
      padding: 0.75rem 2rem;
      border-radius: 8px;
      text-decoration: none;
      font-weight: 500;
      border: 1px solid #d1d5db;
      min-width: 220px;
      text-align: center;
    }
    .btn-link {
      color: #6b7280;
      text-decoration: none;
      font-size: 0.9rem;
      margin-top: 0.5rem;
    }
    .btn-link:hover { color: #1a1a2e; text-decoration: underline; }
    .support-note {
      color: #9ca3af;
      font-size: 0.85rem;
      margin-top: 2rem;
    }
  `],
})
export class CheckoutResultComponent implements OnInit {
  isSuccess = false;
  orderId = '';
  reason = '';

  constructor(private route: ActivatedRoute) {}

  ngOnInit(): void {
    const path = this.route.snapshot.url.map((s) => s.path).join('/');
    this.isSuccess = path.includes('success');
    this.orderId = this.route.snapshot.queryParamMap.get('orderId') || '';
    this.reason = this.route.snapshot.queryParamMap.get('reason') || '';
  }
}
