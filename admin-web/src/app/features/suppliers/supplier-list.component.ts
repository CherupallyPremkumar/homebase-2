import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { QueryService } from '../../core/services/query.service';
import { ApiService } from '../../core/services/api.service';

@Component({
  selector: 'app-supplier-list',
  standalone: true,
  imports: [CommonModule, RouterLink],
  template: `
    <div class="admin-page">
      <div class="page-header">
        <h2>Suppliers</h2>
        <span class="count" *ngIf="totalCount">{{ totalCount }} total</span>
      </div>
      <div class="card">
        <table class="table">
          <thead>
            <tr>
              <th>ID</th>
              <th>Name</th>
              <th>Email</th>
              <th>Commission %</th>
              <th>State</th>
              <th>Actions</th>
            </tr>
          </thead>
          <tbody>
            @for (item of items; track item.id) {
              <tr>
                <td><a [routerLink]="['/suppliers', item.id]">{{ item.id }}</a></td>
                <td>{{ item.name }}</td>
                <td>{{ item.email }}</td>
                <td>{{ item.commissionPercentage }}%</td>
                <td><span class="status" [ngClass]="'status-' + (item.stateId ?? '').toLowerCase()">{{ item.stateId }}</span></td>
                <td class="actions">
                  @if (item.stateId === 'PENDING') {
                    <button class="btn btn-approve" (click)="triggerEvent(item.id, 'approve')">Approve</button>
                    <button class="btn btn-reject" (click)="triggerEvent(item.id, 'reject')">Reject</button>
                  }
                </td>
              </tr>
            }
            @if (items.length === 0 && !loading) {
              <tr><td colspan="6" class="empty">No records found.</td></tr>
            }
          </tbody>
        </table>
        <div class="pagination">
          <button [disabled]="page === 0" (click)="load(page - 1)">Prev</button>
          <span>Page {{ page + 1 }}</span>
          <button [disabled]="items.length < 20" (click)="load(page + 1)">Next</button>
        </div>
      </div>
    </div>
  `,
  styles: [`
    .admin-page { padding: 1.5rem; }
    .page-header { display: flex; align-items: center; gap: 1rem; margin-bottom: 1.5rem; }
    .page-header h2 { margin: 0; font-size: 1.3rem; color: #1a1a2e; }
    .count { background: #f3f4f6; padding: 0.25rem 0.75rem; border-radius: 20px; font-size: 0.8rem; color: #6b7280; }
    .card { background: #fff; border: 1px solid #e5e7eb; border-radius: 8px; overflow: hidden; }
    .table { width: 100%; border-collapse: collapse; }
    .table th { background: #f9fafb; padding: 0.75rem 1rem; text-align: left; font-size: 0.8rem; text-transform: uppercase; color: #6b7280; font-weight: 600; letter-spacing: 0.05em; }
    .table td { padding: 0.75rem 1rem; border-top: 1px solid #f3f4f6; font-size: 0.9rem; }
    .table tr:hover { background: #f9fafb; }
    .table a { color: #1a1a2e; font-weight: 500; text-decoration: none; }
    .table a:hover { text-decoration: underline; }
    .status { padding: 0.2rem 0.5rem; border-radius: 4px; font-size: 0.75rem; font-weight: 600; }
    .status-active, .status-approved { background: #d1fae5; color: #065f46; }
    .status-pending { background: #fef3c7; color: #92400e; }
    .status-rejected { background: #fee2e2; color: #991b1b; }
    .empty { text-align: center; color: #9ca3af; padding: 2rem !important; }
    .pagination { display: flex; align-items: center; justify-content: center; gap: 1rem; padding: 1rem; border-top: 1px solid #e5e7eb; }
    .pagination button { padding: 0.4rem 1rem; border: 1px solid #d1d5db; border-radius: 6px; background: #fff; cursor: pointer; }
    .pagination button:disabled { opacity: 0.4; cursor: not-allowed; }
    .actions { display: flex; gap: 0.5rem; }
    .btn { padding: 0.3rem 0.7rem; border: none; border-radius: 4px; font-size: 0.75rem; font-weight: 600; cursor: pointer; }
    .btn-approve { background: #d1fae5; color: #065f46; }
    .btn-approve:hover { background: #a7f3d0; }
    .btn-reject { background: #fee2e2; color: #991b1b; }
    .btn-reject:hover { background: #fecaca; }
  `],
})
export class SupplierListComponent implements OnInit {
  items: any[] = [];
  totalCount = 0;
  page = 0;
  loading = false;

  constructor(private queryService: QueryService, private apiService: ApiService) {}

  ngOnInit(): void { this.load(0); }

  load(page: number): void {
    this.loading = true;
    this.page = page;
    this.queryService.search('suppliers', { pageNum: page }).subscribe(r => {
      this.items = r.items;
      this.totalCount = r.totalCount;
      this.loading = false;
    });
  }

  triggerEvent(id: string, event: string): void {
    this.apiService.triggerEvent('suppliers', id, event).subscribe(() => {
      this.load(this.page);
    });
  }
}
