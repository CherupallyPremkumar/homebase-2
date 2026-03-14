import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { ApiService } from '../../core/services/api.service';
import { DetailLayoutComponent } from '../../shared/components/detail-layout/detail-layout.component';
import { ToastService } from '../../shared/components/toast/toast.component';

@Component({
  selector: 'app-promo-create',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink, DetailLayoutComponent],
  template: `
    <app-detail-layout title="Create Promo Code" subtitle="Add a new promotional code">
      <div class="card">
        <form (ngSubmit)="submit()" #promoForm="ngForm">
          <div class="form-grid">
            <div class="form-group">
              <label for="code">Promo Code *</label>
              <input id="code" [(ngModel)]="model.code" name="code" required
                placeholder="e.g. SUMMER25" style="text-transform: uppercase;">
            </div>
            <div class="form-group">
              <label for="discountType">Discount Type *</label>
              <select id="discountType" [(ngModel)]="model.discountType" name="discountType" required>
                <option value="">Select type</option>
                <option value="PERCENTAGE">Percentage</option>
                <option value="FLAT">Flat Amount</option>
              </select>
            </div>
            <div class="form-group">
              <label for="discountValue">Discount Value *</label>
              <input id="discountValue" type="number" [(ngModel)]="model.discountValue" name="discountValue"
                required min="0" step="0.01" placeholder="e.g. 10">
            </div>
            <div class="form-group">
              <label for="minOrderAmount">Min Order Amount</label>
              <input id="minOrderAmount" type="number" [(ngModel)]="model.minOrderAmount" name="minOrderAmount"
                min="0" step="0.01" placeholder="e.g. 500">
            </div>
            <div class="form-group">
              <label for="maxDiscountAmount">Max Discount Amount</label>
              <input id="maxDiscountAmount" type="number" [(ngModel)]="model.maxDiscountAmount" name="maxDiscountAmount"
                min="0" step="0.01" placeholder="e.g. 200">
            </div>
            <div class="form-group">
              <label for="maxUsage">Max Usage</label>
              <input id="maxUsage" type="number" [(ngModel)]="model.maxUsage" name="maxUsage"
                min="1" step="1" placeholder="e.g. 100">
            </div>
            <div class="form-group">
              <label for="expiryDate">Expiry Date</label>
              <input id="expiryDate" type="date" [(ngModel)]="model.expiryDate" name="expiryDate">
            </div>
            <div class="form-group full-width">
              <label for="description">Description</label>
              <textarea id="description" [(ngModel)]="model.description" name="description" rows="3"
                placeholder="Describe the promotion..."></textarea>
            </div>
          </div>
          <div class="form-actions">
            <a routerLink="/promo" class="btn btn-secondary">Cancel</a>
            <button type="submit" class="btn btn-primary" [disabled]="saving || promoForm.invalid">
              {{ saving ? 'Creating...' : 'Create Promo Code' }}
            </button>
          </div>
        </form>
      </div>
    </app-detail-layout>
  `,
  styles: [`
    .card { background: #fff; border: 1px solid #e5e7eb; border-radius: 8px; padding: 1.5rem; }
    .form-grid { display: grid; grid-template-columns: 1fr 1fr; gap: 1rem; margin-bottom: 1.5rem; }
    .full-width { grid-column: 1 / -1; }
    .form-group label { display: block; font-size: 0.8rem; font-weight: 600; color: #374151; margin-bottom: 0.3rem; }
    .form-group input, .form-group textarea, .form-group select {
      width: 100%; padding: 0.5rem 0.75rem; border: 1px solid #d1d5db; border-radius: 6px;
      font-size: 0.9rem; color: #111827; background: #fff; box-sizing: border-box;
    }
    .form-group input:focus, .form-group textarea:focus, .form-group select:focus {
      outline: none; border-color: #e94560; box-shadow: 0 0 0 2px rgba(233,69,96,0.15);
    }
    .form-actions { display: flex; justify-content: flex-end; gap: 0.75rem; }
    .btn {
      padding: 0.5rem 1.2rem; border: none; border-radius: 6px;
      font-size: 0.85rem; font-weight: 600; cursor: pointer; text-decoration: none;
      display: inline-flex; align-items: center; transition: opacity 0.15s;
    }
    .btn:disabled { opacity: 0.5; cursor: not-allowed; }
    .btn-primary { background: #e94560; color: #fff; }
    .btn-primary:hover:not(:disabled) { background: #d63851; }
    .btn-secondary { background: #e5e7eb; color: #374151; }
    .btn-secondary:hover { background: #d1d5db; }
  `],
})
export class PromoCreateComponent {
  model: any = {
    code: '',
    description: '',
    discountType: '',
    discountValue: null,
    minOrderAmount: null,
    maxDiscountAmount: null,
    expiryDate: '',
    maxUsage: null,
  };
  saving = false;

  constructor(
    private api: ApiService,
    private router: Router,
    private toast: ToastService,
  ) {}

  submit(): void {
    this.saving = true;
    const body = { ...this.model };
    if (body.code) body.code = body.code.toUpperCase();
    this.api.create<any>('promo', body).subscribe({
      next: () => {
        this.toast.success('Promo code created successfully.');
        this.router.navigate(['/promo']);
      },
      error: () => {
        this.saving = false;
        this.toast.error('Failed to create promo code.');
      },
    });
  }
}
