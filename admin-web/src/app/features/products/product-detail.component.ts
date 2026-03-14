import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterLink, ActivatedRoute } from '@angular/router';
import { ApiService } from '../../core/services/api.service';
import { DetailLayoutComponent } from '../../shared/components/detail-layout/detail-layout.component';
import { ToastService } from '../../shared/components/toast/toast.component';

@Component({
  selector: 'app-product-detail',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink, DetailLayoutComponent],
  template: `
    <div class="loading" *ngIf="loading">Loading product...</div>
    <div class="error-msg" *ngIf="error">{{ error }}</div>

    <app-detail-layout *ngIf="product"
      [title]="product.name || 'Product'"
      [subtitle]="'ID: ' + product.id"
      [status]="product.stateId ?? ''">

      <!-- Info Card -->
      <div class="card">
        <h3>Product Information</h3>
        <div class="field-grid">
          <div class="field">
            <label>Name</label>
            <span>{{ product.name }}</span>
          </div>
          <div class="field">
            <label>Brand</label>
            <span>{{ product.brand ?? '---' }}</span>
          </div>
          <div class="field">
            <label>SKU</label>
            <span>{{ product.sku ?? '---' }}</span>
          </div>
          <div class="field">
            <label>Category ID</label>
            <span>{{ product.categoryId ?? '---' }}</span>
          </div>
          <div class="field full-width">
            <label>Description</label>
            <span>{{ product.description ?? 'No description' }}</span>
          </div>
        </div>
      </div>

      <!-- State Transition Actions -->
      <div class="card" *ngIf="product.stateId">
        <h3>Actions</h3>
        <div class="action-buttons">
          <button class="btn btn-success" (click)="triggerEvent('approve')"
            [disabled]="actionLoading">Approve</button>
          <button class="btn btn-danger" (click)="triggerEvent('reject')"
            [disabled]="actionLoading">Reject</button>
          <button class="btn btn-warning" (click)="triggerEvent('discontinue')"
            [disabled]="actionLoading">Discontinue</button>
        </div>
      </div>

      <!-- Edit Form -->
      <div class="card">
        <h3>Edit Product</h3>
        <form (ngSubmit)="save()" class="edit-form">
          <div class="form-grid">
            <div class="form-group">
              <label for="name">Name</label>
              <input id="name" [(ngModel)]="editModel.name" name="name" required>
            </div>
            <div class="form-group">
              <label for="brand">Brand</label>
              <input id="brand" [(ngModel)]="editModel.brand" name="brand">
            </div>
            <div class="form-group">
              <label for="sku">SKU</label>
              <input id="sku" [(ngModel)]="editModel.sku" name="sku">
            </div>
            <div class="form-group">
              <label for="categoryId">Category ID</label>
              <input id="categoryId" [(ngModel)]="editModel.categoryId" name="categoryId">
            </div>
            <div class="form-group full-width">
              <label for="description">Description</label>
              <textarea id="description" [(ngModel)]="editModel.description" name="description" rows="3"></textarea>
            </div>
          </div>
          <div class="form-actions">
            <button type="submit" class="btn btn-primary" [disabled]="saving">
              {{ saving ? 'Saving...' : 'Save Changes' }}
            </button>
          </div>
        </form>
      </div>
    </app-detail-layout>
  `,
  styles: [`
    .loading { padding: 3rem; text-align: center; color: #6b7280; }
    .error-msg { padding: 1.5rem; margin: 1.5rem; background: #fee2e2; color: #991b1b; border-radius: 8px; }
    .card {
      background: #fff; border: 1px solid #e5e7eb; border-radius: 8px;
      padding: 1.25rem; margin-bottom: 0;
    }
    .card h3 { margin: 0 0 1rem; font-size: 1rem; color: #1a1a2e; }
    .field-grid { display: grid; grid-template-columns: 1fr 1fr; gap: 1rem; }
    .field label { display: block; font-size: 0.75rem; text-transform: uppercase; color: #6b7280; font-weight: 600; letter-spacing: 0.05em; margin-bottom: 0.2rem; }
    .field span { font-size: 0.95rem; color: #111827; }
    .full-width { grid-column: 1 / -1; }
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
    .edit-form { }
    .form-grid { display: grid; grid-template-columns: 1fr 1fr; gap: 1rem; margin-bottom: 1rem; }
    .form-group label { display: block; font-size: 0.8rem; font-weight: 600; color: #374151; margin-bottom: 0.3rem; }
    .form-group input, .form-group textarea, .form-group select {
      width: 100%; padding: 0.5rem 0.75rem; border: 1px solid #d1d5db; border-radius: 6px;
      font-size: 0.9rem; color: #111827; background: #fff; box-sizing: border-box;
    }
    .form-group input:focus, .form-group textarea:focus, .form-group select:focus {
      outline: none; border-color: #e94560; box-shadow: 0 0 0 2px rgba(233,69,96,0.15);
    }
    .form-actions { display: flex; justify-content: flex-end; }
  `],
})
export class ProductDetailComponent implements OnInit {
  product: any = null;
  editModel: any = {};
  loading = true;
  error = '';
  saving = false;
  actionLoading = false;
  private id = '';

  constructor(
    private route: ActivatedRoute,
    private api: ApiService,
    private toast: ToastService,
  ) {}

  ngOnInit(): void {
    this.id = this.route.snapshot.paramMap.get('id')!;
    this.loadProduct();
  }

  loadProduct(): void {
    this.loading = true;
    this.api.get<any>('product', this.id).subscribe({
      next: data => {
        this.product = data;
        this.editModel = { ...data };
        this.loading = false;
      },
      error: () => {
        this.error = 'Failed to load product.';
        this.loading = false;
      },
    });
  }

  save(): void {
    this.saving = true;
    const body = {
      name: this.editModel.name,
      description: this.editModel.description,
      brand: this.editModel.brand,
      sku: this.editModel.sku,
      categoryId: this.editModel.categoryId,
    };
    this.api.update<any>('product', this.id, body).subscribe({
      next: data => {
        this.product = data;
        this.editModel = { ...data };
        this.saving = false;
        this.toast.success('Product updated successfully.');
      },
      error: () => {
        this.saving = false;
        this.toast.error('Failed to update product.');
      },
    });
  }

  triggerEvent(event: string): void {
    this.actionLoading = true;
    this.api.triggerEvent<any>('product', this.id, event).subscribe({
      next: data => {
        this.product = data;
        this.editModel = { ...data };
        this.actionLoading = false;
        this.toast.success(`Product ${event} successful.`);
      },
      error: () => {
        this.actionLoading = false;
        this.toast.error(`Failed to ${event} product.`);
      },
    });
  }
}
