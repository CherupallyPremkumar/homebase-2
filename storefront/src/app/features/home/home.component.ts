import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { ProductService } from '../../core/services/product.service';
import { CatalogItem, Collection } from '../../shared/models/api.models';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [CommonModule, RouterLink],
  template: `
    <section class="hero">
      <div class="hero-content">
        <h1>Discover Handcrafted Products</h1>
        <p>Unique, artisan-made goods delivered to your doorstep</p>
        <a routerLink="/products" class="btn-primary">Shop Now</a>
      </div>
    </section>

    @if (featuredItems.length > 0) {
      <section class="section">
        <h2>Featured Products</h2>
        <div class="product-grid">
          @for (item of featuredItems; track item.id) {
            <a [routerLink]="['/products', item.productId]" class="product-card">
              <div class="product-image"></div>
              <h3>{{ item.name }}</h3>
              <p class="price">{{ item.price | currency:'INR' }}</p>
            </a>
          }
        </div>
      </section>
    }

    @if (collections.length > 0) {
      <section class="section">
        <h2>Collections</h2>
        <div class="collection-grid">
          @for (col of collections; track col.id) {
            <a [routerLink]="['/collection', col.slug]" class="collection-card">
              @if (col.imageUrl) {
                <img [src]="col.imageUrl" [alt]="col.name" />
              }
              <h3>{{ col.name }}</h3>
              <p>{{ col.description }}</p>
            </a>
          }
        </div>
      </section>
    }
  `,
  styles: [`
    .hero {
      background: linear-gradient(135deg, #1a1a2e 0%, #16213e 100%);
      color: #fff;
      padding: 5rem 1rem;
      text-align: center;
    }
    .hero h1 { font-size: 2.5rem; margin: 0 0 1rem; }
    .hero p { font-size: 1.2rem; color: #d1d5db; margin: 0 0 2rem; }
    .btn-primary {
      background: #fff;
      color: #1a1a2e;
      padding: 0.75rem 2rem;
      border-radius: 8px;
      text-decoration: none;
      font-weight: 600;
      transition: transform 0.2s;
    }
    .btn-primary:hover { transform: translateY(-2px); }
    .section {
      max-width: 1200px;
      margin: 3rem auto;
      padding: 0 1rem;
    }
    .section h2 {
      font-size: 1.5rem;
      margin: 0 0 1.5rem;
      color: #1a1a2e;
    }
    .product-grid {
      display: grid;
      grid-template-columns: repeat(auto-fill, minmax(250px, 1fr));
      gap: 1.5rem;
    }
    .product-card {
      border: 1px solid #e5e7eb;
      border-radius: 12px;
      padding: 1rem;
      text-decoration: none;
      color: inherit;
      transition: box-shadow 0.2s;
    }
    .product-card:hover { box-shadow: 0 4px 12px rgba(0,0,0,0.1); }
    .product-image {
      background: #f3f4f6;
      border-radius: 8px;
      height: 200px;
      margin-bottom: 1rem;
    }
    .product-card h3 { margin: 0 0 0.5rem; font-size: 1rem; }
    .price { color: #1a1a2e; font-weight: 600; margin: 0; }
    .collection-grid {
      display: grid;
      grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
      gap: 1.5rem;
    }
    .collection-card {
      border: 1px solid #e5e7eb;
      border-radius: 12px;
      padding: 1.5rem;
      text-decoration: none;
      color: inherit;
      transition: box-shadow 0.2s;
    }
    .collection-card:hover { box-shadow: 0 4px 12px rgba(0,0,0,0.1); }
    .collection-card img {
      width: 100%;
      height: 180px;
      object-fit: cover;
      border-radius: 8px;
      margin-bottom: 1rem;
    }
    .collection-card h3 { margin: 0 0 0.5rem; }
    .collection-card p { color: #6b7280; margin: 0; font-size: 0.9rem; }
  `],
})
export class HomeComponent implements OnInit {
  featuredItems: CatalogItem[] = [];
  collections: Collection[] = [];

  constructor(private productService: ProductService) {}

  ngOnInit(): void {
    this.productService.getFeaturedItems().subscribe({
      next: (res) => (this.featuredItems = res.items ?? []),
      error: () => {},
    });
    this.productService.getCollections().subscribe({
      next: (res) => (this.collections = res.items ?? []),
      error: () => {},
    });
  }
}
