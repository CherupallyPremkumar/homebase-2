import { Component, Injectable, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Subject, Subscription } from 'rxjs';

export type ToastType = 'success' | 'error' | 'info';

export interface ToastMessage {
  id: number;
  text: string;
  type: ToastType;
}

@Injectable({ providedIn: 'root' })
export class ToastService {
  private counter = 0;
  private toastSubject = new Subject<ToastMessage>();
  private dismissSubject = new Subject<number>();

  toast$ = this.toastSubject.asObservable();
  dismiss$ = this.dismissSubject.asObservable();

  success(text: string): void {
    this.show(text, 'success');
  }

  error(text: string): void {
    this.show(text, 'error');
  }

  info(text: string): void {
    this.show(text, 'info');
  }

  private show(text: string, type: ToastType): void {
    const id = ++this.counter;
    this.toastSubject.next({ id, text, type });
    setTimeout(() => this.dismissSubject.next(id), 3000);
  }
}

@Component({
  selector: 'app-toast',
  standalone: true,
  imports: [CommonModule],
  template: `
    <div class="toast-container">
      @for (toast of toasts; track toast.id) {
        <div class="toast" [class]="'toast-' + toast.type" (click)="dismiss(toast.id)">
          <span class="toast-icon">
            @if (toast.type === 'success') { &#10003; }
            @else if (toast.type === 'error') { &#10007; }
            @else { &#8505; }
          </span>
          <span class="toast-text">{{ toast.text }}</span>
        </div>
      }
    </div>
  `,
  styles: [`
    .toast-container {
      position: fixed;
      top: 1rem;
      right: 1rem;
      z-index: 9999;
      display: flex;
      flex-direction: column;
      gap: 0.5rem;
      max-width: 380px;
    }
    .toast {
      display: flex;
      align-items: center;
      gap: 0.75rem;
      padding: 0.75rem 1rem;
      border-radius: 8px;
      box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
      cursor: pointer;
      animation: slideIn 0.3s ease-out;
      font-size: 0.95rem;
    }
    .toast-success {
      background: #ecfdf5;
      border: 1px solid #a7f3d0;
      color: #065f46;
    }
    .toast-error {
      background: #fef2f2;
      border: 1px solid #fecaca;
      color: #991b1b;
    }
    .toast-info {
      background: #eff6ff;
      border: 1px solid #bfdbfe;
      color: #1e40af;
    }
    .toast-icon { font-weight: 700; font-size: 1.1rem; flex-shrink: 0; }
    .toast-text { flex: 1; }
    @keyframes slideIn {
      from { transform: translateX(100%); opacity: 0; }
      to { transform: translateX(0); opacity: 1; }
    }
  `],
})
export class ToastComponent implements OnDestroy {
  toasts: ToastMessage[] = [];
  private subs: Subscription[] = [];

  constructor(private toastService: ToastService) {
    this.subs.push(
      this.toastService.toast$.subscribe((toast) => this.toasts.push(toast)),
      this.toastService.dismiss$.subscribe((id) => this.dismiss(id))
    );
  }

  dismiss(id: number): void {
    this.toasts = this.toasts.filter((t) => t.id !== id);
  }

  ngOnDestroy(): void {
    this.subs.forEach((s) => s.unsubscribe());
  }
}
