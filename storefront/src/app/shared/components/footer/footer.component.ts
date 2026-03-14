import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-footer',
  standalone: true,
  imports: [RouterLink],
  template: `
    <footer class="footer">
      <div class="footer-container">
        <div class="footer-section">
          <h3>HomeBase</h3>
          <p>Handcrafted products, delivered with care.</p>
        </div>
        <div class="footer-section">
          <h4>Shop</h4>
          <a routerLink="/products">All Products</a>
          <a routerLink="/categories">Categories</a>
        </div>
        <div class="footer-section">
          <h4>Account</h4>
          <a routerLink="/orders">My Orders</a>
          <a routerLink="/profile">Profile</a>
        </div>
        <div class="footer-section">
          <h4>Help</h4>
          <a href="#">Contact Us</a>
          <a href="#">Shipping Info</a>
          <a href="#">Returns</a>
        </div>
      </div>
      <div class="footer-bottom">
        <p>&copy; 2026 HomeBase. All rights reserved.</p>
      </div>
    </footer>
  `,
  styles: [`
    .footer {
      background: #1a1a2e;
      color: #d1d5db;
      padding: 3rem 1rem 1rem;
      margin-top: 4rem;
    }
    .footer-container {
      max-width: 1200px;
      margin: 0 auto;
      display: grid;
      grid-template-columns: 2fr 1fr 1fr 1fr;
      gap: 2rem;
    }
    h3 { color: #fff; margin: 0 0 0.5rem; }
    h4 { color: #fff; margin: 0 0 0.75rem; font-size: 0.9rem; }
    .footer-section a {
      display: block;
      color: #9ca3af;
      text-decoration: none;
      margin-bottom: 0.4rem;
      font-size: 0.9rem;
    }
    .footer-section a:hover { color: #fff; }
    .footer-bottom {
      max-width: 1200px;
      margin: 2rem auto 0;
      padding-top: 1rem;
      border-top: 1px solid #374151;
      font-size: 0.85rem;
      color: #6b7280;
    }
  `],
})
export class FooterComponent {}
