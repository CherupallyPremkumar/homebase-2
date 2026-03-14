import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink, RouterLinkActive } from '@angular/router';
import { AuthService } from '../../../core/services/auth.service';
import { CartService } from '../../../core/services/cart.service';

@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [CommonModule, RouterLink, RouterLinkActive],
  template: `
    <nav class="navbar">
      <div class="nav-container">
        <a routerLink="/" class="logo">HomeBase</a>

        <div class="nav-links">
          <a routerLink="/products" routerLinkActive="active">Products</a>
          <a routerLink="/categories" routerLinkActive="active">Categories</a>
        </div>

        <div class="nav-actions">
          <a routerLink="/cart" class="cart-link">
            Cart
            @if (cartService.itemCount > 0) {
              <span class="badge">{{ cartService.itemCount }}</span>
            }
          </a>

          @if (authService.isLoggedIn) {
            <a routerLink="/orders" routerLinkActive="active">Orders</a>
            <a routerLink="/profile" routerLinkActive="active">Profile</a>
            <button (click)="authService.logout()" class="btn-link">Logout</button>
          } @else {
            <button (click)="authService.login()" class="btn-primary">Login</button>
          }
        </div>
      </div>
    </nav>
  `,
  styles: [`
    .navbar {
      background: #fff;
      border-bottom: 1px solid #e5e7eb;
      padding: 0 1rem;
      position: sticky;
      top: 0;
      z-index: 100;
    }
    .nav-container {
      max-width: 1200px;
      margin: 0 auto;
      display: flex;
      align-items: center;
      height: 64px;
      gap: 2rem;
    }
    .logo {
      font-size: 1.5rem;
      font-weight: 700;
      color: #1a1a2e;
      text-decoration: none;
    }
    .nav-links {
      display: flex;
      gap: 1.5rem;
      flex: 1;
    }
    .nav-links a, .nav-actions a {
      color: #4b5563;
      text-decoration: none;
      font-weight: 500;
      transition: color 0.2s;
    }
    .nav-links a:hover, .nav-actions a:hover,
    .nav-links a.active, .nav-actions a.active {
      color: #1a1a2e;
    }
    .nav-actions {
      display: flex;
      align-items: center;
      gap: 1rem;
    }
    .cart-link {
      position: relative;
    }
    .badge {
      position: absolute;
      top: -8px;
      right: -12px;
      background: #e11d48;
      color: #fff;
      font-size: 0.7rem;
      width: 18px;
      height: 18px;
      border-radius: 50%;
      display: flex;
      align-items: center;
      justify-content: center;
    }
    .btn-primary {
      background: #1a1a2e;
      color: #fff;
      border: none;
      padding: 0.5rem 1rem;
      border-radius: 6px;
      cursor: pointer;
      font-weight: 500;
    }
    .btn-link {
      background: none;
      border: none;
      color: #4b5563;
      cursor: pointer;
      font-weight: 500;
    }
  `],
})
export class NavbarComponent {
  constructor(
    public authService: AuthService,
    public cartService: CartService
  ) {}
}
