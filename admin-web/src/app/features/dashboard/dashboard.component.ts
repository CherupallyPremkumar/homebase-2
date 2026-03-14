import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { QueryService } from '../../core/services/query.service';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule, RouterLink],
  template: `
    <div class="admin-page">
      <div class="page-header">
        <h2>Dashboard</h2>
      </div>
      <div class="stats-grid">
        <a routerLink="/orders" class="stat-card">
          <div class="stat-icon orders">&#128230;</div>
          <div class="stat-info">
            <span class="stat-value">{{ stats.totalOrders }}</span>
            <span class="stat-label">Total Orders</span>
          </div>
        </a>
        <a routerLink="/orders" class="stat-card">
          <div class="stat-icon revenue">&#8377;</div>
          <div class="stat-info">
            <span class="stat-value">{{ stats.totalRevenue | number:'1.0-0' }}</span>
            <span class="stat-label">Revenue</span>
          </div>
        </a>
        <a routerLink="/suppliers" class="stat-card">
          <div class="stat-icon suppliers">&#127970;</div>
          <div class="stat-info">
            <span class="stat-value">{{ stats.activeSuppliers }}</span>
            <span class="stat-label">Suppliers</span>
          </div>
        </a>
        <a routerLink="/returns" class="stat-card">
          <div class="stat-icon returns">&#128259;</div>
          <div class="stat-info">
            <span class="stat-value">{{ stats.pendingReturns }}</span>
            <span class="stat-label">Pending Returns</span>
          </div>
        </a>
        <a routerLink="/carts" class="stat-card">
          <div class="stat-icon carts">&#128722;</div>
          <div class="stat-info">
            <span class="stat-value">{{ stats.activeCarts }}</span>
            <span class="stat-label">Active Carts</span>
          </div>
        </a>
        <a routerLink="/inventory" class="stat-card">
          <div class="stat-icon inventory">&#128230;</div>
          <div class="stat-info">
            <span class="stat-value">{{ stats.lowStockItems }}</span>
            <span class="stat-label">Low Stock Items</span>
          </div>
        </a>
      </div>
    </div>
  `,
  styles: [`
    .admin-page { padding: 1.5rem; }
    .page-header { margin-bottom: 1.5rem; }
    .page-header h2 { margin: 0; font-size: 1.3rem; color: #1a1a2e; }
    .stats-grid { display: grid; grid-template-columns: repeat(auto-fill, minmax(220px, 1fr)); gap: 1.25rem; }
    .stat-card {
      display: flex; align-items: center; gap: 1rem; padding: 1.25rem;
      background: #fff; border: 1px solid #e5e7eb; border-radius: 10px;
      text-decoration: none; color: inherit; transition: box-shadow 0.2s, transform 0.2s;
    }
    .stat-card:hover { box-shadow: 0 4px 12px rgba(0,0,0,0.08); transform: translateY(-2px); }
    .stat-icon {
      width: 48px; height: 48px; border-radius: 12px; display: flex; align-items: center;
      justify-content: center; font-size: 1.4rem;
    }
    .stat-icon.orders { background: #dbeafe; color: #2563eb; }
    .stat-icon.revenue { background: #d1fae5; color: #059669; font-weight: 700; }
    .stat-icon.suppliers { background: #ede9fe; color: #7c3aed; }
    .stat-icon.returns { background: #fef3c7; color: #d97706; }
    .stat-icon.carts { background: #fce7f3; color: #db2777; }
    .stat-icon.inventory { background: #ffedd5; color: #ea580c; }
    .stat-info { display: flex; flex-direction: column; }
    .stat-value { font-size: 1.5rem; font-weight: 700; color: #1a1a2e; line-height: 1.2; }
    .stat-label { font-size: 0.8rem; color: #6b7280; margin-top: 0.15rem; }
  `],
})
export class DashboardComponent implements OnInit {
  stats = { totalOrders: 0, totalRevenue: 0, activeSuppliers: 0, pendingReturns: 0, activeCarts: 0, lowStockItems: 0 };

  constructor(private queryService: QueryService) {}

  ngOnInit(): void {
    this.queryService.search('orders', { numRowsInPage: 1 }).subscribe(r => {
      this.stats.totalOrders = r.totalCount;
    });
    this.queryService.search('orders', { numRowsInPage: 0 }).subscribe(r => {
      this.stats.totalRevenue = r.items.reduce((sum: number, o: any) => sum + (o.totalAmount?.amount ?? 0), 0);
    });
    this.queryService.search('suppliers', { numRowsInPage: 1 }).subscribe(r => {
      this.stats.activeSuppliers = r.totalCount;
    });
    this.queryService.search('returnrequests', { numRowsInPage: 1, filters: { stateId: 'PENDING' } }).subscribe(r => {
      this.stats.pendingReturns = r.totalCount;
    });
    this.queryService.search('carts', { numRowsInPage: 1, filters: { stateId: 'ACTIVE' } }).subscribe(r => {
      this.stats.activeCarts = r.totalCount;
    });
    this.queryService.search('inventory', { numRowsInPage: 1, filters: { status: 'LOW_STOCK' } }).subscribe(r => {
      this.stats.lowStockItems = r.totalCount;
    });
  }
}
