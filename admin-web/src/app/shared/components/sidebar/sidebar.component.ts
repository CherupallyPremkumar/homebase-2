import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink, RouterLinkActive } from '@angular/router';

@Component({
  selector: 'app-sidebar',
  standalone: true,
  imports: [CommonModule, RouterLink, RouterLinkActive],
  template: `
    <aside class="sidebar">
      <div class="logo">
        <span class="logo-icon">H</span>
        <span class="logo-text">HomeBase Admin</span>
      </div>
      <nav>
        <ul>
          <li><a routerLink="/dashboard" routerLinkActive="active"><i class="icon">&#9632;</i> Dashboard</a></li>

          <li class="section-label">Commerce</li>
          <li><a routerLink="/orders" routerLinkActive="active"><i class="icon">&#128230;</i> Orders</a></li>
          <li><a routerLink="/products" routerLinkActive="active"><i class="icon">&#128230;</i> Products</a></li>
          <li><a routerLink="/inventory" routerLinkActive="active"><i class="icon">&#127970;</i> Inventory</a></li>
          <li><a routerLink="/carts" routerLinkActive="active"><i class="icon">&#128722;</i> Carts</a></li>

          <li class="section-label">Marketplace</li>
          <li><a routerLink="/suppliers" routerLinkActive="active"><i class="icon">&#128666;</i> Suppliers</a></li>
          <li><a routerLink="/catalog" routerLinkActive="active"><i class="icon">&#128218;</i> Catalog</a></li>
          <li><a routerLink="/promo" routerLinkActive="active"><i class="icon">&#127991;</i> Promo Codes</a></li>

          <li class="section-label">Fulfillment</li>
          <li><a routerLink="/shipping" routerLinkActive="active"><i class="icon">&#128666;</i> Shipping</a></li>
          <li><a routerLink="/returns" routerLinkActive="active"><i class="icon">&#128260;</i> Returns</a></li>
          <li><a routerLink="/settlements" routerLinkActive="active"><i class="icon">&#128176;</i> Settlements</a></li>

          <li class="section-label">Users</li>
          <li><a routerLink="/customers" routerLinkActive="active"><i class="icon">&#128101;</i> Customers</a></li>
        </ul>
      </nav>
    </aside>
  `,
  styles: [`
    .sidebar {
      width: 240px;
      height: 100vh;
      background: #1a1a2e;
      color: #d1d5db;
      position: fixed;
      left: 0;
      top: 0;
      overflow-y: auto;
      z-index: 100;
    }
    .logo {
      padding: 1.25rem;
      display: flex;
      align-items: center;
      gap: 0.75rem;
      border-bottom: 1px solid rgba(255,255,255,0.1);
    }
    .logo-icon {
      width: 32px; height: 32px;
      background: #e94560;
      color: #fff;
      display: flex; align-items: center; justify-content: center;
      border-radius: 8px; font-weight: 700;
    }
    .logo-text { font-weight: 600; color: #fff; font-size: 0.95rem; }
    nav ul { list-style: none; padding: 0.5rem 0; margin: 0; }
    .section-label {
      font-size: 0.7rem;
      text-transform: uppercase;
      letter-spacing: 0.1em;
      color: #6b7280;
      padding: 1rem 1.25rem 0.25rem;
      font-weight: 600;
    }
    nav a {
      display: flex;
      align-items: center;
      gap: 0.75rem;
      padding: 0.6rem 1.25rem;
      color: #9ca3af;
      text-decoration: none;
      font-size: 0.9rem;
      transition: all 0.15s;
      border-left: 3px solid transparent;
    }
    nav a:hover { color: #fff; background: rgba(255,255,255,0.05); }
    nav a.active {
      color: #fff;
      background: rgba(233,69,96,0.15);
      border-left-color: #e94560;
    }
    .icon { font-style: normal; font-size: 1rem; width: 20px; text-align: center; }
  `],
})
export class SidebarComponent {}
