import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-spinner',
  standalone: true,
  imports: [CommonModule],
  template: `
    @if (overlay) {
      <div class="spinner-overlay">
        <div class="spinner" [style.width.px]="size" [style.height.px]="size"></div>
      </div>
    } @else {
      <div class="spinner-inline">
        <div class="spinner" [style.width.px]="size" [style.height.px]="size"></div>
      </div>
    }
  `,
  styles: [`
    .spinner-overlay {
      position: fixed;
      top: 0;
      left: 0;
      width: 100%;
      height: 100%;
      background: rgba(255, 255, 255, 0.8);
      display: flex;
      align-items: center;
      justify-content: center;
      z-index: 9000;
    }
    .spinner-inline {
      display: flex;
      align-items: center;
      justify-content: center;
      padding: 2rem;
    }
    .spinner {
      border: 3px solid #e5e7eb;
      border-top-color: #1a1a2e;
      border-radius: 50%;
      animation: spin 0.7s linear infinite;
    }
    @keyframes spin {
      to { transform: rotate(360deg); }
    }
  `],
})
export class SpinnerComponent {
  @Input() overlay = false;
  @Input() size = 40;
}
