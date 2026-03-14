import { Routes } from '@angular/router';
import { ProductGalleryComponent } from './components/product-gallery/product-gallery';
import { ProductDetailComponent } from './components/product-detail/product-detail';

export const routes: Routes = [
  { path: '', redirectTo: 'gallery', pathMatch: 'full' },
  { path: 'gallery', component: ProductGalleryComponent },
  { path: 'product/:id', component: ProductDetailComponent },
  { path: '**', redirectTo: 'gallery' }
];
