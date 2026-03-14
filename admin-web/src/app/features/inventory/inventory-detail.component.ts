import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterLink, ActivatedRoute } from '@angular/router';
import { ApiService } from '../../core/services/api.service';
import { DetailLayoutComponent } from '../../shared/components/detail-layout/detail-layout.component';
import { ToastService } from '../../shared/components/toast/toast.component';

@Component({
  selector: 'app-inventory-detail',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink, DetailLayoutComponent],
  template: `
    <div class="loading" *ngIf="loading">Loading inventory...</div>
    <div class="error-msg" *ngIf="error">{{ error }}</div>

    <app-detail-layout *ngIf="inventory"
      [title]="'Inventory #' + inventory.id"
      [subtitle]="'Product: ' + (inventory.productId ?? inventory.id)"
      [status]="inventory.stateId ?? inventory.status ?? ''">

      <!-- Stock Overview -->
      <div class="card">
        <h3>Stock Overview</h3>
        <div class="stock-grid">
          <div class="stock-card">
            <span class="stock-label">Total Quantity</span>
            <span class="stock-value">{{ inventory.quantity ?? 0 }}</span>
          </div>
          <div class="stock-card available">
            <span class="stock-label">Available</span>
            <span class="stock-value">{{ inventory.availableQuantity ?? 0 }}</span>
          </div>
          <div class="stock-card reserved">
            <span class="stock-label">Reserved</span>
            <span class="stock-value">{{ inventory.reservedQuantity ?? 0 }}</span>
          </div>
          <div class="stock-card damaged">
            <span class="stock-label">Damaged</span>
            <span class="stock-value">{{ inventory.damagedQuantity ?? 0 }}</span>
          </div>
        </div>
      </div>

      <!-- Inventory Info -->
      <div class="card">
        <h3>Details</h3>
        <div class="field-grid">
          <div class="field">
            <label>Product ID</label>
            <span><a [routerLink]="['/products', inventory.productId]">{{ inventory.productId }}</a></span>
          </div>
          <div class="field">
            <label>Warehouse</label>
            <span>{{ inventory.warehouseId ?? '---' }}</span>
          </div>
          <div class="field">
            <label>Reorder Level</label>
            <span>{{ inventory.reorderLevel ?? '---' }}</span>
          </div>
          <div class="field">
            <label>Last Restocked</label>
            <span>{{ inventory.lastRestockedAt ? (inventory.lastRestockedAt | date:'medium') : '---' }}</span>
          </div>
        </div>
      </div>

      <!-- Stock Adjustment -->
      <div class="card">
        <h3>Stock Adjustment</h3>
        <form (ngSubmit)="adjustStock()" class="edit-form">
          <div class="form-row">
            <div class="form-group">
              <label for="adjustQty">Quantity</label>
              <input id="adjustQty" type="number" [(ngModel)]="adjustModel.quantity" name="adjustQty"
                min="0" step="1" required>
            </div>
            <div class="form-group">
              <label for="adjustComment">Comment</label>
              <input id="adjustComment" [(ngModel)]="adjustModel.comment" name="adjustComment"
                placeholder="Reason for adjustment">
            </div>
          </div>
          <div class="action-buttons" style="margin-top: 1rem;">
            <button type="button" class="btn btn-success" (click)="triggerEvent('approveStock')"
              [disabled]="actionLoading">Approve Stock</button>
            <button type="button" class="btn btn-danger" (click)="triggerEvent('rejectStock')"
              [disabled]="actionLoading">Reject Stock</button>
            <button type="button" class="btn btn-warning" (click)="triggerEvent('damageFound')"
              [disabled]="actionLoading">Report Damage</button>
            <button type="button" class="btn btn-primary" (click)="triggerEvent('allocateToWarehouse')"
              [disabled]="actionLoading">Allocate to Warehouse</button>
          </div>
        </form>
      </div>
    </app-detail-layout>
  `,
  styles: [`
    .loading { padding: 3rem; text-align: center; color: #6b7280; }
    .error-msg { padding: 1.5rem; margin: 1.5rem; background: #fee2e2; color: #991b1b; border-radius: 8px; }
    .card { background: #fff; border: 1px solid #e5e7eb; border-radius: 8px; padding: 1.25rem; }
    .card h3 { margin: 0 0 1rem; font-size: 1rem; color: #1a1a2e; }
    .stock-grid { display: grid; grid-template-columns: repeat(4, 1fr); gap: 1rem; }
    .stock-card {
      background: #f9fafb; border: 1px solid #e5e7eb; border-radius: 8px;
      padding: 1rem; text-align: center;
    }
    .stock-card.available { border-left: 3px solid #10b981; }
    .stock-card.reserved { border-left: 3px solid #f59e0b; }
    .stock-card.damaged { border-left: 3px solid #ef4444; }
    .stock-label { display: block; font-size: 0.75rem; text-transform: uppercase; color: #6b7280; font-weight: 600; margin-bottom: 0.3rem; }
    .stock-value { display: block; font-size: 1.5rem; font-weight: 700; color: #1a1a2e; }
    .field-grid { display: grid; grid-template-columns: 1fr 1fr; gap: 1rem; }
    .field label { display: block; font-size: 0.75rem; text-transform: uppercase; color: #6b7280; font-weight: 600; letter-spacing: 0.05em; margin-bottom: 0.2rem; }
    .field span { font-size: 0.95rem; color: #111827; }
    .field a { color: #e94560; text-decoration: none; font-weight: 500; }
    .field a:hover { text-decoration: underline; }
    .form-row { display: flex; gap: 1rem; }
    .form-group { flex: 1; }
    .form-group label { display: block; font-size: 0.8rem; font-weight: 600; color: #374151; margin-bottom: 0.3rem; }
    .form-group input {
      width: 100%; padding: 0.5rem 0.75rem; border: 1px solid #d1d5db; border-radius: 6px;
      font-size: 0.9rem; color: #111827; box-sizing: border-box;
    }
    .form-group input:focus { outline: none; border-color: #e94560; box-shadow: 0 0 0 2px rgba(233,69,96,0.15); }
    .action-buttons { display: flex; gap: 0.75rem; flex-wrap: wrap; }
    .btn {
      padding: 0.5rem 1.2rem; border: none; border-radius: 6px;
      font-size: 0.85rem; font-weight: 600; cursor: pointer; transition: opacity 0.15s;
    }
    .btn:disabled { opacity: 0.5; cursor: not-allowed; }
    .btn-primary { background: #e94560; color: #fff; }
    .btn-primary:hover:not(:disabled) { background: #d63851; }
    .btn-success { background: #065f46; color: #fff; }
    .btn-success:hover:not(:disabled) { background: #047857; }
    .btn-danger { background: #991b1b; color: #fff; }
    .btn-danger:hover:not(:disabled) { background: #b91c1c; }
    .btn-warning { background: #92400e; color: #fff; }
    .btn-warning:hover:not(:disabled) { background: #a16207; }
  `],
})
export class InventoryDetailComponent implements OnInit {
  inventory: any = null;
  adjustModel: any = { quantity: 0, comment: '' };
  loading = true;
  error = '';
  actionLoading = false;
  private id = '';

  constructor(
    private route: ActivatedRoute,
    private api: ApiService,
    private toast: ToastService,
  ) {}

  ngOnInit(): void {
    this.id = this.route.snapshot.paramMap.get('id')!;
    this.loadInventory();
  }

  loadInventory(): void {
    this.loading = true;
    this.api.get<any>('inventory', this.id).subscribe({
      next: data => { this.inventory = data; this.loading = false; },
      error: () => { this.error = 'Failed to load inventory.'; this.loading = false; },
    });
  }

  adjustStock(): void {
    this.triggerEvent('approveStock');
  }

  triggerEvent(event: string): void {
    this.actionLoading = true;
    const payload: any = {};
    if (this.adjustModel.quantity) payload.quantity = this.adjustModel.quantity;
    if (this.adjustModel.comment) payload.comment = this.adjustModel.comment;
    this.api.triggerEvent<any>('inventory', this.id, event, payload).subscribe({
      next: data => {
        this.inventory = data;
        this.actionLoading = false;
        this.toast.success(`Inventory ${event} successful.`);
      },
      error: () => {
        this.actionLoading = false;
        this.toast.error(`Failed to ${event} inventory.`);
      },
    });
  }
}
