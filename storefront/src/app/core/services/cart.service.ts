import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable, tap } from 'rxjs';
import { ApiService } from './api.service';
import { Cart } from '../../shared/models/api.models';

@Injectable({ providedIn: 'root' })
export class CartService {
  private cartSubject = new BehaviorSubject<Cart | null>(null);
  cart$ = this.cartSubject.asObservable();

  private cartIdKey = 'homebase_cart_id';

  constructor(private api: ApiService) {}

  get cartId(): string | null {
    return localStorage.getItem(this.cartIdKey);
  }

  loadCart(): void {
    const id = this.cartId;
    if (id) {
      this.api.get<Cart>('cart', id).subscribe({
        next: (cart) => this.cartSubject.next(cart),
        error: () => {
          localStorage.removeItem(this.cartIdKey);
          this.cartSubject.next(null);
        },
      });
    }
  }

  createCart(): Observable<Cart> {
    return this.api.create<Cart>('cart', {}).pipe(
      tap((cart) => {
        localStorage.setItem(this.cartIdKey, cart.id);
        this.cartSubject.next(cart);
      })
    );
  }

  addItem(productId: string, quantity: number): Observable<Cart> {
    return this.ensureCart().pipe(
      tap(() => {
        const id = this.cartId!;
        this.api
          .triggerEvent<Cart>('cart', id, 'addItem', { productId, quantity })
          .subscribe((cart) => this.cartSubject.next(cart));
      })
    );
  }

  removeItem(itemId: string): Observable<Cart> {
    const id = this.cartId!;
    return this.api
      .triggerEvent<Cart>('cart', id, 'removeItem', { itemId })
      .pipe(tap((cart) => this.cartSubject.next(cart)));
  }

  updateQuantity(itemId: string, quantity: number): Observable<Cart> {
    const id = this.cartId!;
    return this.api
      .triggerEvent<Cart>('cart', id, 'updateQuantity', { itemId, quantity })
      .pipe(tap((cart) => this.cartSubject.next(cart)));
  }

  initiateCheckout(): Observable<Cart> {
    const id = this.cartId!;
    return this.api
      .triggerEvent<Cart>('cart', id, 'initiateCheckout', {})
      .pipe(tap((cart) => this.cartSubject.next(cart)));
  }

  applyPromoCode(promoCode: string): Observable<Cart> {
    const id = this.cartId!;
    return this.api
      .triggerEvent<Cart>('cart', id, 'applyPromoCode', { promoCode })
      .pipe(tap((cart) => this.cartSubject.next(cart)));
  }

  addDeliveryAddress(address: any): Observable<Cart> {
    const id = this.cartId!;
    return this.api
      .triggerEvent<Cart>('cart', id, 'addDeliveryAddress', address)
      .pipe(tap((cart) => this.cartSubject.next(cart)));
  }

  get itemCount(): number {
    return this.cartSubject.value?.items?.length ?? 0;
  }

  clearLocalCart(): void {
    localStorage.removeItem(this.cartIdKey);
    this.cartSubject.next(null);
  }

  private ensureCart(): Observable<Cart> {
    if (this.cartId) {
      return this.api.get<Cart>('cart', this.cartId);
    }
    return this.createCart();
  }
}
