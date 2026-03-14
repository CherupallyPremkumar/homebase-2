import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { CartService } from '../../core/services/cart.service';
import { ToastService } from '../../shared/components/toast/toast.component';
import { Cart, CartItem } from '../../shared/models/api.models';

@Component({
  selector: 'app-cart',
  standalone: true,
  imports: [CommonModule, RouterLink, FormsModule],
  template: `
    <div class="container">
      <h1>Shopping Cart</h1>

      @if (cart && cart.items && cart.items.length > 0) {
        <div class="cart-layout">
          <div class="cart-items">
            @for (item of cart.items; track item.id) {
              <div class="cart-item">
                <div class="item-image"></div>
                <div class="item-info">
                  <h3>
                    <a [routerLink]="['/products', item.productId]">
                      {{ item.productName || item.productId }}
                    </a>
                  </h3>
                  <p class="unit-price">{{ item.priceAmount | currency:'INR' }} each</p>
                  <div class="quantity-control">
                    <button (click)="updateQuantity(item.id, item.quantity - 1)" [disabled]="updating">-</button>
                    <span>{{ item.quantity }}</span>
                    <button (click)="updateQuantity(item.id, item.quantity + 1)" [disabled]="updating">+</button>
                  </div>
                  <p class="est-delivery">Est. delivery: {{ getEstimatedDelivery() | date:'mediumDate' }}</p>
                </div>
                <div class="item-right">
                  <span class="line-total">{{ getLineTotal(item) | currency:'INR' }}</span>
                  <button class="btn-remove" (click)="removeItem(item.id)">Remove</button>
                </div>
              </div>
            }

            <div class="continue-shopping">
              <a routerLink="/products">&larr; Continue Shopping</a>
            </div>
          </div>

          <div class="cart-summary">
            <h2>Order Summary</h2>
            <div class="summary-row">
              <span>Items ({{ getTotalItemCount() }})</span>
              <span>{{ cart.totalAmount?.amount | currency:'INR' }}</span>
            </div>
            <div class="summary-row">
              <span>Shipping</span>
              <span class="free-shipping">FREE</span>
            </div>
            @if (cart.discountAmount && cart.discountAmount.amount > 0) {
              <div class="summary-row discount">
                <span>Discount ({{ cart.appliedPromoCode }})</span>
                <span>-{{ cart.discountAmount.amount | currency:'INR' }}</span>
              </div>
            }
            <hr />
            <div class="summary-row total">
              <span>Total</span>
              <span>{{ getTotal() | currency:'INR' }}</span>
            </div>

            <div class="promo-section">
              <input
                type="text"
                [(ngModel)]="promoCode"
                placeholder="Enter promo code"
              />
              <button (click)="applyPromo()" [disabled]="!promoCode || applyingPromo">
                {{ applyingPromo ? '...' : 'Apply' }}
              </button>
            </div>

            <a routerLink="/checkout" class="btn-checkout">Proceed to Checkout</a>

            <div class="trust-badges">
              <span>&#128274; Secure Checkout</span>
              <span>&#8634; Easy Returns</span>
            </div>
          </div>
        </div>
      } @else {
        <div class="empty-cart">
          <div class="empty-illustration">
            <svg width="120" height="120" viewBox="0 0 120 120" fill="none" xmlns="http://www.w3.org/2000/svg">
              <circle cx="60" cy="60" r="58" stroke="#e5e7eb" stroke-width="2" fill="#f9fafb"/>
              <path d="M35 45h5l8 30h30l8-22H48" stroke="#9ca3af" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round" fill="none"/>
              <circle cx="55" cy="82" r="3.5" fill="#9ca3af"/>
              <circle cx="75" cy="82" r="3.5" fill="#9ca3af"/>
              <line x1="55" y1="55" x2="75" y2="55" stroke="#d1d5db" stroke-width="2" stroke-linecap="round"/>
              <line x1="52" y1="62" x2="72" y2="62" stroke="#d1d5db" stroke-width="2" stroke-linecap="round"/>
            </svg>
          </div>
          <h2>Your cart is empty</h2>
          <p>Looks like you haven't added any items yet. Start exploring and find something you love!</p>
          <a routerLink="/products" class="btn-primary">Start Shopping</a>
          <a routerLink="/" class="btn-link">Back to Home</a>
        </div>
      }
    </div>
  `,
  styles: [`
    .container { max-width: 1200px; margin: 2rem auto; padding: 0 1rem; }
    h1 { color: #1a1a2e; margin-bottom: 1.5rem; }
    .cart-layout { display: grid; grid-template-columns: 1fr 360px; gap: 2rem; }

    .cart-item {
      display: flex;
      gap: 1rem;
      padding: 1.25rem;
      border: 1px solid #e5e7eb;
      border-radius: 10px;
      margin-bottom: 1rem;
      align-items: flex-start;
      transition: box-shadow 0.2s;
    }
    .cart-item:hover { box-shadow: 0 2px 8px rgba(0,0,0,0.04); }
    .item-image {
      width: 90px;
      height: 90px;
      background: #f3f4f6;
      border-radius: 8px;
      flex-shrink: 0;
    }
    .item-info { flex: 1; }
    .item-info h3 { margin: 0 0 0.25rem; font-size: 1rem; }
    .item-info h3 a { color: inherit; text-decoration: none; }
    .item-info h3 a:hover { color: #1a1a2e; text-decoration: underline; }
    .unit-price { color: #6b7280; font-size: 0.85rem; margin: 0 0 0.5rem; }
    .quantity-control {
      display: inline-flex;
      border: 1px solid #d1d5db;
      border-radius: 6px;
      overflow: hidden;
    }
    .quantity-control button {
      padding: 0.3rem 0.75rem;
      border: none;
      background: #f3f4f6;
      cursor: pointer;
      font-size: 0.95rem;
      transition: background 0.15s;
    }
    .quantity-control button:hover:not(:disabled) { background: #e5e7eb; }
    .quantity-control button:disabled { opacity: 0.5; cursor: not-allowed; }
    .quantity-control span { padding: 0.3rem 0.85rem; font-weight: 500; }
    .est-delivery { color: #059669; font-size: 0.8rem; margin: 0.5rem 0 0; }

    .item-right {
      display: flex;
      flex-direction: column;
      align-items: flex-end;
      gap: 0.75rem;
    }
    .line-total { font-weight: 700; font-size: 1.05rem; color: #1a1a2e; }
    .btn-remove {
      background: none;
      border: none;
      color: #ef4444;
      cursor: pointer;
      font-size: 0.85rem;
      padding: 0;
    }
    .btn-remove:hover { text-decoration: underline; }

    .continue-shopping { margin-top: 0.5rem; }
    .continue-shopping a { color: #6b7280; text-decoration: none; font-size: 0.9rem; }
    .continue-shopping a:hover { color: #1a1a2e; }

    .cart-summary {
      border: 1px solid #e5e7eb;
      border-radius: 12px;
      padding: 1.5rem;
      height: fit-content;
      position: sticky;
      top: 80px;
    }
    .cart-summary h2 { margin: 0 0 1rem; font-size: 1.2rem; }
    .summary-row { display: flex; justify-content: space-between; margin-bottom: 0.5rem; font-size: 0.95rem; }
    .summary-row.discount { color: #059669; }
    .summary-row.total { font-weight: 700; font-size: 1.15rem; }
    .free-shipping { color: #059669; font-weight: 500; }
    hr { border: none; border-top: 1px solid #e5e7eb; margin: 0.75rem 0; }

    .promo-section { display: flex; gap: 0.5rem; margin: 1rem 0; }
    .promo-section input {
      flex: 1;
      padding: 0.5rem 0.75rem;
      border: 1px solid #d1d5db;
      border-radius: 6px;
      font-size: 0.9rem;
    }
    .promo-section input:focus { outline: none; border-color: #1a1a2e; }
    .promo-section button {
      padding: 0.5rem 1rem;
      background: #f3f4f6;
      border: 1px solid #d1d5db;
      border-radius: 6px;
      cursor: pointer;
      font-weight: 500;
    }
    .promo-section button:disabled { opacity: 0.5; cursor: not-allowed; }

    .btn-checkout {
      display: block;
      text-align: center;
      background: #1a1a2e;
      color: #fff;
      padding: 0.85rem;
      border-radius: 8px;
      text-decoration: none;
      font-weight: 600;
      font-size: 1rem;
      margin-top: 1rem;
      transition: background 0.2s;
    }
    .btn-checkout:hover { background: #16213e; }

    .trust-badges {
      display: flex;
      justify-content: center;
      gap: 1.25rem;
      margin-top: 1rem;
      font-size: 0.8rem;
      color: #9ca3af;
    }

    /* Empty cart */
    .empty-cart {
      text-align: center;
      padding: 4rem 1rem;
    }
    .empty-illustration { margin-bottom: 1.5rem; }
    .empty-cart h2 { color: #1a1a2e; margin-bottom: 0.5rem; font-size: 1.5rem; }
    .empty-cart p {
      color: #6b7280;
      margin-bottom: 2rem;
      max-width: 400px;
      margin-left: auto;
      margin-right: auto;
      line-height: 1.6;
    }
    .btn-primary {
      display: inline-block;
      background: #1a1a2e;
      color: #fff;
      padding: 0.75rem 2rem;
      border-radius: 8px;
      text-decoration: none;
      font-weight: 600;
    }
    .btn-link {
      display: block;
      color: #6b7280;
      text-decoration: none;
      margin-top: 1rem;
      font-size: 0.9rem;
    }
    .btn-link:hover { color: #1a1a2e; }

    @media (max-width: 768px) {
      .cart-layout { grid-template-columns: 1fr; }
      .cart-item { flex-wrap: wrap; }
    }
  `],
})
export class CartComponent implements OnInit {
  cart: Cart | null = null;
  promoCode = '';
  updating = false;
  applyingPromo = false;

