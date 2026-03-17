import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';

@Component({
    selector: 'app-nav',
    standalone: true,
    imports: [CommonModule, RouterModule],
    template: `
        <nav class="sidebar">
            <div class="sidebar-header">
                <div class="logo">
                    <span class="logo-io">IO</span><span class="logo-click">POLICY</span>
                </div>
            </div>
            <ul class="nav-links">
                <li>
                    <a routerLink="/policies" routerLinkActive="active">
                        <span class="icon">📜</span> Policies
                    </a>
                </li>
                <li>
                    <a routerLink="/facts" routerLinkActive="active">
                        <span class="icon">🔍</span> Fact Catalog
                    </a>
                </li>
                <li>
                    <a routerLink="/audit-log" routerLinkActive="active">
                        <span class="icon">📝</span> Audit Log
                    </a>
                </li>
                <li>
                    <a routerLink="/sandbox" routerLinkActive="active">
                        <span class="icon">🧪</span> Testing Sandbox
                    </a>
                </li>
            </ul>
        </nav>
    `,
    styles: [`
        .sidebar {
            width: 260px;
            height: 100vh;
            background: #1a1c23;
            color: #fff;
            padding: 20px 0;
            position: fixed;
            left: 0;
            top: 0;
            box-shadow: 4px 0 10px rgba(0,0,0,0.1);
        }
        .sidebar-header {
            padding: 0 25px 30px;
            border-bottom: 1px solid #2d2f39;
            margin-bottom: 20px;
        }
        .logo {
            font-size: 1.5rem;
            font-weight: 800;
            letter-spacing: 1px;
        }
        .logo-io { color: #fff; }
        .logo-click { color: #3498db; }
        
        .nav-links {
            list-style: none;
            padding: 0;
            margin: 0;
        }
        .nav-links li {
            margin-bottom: 5px;
        }
        .nav-links a {
            display: flex;
            align-items: center;
            padding: 12px 25px;
            color: #b2b5be;
            text-decoration: none;
            transition: all 0.3s;
            font-weight: 500;
        }
        .nav-links a:hover {
            color: #fff;
            background: rgba(255,255,255,0.05);
        }
        .nav-links a.active {
            color: #fff;
            background: #3498db;
            border-left: 4px solid #fff;
        }
        .icon {
            margin-right: 12px;
            font-size: 1.2rem;
        }
    `]
})
export class AppNavComponent {}
