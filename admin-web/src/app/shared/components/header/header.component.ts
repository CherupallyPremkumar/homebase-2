import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AuthService } from '../../../core/services/auth.service';

@Component({
  selector: 'app-header',
  standalone: true,
  imports: [CommonModule],
  template: `
    <header class="header">
      <div class="header-left">
        <h1 class="page-title">{{ title }}</h1>
      </div>
      <div class="header-right">
        <span class="user-name">{{ authService.userName }}</span>
        <button (click)="authService.logout()" class="btn-logout">Logout</button>
      </div>
    </header>
  `,
  styles: [`
    .header {
      height: 60px;
      background: #fff;
      border-bottom: 1px solid #e5e7eb;
      display: flex;
      align-items: center;
      justify-content: space-between;
      padding: 0 1.5rem;
    }
    .page-title { font-size: 1.1rem; font-weight: 600; color: #1a1a2e; margin: 0; }
    .header-right { display: flex; align-items: center; gap: 1rem; }
    .user-name { color: #6b7280; font-size: 0.9rem; }
    .btn-logout {
      background: none; border: 1px solid #d1d5db; padding: 0.35rem 0.75rem;
      border-radius: 6px; color: #4b5563; cursor: pointer; font-size: 0.85rem;
    }
    .btn-logout:hover { background: #f3f4f6; }
  `],
})
export class HeaderComponent {
  title = 'Dashboard';
  constructor(public authService: AuthService) {}
}
