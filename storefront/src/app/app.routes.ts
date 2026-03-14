import { Routes } from '@angular/router';
import { authGuard } from './core/guards/auth.guard';

export const routes: Routes = [
  {
    path: '',
    loadComponent: () =>
      import('./features/home/home.component').then((m) => m.HomeComponent),
  },
  {
    path: 'products',
    loadComponent: () =>
      import('./features/product/product-list.component').then((m) => m.ProductListComponent),
  },
  {
    path: 'products/:id',
    loadComponent: () =>
      import('./features/product/product-detail.component').then((m) => m.ProductDetailComponent),
  },
  {
    path: 'categories',
    loadComponent: () =>
      import('./features/catalog/catalog.component').then((m) => m.CatalogComponent),
  },
  {
    path: 'category/:slug',
    loadComponent: () =>
      import('./features/catalog/catalog.component').then((m) => m.CatalogComponent),
  },
  {
    path: 'cart',
    loadComponent: () =>
      import('./features/cart/cart.component').then((m) => m.CartComponent),
  },
  {
    path: 'checkout',
    canActivate: [authGuard],
    loadComponent: () =>
      import('./features/checkout/checkout.component').then((m) => m.CheckoutComponent),
  },
  {
    path: 'checkout/success',
    canActivate: [authGuard],
    loadComponent: () =>
      import('./features/checkout/checkout-result.component').then((m) => m.CheckoutResultComponent),
  },
  {
    path: 'checkout/failure',
    loadComponent: () =>
      import('./features/checkout/checkout-result.component').then((m) => m.CheckoutResultComponent),
  },
  {
    path: 'orders',
    canActivate: [authGuard],
    loadComponent: () =>
      import('./features/order/order-list.component').then((m) => m.OrderListComponent),
  },
  {
    path: 'orders/:id',
    canActivate: [authGuard],
    loadComponent: () =>
      import('./features/order/order-detail.component').then((m) => m.OrderDetailComponent),
  },
  {
    path: 'profile',
    canActivate: [authGuard],
    loadComponent: () =>
      import('./features/user/profile.component').then((m) => m.ProfileComponent),
  },
  {
    path: '**',
    redirectTo: '',
  },
];
