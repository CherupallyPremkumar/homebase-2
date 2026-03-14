import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { CartService } from '../../core/services/cart.service';
import { AuthService } from '../../core/services/auth.service';
import { ApiService } from '../../core/services/api.service';
import { ToastService } from '../../shared/components/toast/toast.component';
import { SpinnerComponent } from '../../shared/components/spinner/spinner.component';
import { Cart, Address } from '../../shared/models/api.models';

declare var Razorpay: any;

@Component({
  selector: 'app-checkout',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink, SpinnerComponent],
  template: `
    <div class="container">
      <h1>Checkout</h1>

      @if (!authService.isLoggedIn) {
        <div class="login-prompt">
          <div class="login-icon">&#128274;</div>
          <p>Please log in to continue checkout.</p>
          <button (click)="authService.login()" class="btn-primary">Login</button>
        </div>
      } @else if (processing) {
        <div class="processing-state">
          <app-spinner />
          <p>Processing your order...</p>
        </div>
      } @else if (cart && cart.items && cart.items.length > 0) {
        <div class="checkout-steps">
          <div class="step" [class.active]="step === 1" [class.done]="step > 1">
            <span class="step-num">{{ step > 1 ? '&#10003;' : '1' }}</span>
            <span>Address</span>
          </div>
          <div class="step-line" [class.done]="step > 1"></div>
          <div class="step" [class.active]="step === 2">
            <span class="step-num">2</span>
            <span>Payment</span>
          </div>
        </div>

        <div class="checkout-layout">
          <div class="checkout-form">
            @if (step === 1) {
              <h2>Delivery Address</h2>
              <div class="form-grid">
                <div class="form-group full">
                  <label>Full Name *</label>
                  <input type="text" [(ngModel)]="address.label" placeholder="Full name" />
                </div>
                <div class="form-group full">
                  <label>Address Line 1 *</label>
                  <input type="text" [(ngModel)]="address.line1" placeholder="Street address" />
                </div>
                <div class="form-group full">
                  <label>Address Line 2</label>
                  <input type="text" [(ngModel)]="address.line2" placeholder="Apartment, floor, etc." />
                </div>
                <div class="form-group">
                  <label>City *</label>
                  <input type="text" [(ngModel)]="address.city" />
                </div>
                <div class="form-group">
                  <label>State</label>
                  <input type="text" [(ngModel)]="address.state" />
                </div>
                <div class="form-group">
                  <label>Postal Code *</label>
                  <input type="text" [(ngModel)]="address.postalCode" />
                </div>
                <div class="form-group">
                  <label>Phone</label>
                  <input type="tel" [(ngModel)]="address.phone" />
                </div>
              </div>

              <button class="btn-primary" (click)="goToPayment()">
                Continue to Payment
              </button>
            }

            @if (step === 2) {
              <h2>Payment</h2>
              <div class="address-review">
                <div class="review-header">
                  <h3>Delivering to</h3>
                  <button class="btn-link" (click)="step = 1">Change</button>
                </div>
                <p>{{ address.label }}</p>
                <p>{{ address.line1 }}{{ address.line2 ? ', ' + address.line2 : '' }}</p>
                <p>{{ address.city }}, {{ address.state }} {{ address.postalCode }}</p>
              </div>

              <div class="payment-info">
                <p>Click below to proceed. You will be redirected to the payment gateway to complete your purchase securely.</p>
              </div>

              <button class="btn-primary" (click)="proceedToPayment()">
                Pay {{ getTotal() | currency:'INR' }}
              </button>
              <button class="btn-back" (click)="step = 1">
                &larr; Back to Address
              </button>
            }

            @if (error) {
              <p class="error">{{ error }}</p>
            }
          </div>

          <div class="order-summary">
            <h2>Order Summary</h2>
            @for (item of cart.items; track item.id) {
              <div class="summary-item">
                <div class="summary-item-info">
                  <span class="summary-name">{{ item.productName || item.productId }}</span>
                  <span class="summary-qty">x{{ item.quantity }}</span>
                </div>
                <span class="summary-price">{{ item.priceAmount * item.quantity | currency:'INR' }}</span>
              </div>
            }
            <hr />
            <div class="summary-item subtotal">
              <span>Subtotal</span>
              <span>{{ cart.totalAmount?.amount | currency:'INR' }}</span>
            </div>
            @if (cart.discountAmount && cart.discountAmount.amount > 0) {
              <div class="summary-item discount">
                <span>Discount</span>
                <span>-{{ cart.discountAmount.amount | currency:'INR' }}</span>
              </div>
            }
            <hr />
            <div class="summary-item total">
              <span>Total</span>
              <span>{{ getTotal() | currency:'INR' }}</span>
            </div>
          </div>
        </div>
      } @else {
        <div class="empty-state">
          <p>Your cart is empty. Add items before checkout.</p>
          <a routerLink="/products" class="btn-primary-link">Browse Products</a>
        </div>
      }
    </div>
  `,
  styles: [`
    .container { max-width: 1200px; margin: 2rem auto; padding: 0 1rem; }
    h1 { color: #1a1a2e; margin-bottom: 1.5rem; }

    .login-prompt { text-align: center; padding: 3rem; }
    .login-icon { font-size: 3rem; margin-bottom: 1rem; }

    .processing-state { text-align: center; padding: 4rem 1rem; }
    .processing-state p { color: #6b7280; margin-top: 1rem; }

    /* Steps */
    .checkout-steps {
      display: flex;
      align-items: center;
      justify-content: center;
      margin-bottom: 2rem;
      gap: 0;
    }
    .step {
      display: flex;
      align-items: center;
      gap: 0.5rem;
      color: #9ca3af;
      font-weight: 500;
      font-size: 0.95rem;
    }
    .step.active { color: #1a1a2e; }
    .step.done { color: #059669; }
    .step-num {
      width: 28px; height: 28px;
      border-radius: 50%;
      border: 2px solid #d1d5db;
      display: flex; align-items: center; justify-content: center;
      font-size: 0.8rem; font-weight: 700;
    }
    .step.active .step-num { border-color: #1a1a2e; background: #1a1a2e; color: #fff; }
    .step.done .step-num { border-color: #059669; background: #059669; color: #fff; }
    .step-line { width: 80px; height: 2px; background: #d1d5db; margin: 0 0.75rem; }
    .step-line.done { background: #059669; }

    .checkout-layout { display: grid; grid-template-columns: 1fr 380px; gap: 2rem; }
    .checkout-form h2, .order-summary h2 { font-size: 1.2rem; margin: 0 0 1rem; }
    .form-grid { display: grid; grid-template-columns: 1fr 1fr; gap: 1rem; margin-bottom: 1.5rem; }
    .form-group { display: flex; flex-direction: column; gap: 0.25rem; }
    .form-group.full { grid-column: span 2; }
    .form-group label { font-size: 0.85rem; color: #4b5563; font-weight: 500; }
    .form-group input {
      padding: 0.5rem 0.75rem;
      border: 1px solid #d1d5db;
      border-radius: 6px;
      font-size: 0.95rem;
    }
    .form-group input:focus { outline: none; border-color: #1a1a2e; }

    .btn-primary {
      background: #1a1a2e;
      color: #fff;
      border: none;
      padding: 0.75rem 2rem;
      border-radius: 8px;
      font-weight: 600;
      cursor: pointer;
      width: 100%;
      font-size: 1rem;
      transition: background 0.2s;
    }
    .btn-primary:hover { background: #16213e; }
    .btn-primary:disabled { opacity: 0.6; cursor: not-allowed; }
    .btn-back {
      background: none;
      border: none;
      color: #6b7280;
      padding: 0.75rem;
      cursor: pointer;
      width: 100%;
      margin-top: 0.5rem;
      font-size: 0.9rem;
    }
    .btn-back:hover { color: #1a1a2e; }
    .btn-link { background: none; border: none; color: #1a1a2e; cursor: pointer; font-weight: 600; font-size: 0.85rem; }
    .error { color: #ef4444; margin-top: 1rem; }

    /* Address review */
    .address-review {
      background: #f9fafb;
      border: 1px solid #e5e7eb;
      border-radius: 8px;
      padding: 1rem 1.25rem;
      margin-bottom: 1.5rem;
    }
    .review-header { display: flex; justify-content: space-between; align-items: center; }
    .review-header h3 { margin: 0 0 0.5rem; font-size: 0.95rem; color: #374151; }
    .address-review p { margin: 0.1rem 0; color: #4b5563; font-size: 0.9rem; }

    .payment-info {
      background: #eff6ff;
      border: 1px solid #bfdbfe;
      border-radius: 8px;
      padding: 1rem;
      margin-bottom: 1.5rem;
    }
    .payment-info p { margin: 0; color: #1e40af; font-size: 0.9rem; }

    /* Summary */
    .order-summary {
      border: 1px solid #e5e7eb;
      border-radius: 12px;
      padding: 1.5rem;
      height: fit-content;
      position: sticky;
      top: 80px;
    }
    .summary-item { display: flex; justify-content: space-between; margin-bottom: 0.5rem; font-size: 0.95rem; }
    .summary-item-info { display: flex; gap: 0.5rem; flex: 1; }
    .summary-name { flex: 1; }
    .summary-qty { color: #6b7280; }
    .summary-price { font-weight: 500; }
    .summary-item.subtotal { color: #6b7280; }
    .summary-item.discount { color: #059669; }
    .summary-item.total { font-weight: 700; font-size: 1.1rem; }
    hr { border: none; border-top: 1px solid #e5e7eb; margin: 0.75rem 0; }

    .empty-state { text-align: center; padding: 3rem; }
    .empty-state p { color: #6b7280; margin-bottom: 1rem; }
    .btn-primary-link {
      background: #1a1a2e; color: #fff;
      padding: 0.75rem 2rem; border-radius: 8px;
      text-decoration: none; font-weight: 600;
    }

    @media (max-width: 768px) {
      .checkout-layout { grid-template-columns: 1fr; }
      .form-grid { grid-template-columns: 1fr; }
      .form-group.full { grid-column: span 1; }
    }
  `],
})
export class CheckoutComponent implements OnInit {
  cart: Cart | null = null;
  processing = false;
  error = '';
  step = 1;
  address: Address = {
    label: '',
    line1: '',
    line2: '',
    city: '',
    state: '',
    postalCode: '',
    country: 'IN',
    phone: '',
  };

