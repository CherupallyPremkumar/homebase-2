import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { SidebarComponent } from './shared/components/sidebar/sidebar.component';
import { HeaderComponent } from './shared/components/header/header.component';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, SidebarComponent, HeaderComponent],
  template: `
    <div class="admin-layout">
      <app-sidebar />
      <div class="main-area">
        <app-header />
        <div class="content-area">
          <router-outlet />
        </div>
      </div>
    </div>
  `,
  styles: [`
    .admin-layout { display: flex; min-height: 100vh; }
    .main-area { margin-left: 240px; flex: 1; background: #f9fafb; }
    .content-area { padding: 0; }
  `],
})
export class AppComponent {}
