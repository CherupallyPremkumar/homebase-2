import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { OrderService } from '../../core/services/order.service';
import { ToastService } from '../../shared/components/toast/toast.component';
import { SpinnerComponent } from '../../shared/components/spinner/spinner.component';
import { Order, OrderItem } from '../../shared/models/api.models';

const STATUS_STEPS = ['CREATED', 'PAYMENT_CONFIRMED', 'PROCESSING', 'SHIPPED', 'DELIVERED'];

@Component({
  selector: 'app-order-detail',
  standalone: true,
  imports: [CommonModule, RouterLink, FormsModule, SpinnerComponent],
  template: `
    <div class="container">
      <a routerLink="/orders" class="back-link">&larr; Back to Orders</a>

      @if (loading) {
        <app-spinner />
      } @else if (order) {
        <div class="order-detail">
          <div class="order-header">
            <div>
              <h1>Order #{{ order.id | slice:0:8 }}</h1>
              @if (order.createdAt) {
                <p class="order-date">Placed on {{ order.createdAt | date:'mediumDate' }}</p>
              }
            </div>
            <span class="status" [class]="'status-' + order.status.toLowerCase()">
              {{ formatStatus(order.status) }}
            </span>
          </div>

          <!-- Status Timeline -->
          @if (!isCancelled) {
            <div class="timeline">
              @for (step of statusSteps; track step; let i = $index) {
                <div class="timeline-step" [class.completed]="getStepIndex(order!.status) >= i" [class.active]="getStepIndex(order!.status) === i">
                  <div class="step-dot">
                    @if (getStepIndex(order!.status) > i) {
                      <span class="check">&#10003;</span>
                    }
                  </div>
                  <span class="step-label">{{ formatStatus(step) }}</span>
                </div>
                @if (i < statusSteps.length - 1) {
                  <div class="timeline-line" [class.completed]="getStepIndex(order!.status) > i"></div>
                }
              }
            </div>
          }

          <!-- Shipping Address -->
          @if (order.shippingAddress) {
            <div class="section">
              <h2>Shipping Address</h2>
              <div class="address-card">
                @if (order.shippingAddress.label) {
                  <p class="address-name">{{ order.shippingAddress.label }}</p>
                }
                <p>{{ order.shippingAddress.line1 }}</p>
                @if (order.shippingAddress.line2) {
                  <p>{{ order.shippingAddress.line2 }}</p>
                }
                <p>{{ order.shippingAddress.city }}{{ order.shippingAddress.state ? ', ' + order.shippingAddress.state : '' }} {{ order.shippingAddress.postalCode }}</p>
                @if (order.shippingAddress.phone) {
                  <p class="address-phone">Phone: {{ order.shippingAddress.phone }}</p>
                }
              </div>
            </div>
          }

          <!-- Tracking Info -->
          @if (order.status === 'SHIPPED' || order.status === 'DELIVERED') {
            <div class="section tracking-section">
              <h2>Tracking Information</h2>
              <div class="tracking-card">
                <p>Your order has been {{ order.status === 'SHIPPED' ? 'shipped' : 'delivered' }}.</p>
                @if (order.status === 'SHIPPED') {
                  <p class="tracking-note">You will receive tracking details via email.</p>
                }
              </div>
            </div>
          }

          <!-- Items -->
          <div class="section">
            <h2>Items ({{ order.items.length }})</h2>
            @for (item of order.items; track item.id) {
              <div class="item-row">
                <div class="item-image-placeholder"></div>
                <div class="item-details">
                  <strong>{{ item.productName }}</strong>
                  <p class="item-meta">
                    Qty: {{ item.quantity }}
                    &middot;
                    Unit: {{ item.unitPrice.amount | currency:'INR' }}
                    &middot;
                    Status: <span class="item-status" [class]="'istatus-' + item.status.toLowerCase()">{{ formatStatus(item.status) }}</span>
                  </p>
                </div>
                <div class="item-right">
                  <span class="item-price">{{ item.totalPrice.amount | currency:'INR' }}</span>
                  @if (canRequestReturn(item)) {
                    <button class="btn-return" (click)="openReturnModal(item)">Request Return</button>
                  }
                </div>
              </div>
            }
          </div>

          <!-- Totals -->
          <div class="totals">
            <div class="total-row">
              <span>Subtotal</span>
              <span>{{ order.totalAmount.amount | currency:'INR' }}</span>
            </div>
            @if (order.taxAmount) {
              <div class="total-row">
                <span>Tax</span>
                <span>{{ order.taxAmount.amount | currency:'INR' }}</span>
              </div>
            }
            @if (order.shippingAmount) {
              <div class="total-row">
                <span>Shipping</span>
                <span>{{ order.shippingAmount.amount | currency:'INR' }}</span>
              </div>
            }
            @if (order.discountAmount && order.discountAmount > 0) {
              <div class="total-row discount">
                <span>Discount</span>
                <span>-{{ order.discountAmount | currency:'INR' }}</span>
              </div>
            }
            <hr />
            <div class="total-row total">
              <span>Total</span>
              <span>{{ order.totalAmount.amount | currency:'INR' }}</span>
            </div>
          </div>

          @if (order.status === 'CREATED' || order.status === 'PENDING') {
            <button class="btn-cancel" (click)="cancelOrder()" [disabled]="cancelling">
              {{ cancelling ? 'Cancelling...' : 'Cancel Order' }}
            </button>
          }
        </div>

        <!-- Return Modal -->
        @if (returnItem) {
          <div class="modal-overlay" (click)="closeReturnModal()">
            <div class="modal" (click)="$event.stopPropagation()">
              <h3>Request Return</h3>
              <p>Item: <strong>{{ returnItem.productName }}</strong></p>
              <div class="form-group">
                <label>Quantity to return</label>
                <input type="number" [(ngModel)]="returnQuantity" [max]="returnItem.quantity" min="1" />
              </div>
              <div class="form-group">
                <label>Reason for return</label>
                <textarea [(ngModel)]="returnReason" rows="3" placeholder="Please describe why you want to return this item"></textarea>
              </div>
              <div class="modal-actions">
                <button class="btn-secondary" (click)="closeReturnModal()">Cancel</button>
                <button class="btn-primary" (click)="submitReturn()" [disabled]="submittingReturn || !returnReason">
                  {{ submittingReturn ? 'Submitting...' : 'Submit Return' }}
                </button>
              </div>
            </div>
          </div>
        }
      } @else {
        <div class="not-found">
          <h2>Order Not Found</h2>
          <p>This order does not exist or you do not have access to it.</p>
          <a routerLink="/orders" class="btn-primary-link">View My Orders</a>
        </div>
      }
    </div>
  `,
  styles: [`
    .container { max-width: 900px; margin: 2rem auto; padding: 0 1rem; }
    .back-link { color: #6b7280; text-decoration: none; font-size: 0.9rem; }
    .back-link:hover { color: #1a1a2e; }
    .order-header { display: flex; justify-content: space-between; align-items: flex-start; margin: 1.5rem 0; }
    .order-header h1 { margin: 0; color: #1a1a2e; }
    .order-date { margin: 0.25rem 0 0; color: #6b7280; font-size: 0.9rem; }
    .status {
      padding: 0.3rem 0.85rem; border-radius: 20px;
      font-size: 0.85rem; font-weight: 600; white-space: nowrap;
    }
    .status-created, .status-pending { background: #fef3c7; color: #92400e; }
    .status-payment_confirmed, .status-confirmed, .status-processing { background: #dbeafe; color: #1e40af; }
    .status-shipped { background: #e0e7ff; color: #3730a3; }
    .status-delivered { background: #d1fae5; color: #065f46; }
    .status-cancelled { background: #fee2e2; color: #991b1b; }

    /* Timeline */
    .timeline {
      display: flex;
      align-items: center;
      margin: 1.5rem 0 2rem;
      padding: 1.5rem;
      background: #f9fafb;
      border-radius: 12px;
    }
    .timeline-step {
      display: flex;
      flex-direction: column;
      align-items: center;
      gap: 0.5rem;
      flex-shrink: 0;
    }
    .step-dot {
      width: 28px;
      height: 28px;
      border-radius: 50%;
      border: 2px solid #d1d5db;
      background: #fff;
      display: flex;
      align-items: center;
      justify-content: center;
      font-size: 0.75rem;
    }
    .timeline-step.completed .step-dot { background: #059669; border-color: #059669; color: #fff; }
    .timeline-step.active .step-dot { border-color: #1a1a2e; background: #1a1a2e; color: #fff; }
    .step-label { font-size: 0.7rem; color: #6b7280; text-align: center; max-width: 80px; }
    .timeline-step.completed .step-label, .timeline-step.active .step-label { color: #1a1a2e; font-weight: 600; }
    .check { font-weight: 700; }
    .timeline-line { flex: 1; height: 2px; background: #d1d5db; margin: 0 0.25rem; margin-bottom: 1.5rem; }
    .timeline-line.completed { background: #059669; }

    /* Sections */
    .section { margin-bottom: 2rem; }
    .section h2 { font-size: 1.1rem; margin-bottom: 1rem; color: #374151; }

    /* Address */
    .address-card {
      background: #f9fafb;
      border: 1px solid #e5e7eb;
      border-radius: 8px;
      padding: 1rem 1.25rem;
    }
    .address-card p { margin: 0.15rem 0; color: #374151; font-size: 0.95rem; }
    .address-name { font-weight: 600; }
    .address-phone { color: #6b7280 !important; margin-top: 0.5rem !important; font-size: 0.85rem !important; }

    /* Tracking */
    .tracking-card {
      background: #eff6ff;
      border: 1px solid #bfdbfe;
      border-radius: 8px;
      padding: 1rem 1.25rem;
    }
    .tracking-card p { margin: 0.15rem 0; color: #1e40af; font-size: 0.95rem; }
    .tracking-note { font-size: 0.85rem !important; color: #6b7280 !important; }

    /* Items */
    .item-row {
      display: flex;
      gap: 1rem;
      align-items: center;
      padding: 0.75rem 0;
      border-bottom: 1px solid #f3f4f6;
    }
    .item-image-placeholder {
      width: 56px;
      height: 56px;
      background: #f3f4f6;
      border-radius: 8px;
      flex-shrink: 0;
    }
    .item-details { flex: 1; }
    .item-details strong { font-size: 0.95rem; color: #1a1a2e; }
    .item-meta { margin: 0.25rem 0 0; color: #6b7280; font-size: 0.85rem; }
    .item-status { font-weight: 500; }
    .istatus-delivered { color: #059669; }
    .istatus-shipped { color: #3730a3; }
    .istatus-cancelled { color: #991b1b; }
    .item-right { text-align: right; display: flex; flex-direction: column; gap: 0.5rem; align-items: flex-end; }
    .item-price { font-weight: 600; }
    .btn-return {
      background: none;
      border: 1px solid #6b7280;
      color: #374151;
      padding: 0.3rem 0.75rem;
      border-radius: 6px;
      cursor: pointer;
      font-size: 0.8rem;
    }
    .btn-return:hover { background: #f3f4f6; }

    /* Totals */
    .totals { margin-top: 1.5rem; max-width: 350px; margin-left: auto; }
    .total-row { display: flex; justify-content: space-between; margin-bottom: 0.5rem; }
    .total-row.discount { color: #059669; }
    .total-row.total { font-weight: 700; font-size: 1.1rem; }
    hr { border: none; border-top: 1px solid #e5e7eb; margin: 0.5rem 0; }

    .btn-cancel {
      margin-top: 2rem; background: #fff; color: #ef4444;
      border: 1px solid #ef4444; padding: 0.75rem 2rem;
      border-radius: 8px; cursor: pointer; font-weight: 600;
    }
    .btn-cancel:hover { background: #fef2f2; }
    .btn-cancel:disabled { opacity: 0.5; cursor: not-allowed; }

    /* Return Modal */
    .modal-overlay {
      position: fixed; top: 0; left: 0;
      width: 100%; height: 100%;
      background: rgba(0,0,0,0.4);
      display: flex; align-items: center; justify-content: center;
      z-index: 8000;
    }
    .modal {
      background: #fff;
      border-radius: 12px;
      padding: 2rem;
      max-width: 480px;
      width: 90%;
    }
    .modal h3 { margin: 0 0 1rem; color: #1a1a2e; }
    .modal p { margin: 0 0 1rem; color: #374151; }
    .form-group { display: flex; flex-direction: column; gap: 0.25rem; margin-bottom: 1rem; }
    .form-group label { font-size: 0.85rem; color: #4b5563; font-weight: 500; }
    .form-group input, .form-group textarea {
      padding: 0.5rem 0.75rem;
      border: 1px solid #d1d5db;
      border-radius: 6px;
      font-family: inherit;
      font-size: 0.95rem;
    }
    .modal-actions { display: flex; gap: 0.75rem; justify-content: flex-end; }
    .btn-secondary {
      background: #f3f4f6; color: #374151;
      border: 1px solid #d1d5db;
      padding: 0.5rem 1.25rem; border-radius: 6px;
      cursor: pointer; font-weight: 500;
    }
    .btn-primary {
      background: #1a1a2e; color: #fff;
      border: none;
      padding: 0.5rem 1.25rem; border-radius: 6px;
      cursor: pointer; font-weight: 600;
    }
    .btn-primary:disabled { opacity: 0.5; cursor: not-allowed; }

    .not-found { text-align: center; padding: 4rem 1rem; }
    .not-found h2 { color: #1a1a2e; margin-bottom: 0.5rem; }
    .not-found p { color: #6b7280; margin-bottom: 1.5rem; }
    .btn-primary-link {
      background: #1a1a2e; color: #fff;
      padding: 0.75rem 2rem; border-radius: 8px;
      text-decoration: none; font-weight: 600;
    }

    @media (max-width: 768px) {
      .order-header { flex-direction: column; gap: 0.75rem; }
      .timeline { overflow-x: auto; }
      .item-row { flex-wrap: wrap; }
      .totals { max-width: 100%; }
    }
  `],
})
export class OrderDetailComponent implements OnInit {
  order: Order | null = null;
  loading = true;
  cancelling = false;
  statusSteps = STATUS_STEPS;

