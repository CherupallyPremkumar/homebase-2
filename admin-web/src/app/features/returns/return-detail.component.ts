import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterLink, ActivatedRoute } from '@angular/router';
import { ApiService } from '../../core/services/api.service';
import { DetailLayoutComponent } from '../../shared/components/detail-layout/detail-layout.component';
import { ToastService } from '../../shared/components/toast/toast.component';

@Component({
  selector: 'app-return-detail',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink, DetailLayoutComponent],
  template: `
    <div class="loading" *ngIf="loading">Loading return request...</div>
    <div class="error-msg" *ngIf="error">{{ error }}</div>

    <app-detail-layout *ngIf="returnReq"
      [title]="'Return #' + returnReq.id"
      [subtitle]="'Order: ' + returnReq.orderId"
      [status]="returnReq.stateId ?? ''">

      <!-- Return Info -->
      <div class="card">
        <h3>Return Details</h3>
        <div class="field-grid">
          <div class="field">
            <label>Order ID</label>
            <span><a [routerLink]="['/orders', returnReq.orderId]">{{ returnReq.orderId }}</a></span>
          </div>
          <div class="field">
            <label>Order Item ID</label>
            <span>{{ returnReq.orderItemId }}</span>
          </div>
          <div class="field">
            <label>Return Type</label>
            <span>{{ returnReq.returnType ?? '---' }}</span>
          </div>
          <div class="field">
            <label>Quantity</label>
            <span>{{ returnReq.quantity }}</span>
          </div>
          <div class="field full-width">
            <label>Reason</label>
            <span>{{ returnReq.reason ?? 'No reason provided' }}</span>
          </div>
          <div class="field" *ngIf="returnReq.refundAmount != null">
            <label>Refund Amount</label>
            <span class="amount">INR {{ returnReq.refundAmount | number:'1.2-2' }}</span>
          </div>
        </div>
      </div>

      <!-- Action: Approve / Reject -->
      <div class="card">
        <h3>Review Return</h3>
        <div class="form-group" style="margin-bottom: 1rem;">
          <label for="comment">Comment</label>
          <textarea id="comment" [(ngModel)]="comment" name="comment" rows="3"
            placeholder="Add a comment for the customer..."></textarea>
        </div>
        <div class="action-buttons">
          <button class="btn btn-success" (click)="triggerEvent('approve')"
            [disabled]="actionLoading">Approve Return</button>
          <button class="btn btn-danger" (click)="triggerEvent('reject')"
            [disabled]="actionLoading">Reject Return</button>
        </div>
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
    .field a { color: #e94560; text-decoration: none; font-weight: 500; }
    .field a:hover { text-decoration: underline; }
    .amount { font-weight: 700; font-size: 1.1rem; color: #1a1a2e; }
    .full-width { grid-column: 1 / -1; }
    .action-buttons { display: flex; gap: 0.75rem; flex-wrap: wrap; }
    .btn {
      padding: 0.5rem 1.2rem; border: none; border-radius: 6px;
      font-size: 0.85rem; font-weight: 600; cursor: pointer; transition: opacity 0.15s;
    }
    .btn:disabled { opacity: 0.5; cursor: not-allowed; }
    .btn-success { background: #065f46; color: #fff; }
    .btn-success:hover:not(:disabled) { background: #047857; }
    .btn-danger { background: #991b1b; color: #fff; }
    .btn-danger:hover:not(:disabled) { background: #b91c1c; }
    .form-group label { display: block; font-size: 0.8rem; font-weight: 600; color: #374151; margin-bottom: 0.3rem; }
    .form-group textarea {
      width: 100%; padding: 0.5rem 0.75rem; border: 1px solid #d1d5db; border-radius: 6px;
      font-size: 0.9rem; color: #111827; box-sizing: border-box; resize: vertical;
    }
    .form-group textarea:focus { outline: none; border-color: #e94560; box-shadow: 0 0 0 2px rgba(233,69,96,0.15); }
  `],
})
export class ReturnDetailComponent implements OnInit {
  returnReq: any = null;
  comment = '';
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
    this.loadReturn();
  }

  loadReturn(): void {
    this.loading = true;
    this.api.get<any>('returnRequest', this.id).subscribe({
      next: data => { this.returnReq = data; this.loading = false; },
      error: () => { this.error = 'Failed to load return request.'; this.loading = false; },
    });
  }

  triggerEvent(event: string): void {
    this.actionLoading = true;
    const payload = this.comment ? { comment: this.comment } : {};
    this.api.triggerEvent<any>('returnRequest', this.id, event, payload).subscribe({
      next: data => {
        this.returnReq = data;
        this.actionLoading = false;
        this.toast.success(`Return ${event} successful.`);
      },
      error: () => {
        this.actionLoading = false;
        this.toast.error(`Failed to ${event} return.`);
      },
    });
  }
}
