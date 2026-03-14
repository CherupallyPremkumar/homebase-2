import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { AuthService } from '../../core/services/auth.service';

@Component({
  selector: 'app-profile',
  standalone: true,
  imports: [CommonModule, FormsModule],
  template: `
    <div class="container">
      <h1>My Profile</h1>

      @if (profile) {
        <div class="profile-card">
          <div class="avatar">
            {{ getInitials() }}
          </div>
          <div class="profile-info">
            <div class="form-group">
              <label>Email</label>
              <input type="email" [value]="profile.email" disabled />
            </div>
            <div class="form-row">
              <div class="form-group">
                <label>First Name</label>
                <input type="text" [value]="profile.given_name || ''" disabled />
              </div>
              <div class="form-group">
                <label>Last Name</label>
                <input type="text" [value]="profile.family_name || ''" disabled />
              </div>
            </div>
            <p class="hint">Profile is managed by your Keycloak account.
              <a href="http://localhost:8180/realms/homebase/account" target="_blank">
                Edit in Account Settings
              </a>
            </p>
          </div>
        </div>
      } @else {
        <p class="loading">Loading profile...</p>
      }
    </div>
  `,
  styles: [`
    .container { max-width: 600px; margin: 2rem auto; padding: 0 1rem; }
    h1 { color: #1a1a2e; margin-bottom: 1.5rem; }
    .profile-card {
      border: 1px solid #e5e7eb;
      border-radius: 12px;
      padding: 2rem;
    }
    .avatar {
      width: 80px; height: 80px; border-radius: 50%;
      background: #1a1a2e; color: #fff;
      display: flex; align-items: center; justify-content: center;
      font-size: 1.5rem; font-weight: 700;
      margin: 0 auto 1.5rem;
    }
    .form-group { margin-bottom: 1rem; }
    .form-group label { display: block; font-size: 0.85rem; color: #4b5563; margin-bottom: 0.25rem; font-weight: 500; }
    .form-group input {
      width: 100%; padding: 0.5rem 0.75rem;
      border: 1px solid #d1d5db; border-radius: 6px;
      background: #f9fafb;
    }
    .form-row { display: grid; grid-template-columns: 1fr 1fr; gap: 1rem; }
    .hint { color: #6b7280; font-size: 0.85rem; margin-top: 1rem; }
    .hint a { color: #1a1a2e; font-weight: 500; }
    .loading { text-align: center; color: #6b7280; padding: 3rem; }
  `],
})
export class ProfileComponent implements OnInit {
  profile: any = null;

  constructor(private authService: AuthService) {}

  ngOnInit(): void {
    this.authService.userProfile$.subscribe((p) => {
      this.profile = p?.info ?? p;
    });
  }

  getInitials(): string {
    if (!this.profile) return '';
    const first = this.profile.given_name?.[0] ?? '';
    const last = this.profile.family_name?.[0] ?? '';
    const initials = (first + last).toUpperCase();
    return initials || (this.profile.email?.[0]?.toUpperCase() ?? '?');
  }
}