  constructor(
    public authService: AuthService,
    private cartService: CartService,
    private apiService: ApiService,
    private router: Router,
    private toast: ToastService
  ) {}

  ngOnInit(): void {
    this.cartService.cart$.subscribe((cart) => (this.cart = cart));
    this.cartService.loadCart();
  }

  getTotal(): number {
    const subtotal = this.cart?.totalAmount?.amount ?? 0;
    const discount = this.cart?.discountAmount?.amount ?? 0;
    return subtotal - discount;
  }

  goToPayment(): void {
    if (!this.address.line1 || !this.address.city || !this.address.postalCode) {
      this.error = 'Please fill in required address fields (Address, City, Postal Code).';
      return;
    }
    this.error = '';
    this.step = 2;
  }

  proceedToPayment(): void {
    this.processing = true;
    this.error = '';

    // Step 1: Add delivery address to cart
    this.cartService.addDeliveryAddress(this.address).subscribe({
      next: () => {
        // Step 2: Initiate checkout (triggers STM transition, creates payment session)
        this.cartService.initiateCheckout().subscribe({
          next: (cart: any) => {
            // The cart or response may contain payment gateway details
            const gatewaySessionId = cart.gatewaySessionId || cart.paymentSessionId;
            if (gatewaySessionId && typeof Razorpay !== 'undefined') {
              this.openRazorpay(gatewaySessionId, cart);
            } else {
              // Fallback: redirect to orders if no gateway integration
              this.processing = false;
              this.cartService.clearLocalCart();
              this.toast.success('Order placed successfully!');
              this.router.navigate(['/checkout/success'], { queryParams: { orderId: cart.orderId || cart.id } });
            }
          },
          error: (err) => {
            this.processing = false;
            this.error = err.error?.description ?? 'Checkout failed. Please try again.';
            this.toast.error(this.error);
          },
        });
      },
      error: (err) => {
        this.processing = false;
        this.error = err.error?.description ?? 'Failed to save address.';
        this.toast.error(this.error);
      },
    });
  }

