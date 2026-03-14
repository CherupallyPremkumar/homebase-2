import { Routes } from '@angular/router';
import { adminGuard } from './core/guards/admin.guard';

export const routes: Routes = [
  { path: '', redirectTo: 'dashboard', pathMatch: 'full' },
  {
    path: 'dashboard', canActivate: [adminGuard],
    loadComponent: () => import('./features/dashboard/dashboard.component').then(m => m.DashboardComponent),
  },
  {
    path: 'orders', canActivate: [adminGuard],
    loadComponent: () => import('./features/orders/order-list.component').then(m => m.OrderListComponent),
  },
  {
    path: 'orders/:id', canActivate: [adminGuard],
    loadComponent: () => import('./features/orders/order-detail.component').then(m => m.OrderDetailComponent),
  },
  {
    path: 'products', canActivate: [adminGuard],
    loadComponent: () => import('./features/products/product-list.component').then(m => m.ProductListComponent),
  },
  {
    path: 'products/:id', canActivate: [adminGuard],
    loadComponent: () => import('./features/products/product-detail.component').then(m => m.ProductDetailComponent),
  },
  {
    path: 'inventory', canActivate: [adminGuard],
    loadComponent: () => import('./features/inventory/inventory-list.component').then(m => m.InventoryListComponent),
  },
  {
    path: 'inventory/:id', canActivate: [adminGuard],
    loadComponent: () => import('./features/inventory/inventory-detail.component').then(m => m.InventoryDetailComponent),
  },
  {
    path: 'customers', canActivate: [adminGuard],
    loadComponent: () => import('./features/customers/customer-list.component').then(m => m.CustomerListComponent),
  },
  {
    path: 'suppliers', canActivate: [adminGuard],
    loadComponent: () => import('./features/suppliers/supplier-list.component').then(m => m.SupplierListComponent),
  },
  {
    path: 'suppliers/:id', canActivate: [adminGuard],
    loadComponent: () => import('./features/suppliers/supplier-detail.component').then(m => m.SupplierDetailComponent),
  },
  {
    path: 'shipping', canActivate: [adminGuard],
    loadComponent: () => import('./features/shipping/shipping-list.component').then(m => m.ShippingListComponent),
  },
  {
    path: 'shipping/:id', canActivate: [adminGuard],
    loadComponent: () => import('./features/shipping/shipping-detail.component').then(m => m.ShippingDetailComponent),
  },
  {
    path: 'settlements', canActivate: [adminGuard],
    loadComponent: () => import('./features/settlements/settlement-list.component').then(m => m.SettlementListComponent),
  },
  {
    path: 'settlements/:id', canActivate: [adminGuard],
    loadComponent: () => import('./features/settlements/settlement-detail.component').then(m => m.SettlementDetailComponent),
  },
  {
    path: 'returns', canActivate: [adminGuard],
    loadComponent: () => import('./features/returns/return-list.component').then(m => m.ReturnListComponent),
  },
  {
    path: 'returns/:id', canActivate: [adminGuard],
    loadComponent: () => import('./features/returns/return-detail.component').then(m => m.ReturnDetailComponent),
  },
  {
    path: 'promo', canActivate: [adminGuard],
    loadComponent: () => import('./features/promo/promo-list.component').then(m => m.PromoListComponent),
  },
  {
    path: 'promo/create', canActivate: [adminGuard],
    loadComponent: () => import('./features/promo/promo-create.component').then(m => m.PromoCreateComponent),
  },
  {
    path: 'catalog', canActivate: [adminGuard],
    loadComponent: () => import('./features/catalog/catalog-list.component').then(m => m.CatalogListComponent),
  },
  {
    path: 'carts', canActivate: [adminGuard],
    loadComponent: () => import('./features/carts/cart-list.component').then(m => m.CartListComponent),
  },
  {
    path: 'unauthorized',
    loadComponent: () => import('./features/dashboard/dashboard.component').then(m => m.DashboardComponent),
  },
  { path: '**', redirectTo: 'dashboard' },
];