  constructor(
    public cartService: CartService,
    private toast: ToastService
  ) {}

  ngOnInit(): void {
    this.cartService.cart$.subscribe((cart) => (this.cart = cart));
    this.cartService.loadCart();
  }

  getLineTotal(item: CartItem): number {
    return item.priceAmount * item.quantity;
  }

  getTotalItemCount(): number {
    return this.cart?.items?.reduce((sum, item) => sum + item.quantity, 0) ?? 0;
  }

  getEstimatedDelivery(): Date {
    const date = new Date();
    date.setDate(date.getDate() + 5); // Estimate 5 business days
    return date;
  }

  updateQuantity(itemId: string, quantity: number): void {
    if (quantity < 1) {
      this.removeItem(itemId);
      return;
    }
    this.updating = true;
    this.cartService.updateQuantity(itemId, quantity).subscribe({
      next: () => (this.updating = false),
      error: () => {
        this.updating = false;
        this.toast.error('Failed to update quantity.');
      },
    });
  }

  removeItem(itemId: string): void {
    this.cartService.removeItem(itemId).subscribe({
      next: () => this.toast.info('Item removed from cart.'),
      error: () => this.toast.error('Failed to remove item.'),
    });
  }

  applyPromo(): void {
    if (!this.promoCode) return;
    this.applyingPromo = true;
    this.cartService.applyPromoCode(this.promoCode).subscribe({
      next: () => {
        this.applyingPromo = false;
        this.toast.success('Promo code applied!');
        this.promoCode = '';
      },
      error: () => {
        this.applyingPromo = false;
        this.toast.error('Invalid promo code.');
      },
    });
  }

  getTotal(): number {
    const subtotal = this.cart?.totalAmount?.amount ?? 0;
    const discount = this.cart?.discountAmount?.amount ?? 0;
    return subtotal - discount;
  }
}
