import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterLink, ActivatedRoute } from '@angular/router';
import { ApiService } from '../../core/services/api.service';
import { DetailLayoutComponent } from '../../shared/components/detail-layout/detail-layout.component';
import { ToastService } from '../../shared/components/toast/toast.component';

@Component({
  selector: 'app-supplier-detail',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink, DetailLayoutComponent],
  template: `
    <div class="loading" *ngIf="loading">Loading supplier...</div>
    <div class="error-msg" *ngIf="error">{{ error }}</div>

    <app-detail-layout *ngIf="supplier"
      [title]="supplier.name || 'Supplier'"
      [subtitle]="'ID: ' + supplier.id"
      [status]="supplier.stateId ?? ''">

      <!-- Supplier Info -->
      <div class="card">
        <h3>Supplier Information</h3>
        <div class="field-grid">
          <div class="field">
            <label>Name</label>
            <span>{{ supplier.name }}</span>
          </div>
          <div class="field">
            <label>Email</label>
            <span>{{ supplier.email ?? '---' }}</span>
          </div>
          <div class="field">
            <label>Phone</label>
            <span>{{ supplier.phone ?? '---' }}</span>
          </div>
          <div class="field">
            <label>User ID</label>
            <span>{{ supplier.userId ?? '---' }}</span>
          </div>
          <div class="field">
            <label>Commission %</label>
            <span class="highlight">{{ supplier.commissionPercentage ?? 0 }}%</span>
          </div>
          <div class="field">
            <label>UPI ID</label>
            <span>{{ supplier.upiId ?? '---' }}</span>
          </div>
          <div class="field full-width">
            <label>Address</label>
            <span>{{ supplier.address ?? 'No address provided' }}</span>
          </div>
          <div class="field full-width" *ngIf="supplier.description">
            <label>Description</label>
            <span>{{ supplier.description }}</span>
          </div>
        </div>
      </div>

      <!-- Actions -->
      <div class="card">
        <h3>Actions</h3>
        <div class="action-buttons">
          <button class="btn btn-success" (click)="triggerEvent('approve')"
            [disabled]="actionLoading">Approve</button>
          <button class="btn btn-danger" (click)="triggerEvent('reject')"
            [disabled]="actionLoading">Reject</button>
          <button class="btn btn-warning" (click)="triggerEvent('suspend')"
            [disabled]="actionLoading">Suspend</button>
        </div>
      </div>

      <!-- Edit Commission -->
      <div class="card">
        <h3>Edit Commission</h3>
        <form (ngSubmit)="saveCommission()" class="edit-form">
          <div class="form-row">
            <div class="form-group">
              <label for="commission">Commission Percentage</label>
              <input id="commission" type="number" min="0" max="100" step="0.1"
                [(ngModel)]="editCommission" name="commission" required>
            </div>
            <button type="submit" class="btn btn-primary" [disabled]="saving">
              {{ saving ? 'Saving...' : 'Update Commission' }}
            </button>
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
    .field-grid { display: grid; grid-template-columns: 1fr 1fr; gap: 1rem; }
    .field label { display: block; font-size: 0.75rem; text-transform: uppercase; color: #6b7280; font-weight: 600; letter-spacing: 0.05em; margin-bottom: 0.2rem; }
    .field span { font-size: 0.95rem; color: #111827; }
    .highlight { font-weight: 700; color: #e94560; font-size: 1.1rem; }
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
    .form-row { display: flex; align-items: flex-end; gap: 1rem; }
    .form-group { flex: 1; max-width: 300px; }
    .form-group label { display: block; font-size: 0.8rem; font-weight: 600; color: #374151; margin-bottom: 0.3rem; }
    .form-group input {
      width: 100%; padding: 0.5rem 0.75rem; border: 1px solid #d1d5db; border-radius: 6px;
      font-size: 0.9rem; color: #111827; box-sizing: border-box;
    }
    .form-group input:focus { outline: none; border-color: #e94560; box-shadow: 0 0 0 2px rgba(233,69,96,0.15); }
  `],
})
export class SupplierDetailComponent implements OnInit {
  supplier: any = null;
  editCommission = 0;
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
    this.loadSupplier();
  }

  loadSupplier(): void {
    this.loading = true;
    this.api.get<any>('supplier', this.id).subscribe({
      next: data => {
        this.supplier = data;
        this.editCommission = data.commissionPercentage ?? 0;
        this.loading = false;
      },
      error: () => { this.error = 'Failed to load supplier.'; this.loading = false; },
    });
  }

  saveCommission(): void {
    this.saving = true;
    this.api.update<any>('supplier', this.id, { commissionPercentage: this.editCommission }).subscribe({
      next: data => {
        this.supplier = data;
        this.saving = false;
        this.toast.success('Commission updated successfully.');
      },
      error: () => { this.saving = false; this.toast.error('Failed to update commission.'); },
    });
  }

  triggerEvent(event: string): void {
    this.actionLoading = true;
    this.api.triggerEvent<any>('supplier', this.id, event).subscribe({
      next: data => {
        this.supplier = data;
        this.actionLoading = false;
        this.toast.success(`Supplier ${event} successful.`);
      },
      error: () => {
        this.actionLoading = false;
        this.toast.error(`Failed to ${event} supplier.`);
      },
    });
  }
}
