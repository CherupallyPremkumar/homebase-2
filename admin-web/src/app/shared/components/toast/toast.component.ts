import { Component, Injectable, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Subject, Subscription } from 'rxjs';

export interface ToastMessage {
  id: number;
  message: string;
  type: 'success' | 'error' | 'info' | 'warning';
}

@Injectable({ providedIn: 'root' })
export class ToastService {
  private _messages$ = new Subject<ToastMessage>();
  messages$ = this._messages$.asObservable();
  private counter = 0;

  show(message: string, type: 'success' | 'error' | 'info' | 'warning' = 'info'): void {
    this._messages$.next({ id: ++this.counter, message, type });
  }

  success(message: string): void { this.show(message, 'success'); }
  error(message: string): void { this.show(message, 'error'); }
  warning(message: string): void { this.show(message, 'warning'); }
  info(message: string): void { this.show(message, 'info'); }
}

@Component({
  selector: 'app-toast',
  standalone: true,
  imports: [CommonModule],
  template: `
    <div class="toast-container">
      @for (toast of toasts; track toast.id) {
        <div class="toast" [ngClass]="'toast-' + toast.type" (click)="dismiss(toast.id)">
          <span class="toast-icon">
            @switch (toast.type) {
              @case ('success') { &#10003; }
              @case ('error') { &#10007; }
              @case ('warning') { &#9888; }
              @default { &#8505; }
            }
          </span>
          <span class="toast-message">{{ toast.message }}</span>
          <button class="toast-close" (click)="dismiss(toast.id)">&times;</button>
        </div>
      }
    </div>
  `,
  styles: [`
    .toast-container {
      position: fixed; top: 1rem; right: 1rem; z-index: 10000;
      display: flex; flex-direction: column; gap: 0.5rem;
      max-width: 380px; pointer-events: none;
    }
    .toast {
      display: flex; align-items: center; gap: 0.5rem;
      padding: 0.75rem 1rem; border-radius: 8px;
      box-shadow: 0 4px 12px rgba(0,0,0,0.15);
      font-size: 0.9rem; pointer-events: auto; cursor: pointer;
      animation: slideIn 0.3s ease-out;
    }
    @keyframes slideIn {
      from { transform: translateX(100%); opacity: 0; }
      to { transform: translateX(0); opacity: 1; }
    }
    .toast-success { background: #065f46; color: #fff; }
    .toast-error { background: #991b1b; color: #fff; }
    .toast-warning { background: #92400e; color: #fff; }
    .toast-info { background: #1a1a2e; color: #fff; }
    .toast-icon { font-size: 1rem; flex-shrink: 0; }
    .toast-message { flex: 1; }
    .toast-close {
      background: none; border: none; color: inherit; font-size: 1.2rem;
      cursor: pointer; opacity: 0.7; padding: 0; line-height: 1;
    }
    .toast-close:hover { opacity: 1; }
  `],
})
export class ToastComponent implements OnDestroy {
  toasts: ToastMessage[] = [];
  private sub: Subscription;

  constructor(private toastService: ToastService) {
    this.sub = this.toastService.messages$.subscribe(toast => {
      this.toasts.push(toast);
      setTimeout(() => this.dismiss(toast.id), 4000);
    });
  }

  dismiss(id: number): void {
    this.toasts = this.toasts.filter(t => t.id !== id);
  }

  ngOnDestroy(): void {
    this.sub.unsubscribe();
  }
}
