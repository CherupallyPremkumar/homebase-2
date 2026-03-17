import { Routes } from '@angular/router';
import { PolicyListComponent } from './components/policy-list/policy-list.component';
import { PolicyDetailComponent } from './components/policy-detail/policy-detail.component';
import { AuditLogComponent } from './components/audit-log/audit-log';
import { TestingSandboxComponent } from './components/testing-sandbox/testing-sandbox';

export const routes: Routes = [
    { path: 'policies', component: PolicyListComponent },
    { path: 'policies/new', component: PolicyDetailComponent },
    { path: 'policies/:id', component: PolicyDetailComponent },
    { path: 'facts', loadComponent: () => import('./components/fact-list/fact-list.component').then(m => m.FactListComponent) },
    { path: 'audit-log', component: AuditLogComponent },
    { path: 'sandbox', component: TestingSandboxComponent },
    { path: '', redirectTo: '/policies', pathMatch: 'full' }
];