  // Return request
  returnItem: OrderItem | null = null;
  returnQuantity = 1;
  returnReason = '';
  submittingReturn = false;

  constructor(
    private route: ActivatedRoute,
    private orderService: OrderService,
    private toast: ToastService
  ) {}

  get isCancelled(): boolean {
    return this.order?.status === 'CANCELLED';
  }

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id')!;
    this.orderService.getOrder(id).subscribe({
      next: (order) => { this.order = order; this.loading = false; },
      error: () => {
        this.loading = false;
        this.toast.error('Failed to load order details.');
      },
    });
  }

  getStepIndex(status: string): number {
    const idx = STATUS_STEPS.indexOf(status);
    return idx >= 0 ? idx : -1;
  }

  formatStatus(status: string): string {
    return status.replace(/_/g, ' ').replace(/\b\w/g, (c) => c.toUpperCase());
  }

  canRequestReturn(item: OrderItem): boolean {
    return (this.order?.status === 'DELIVERED' || item.status === 'DELIVERED') && item.status !== 'RETURNED' && item.status !== 'RETURN_REQUESTED';
  }

  openReturnModal(item: OrderItem): void {
    this.returnItem = item;
    this.returnQuantity = item.quantity;
    this.returnReason = '';
  }

  closeReturnModal(): void {
    this.returnItem = null;
  }

  submitReturn(): void {
    if (!this.order || !this.returnItem || !this.returnReason) return;
    this.submittingReturn = true;
    this.orderService.requestReturn(
      this.order.id,
      this.returnItem.id,
      this.returnReason,
      this.returnQuantity
    ).subscribe({
      next: () => {
        this.submittingReturn = false;
        this.toast.success('Return request submitted successfully.');
        this.closeReturnModal();
        // Reload order to reflect updated status
        this.orderService.getOrder(this.order!.id).subscribe({
          next: (order) => (this.order = order),
        });
      },
      error: () => {
        this.submittingReturn = false;
        this.toast.error('Failed to submit return request.');
      },
    });
  }

  cancelOrder(): void {
    if (!this.order) return;
    this.cancelling = true;
    this.orderService.cancelOrder(this.order.id, 'Customer requested cancellation').subscribe({
      next: (order) => {
        this.order = order;
        this.cancelling = false;
        this.toast.info('Order has been cancelled.');
      },
      error: () => {
        this.cancelling = false;
        this.toast.error('Failed to cancel order.');
      },
    });
  }
}