  private openRazorpay(orderId: string, cart: any): void {
    const options = {
      key: cart.razorpayKeyId || '',
      amount: this.getTotal() * 100, // Razorpay expects paise
      currency: 'INR',
      name: 'HomeBase',
      description: 'Order Payment',
      order_id: orderId,
      handler: (response: any) => {
        // Payment successful -- verify on backend
        this.verifyPayment(response);
      },
      modal: {
        ondismiss: () => {
          this.processing = false;
          this.toast.info('Payment was cancelled.');
          this.router.navigate(['/checkout/failure'], { queryParams: { reason: 'cancelled' } });
        },
      },
      prefill: {
        name: this.address.label || '',
        contact: this.address.phone || '',
      },
      theme: {
        color: '#1a1a2e',
      },
    };

    try {
      const rzp = new Razorpay(options);
      rzp.on('payment.failed', (response: any) => {
        this.processing = false;
        this.toast.error('Payment failed. Please try again.');
        this.router.navigate(['/checkout/failure'], {
          queryParams: {
            reason: response.error?.description || 'payment_failed',
          },
        });
      });
      rzp.open();
    } catch {
      // Razorpay SDK not loaded -- fallback
      this.processing = false;
      this.toast.error('Payment gateway is not available. Please try again later.');
    }
  }

  private verifyPayment(response: any): void {
    this.apiService.post<any>('/checkout/verify-payment', {
      razorpayPaymentId: response.razorpay_payment_id,
      razorpayOrderId: response.razorpay_order_id,
      razorpaySignature: response.razorpay_signature,
    }).subscribe({
      next: (result: any) => {
        this.processing = false;
        this.cartService.clearLocalCart();
        this.toast.success('Payment successful!');
        this.router.navigate(['/checkout/success'], {
          queryParams: { orderId: result.data?.orderId || result.orderId || '' },
        });
      },
      error: () => {
        this.processing = false;
        this.toast.error('Payment verification failed. Please contact support.');
        this.router.navigate(['/checkout/failure'], { queryParams: { reason: 'verification_failed' } });
      },
    });
  }
}
