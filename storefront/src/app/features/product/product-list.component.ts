import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { ProductService } from '../../core/services/product.service';
import { Product } from '../../shared/models/api.models';

@Component({
  selector: 'app-product-list',
  standalone: true,
  imports: [CommonModule, RouterLink, FormsModule],
  template: `
    <div class="container">
      <div class="header">
        <h1>Products</h1>
        <div class="search-bar">
          <input
            type="text"
            [(ngModel)]="searchTerm"
            (keyup.enter)="search()"
            placeholder="Search products..."
          />
          <button (click)="search()">Search</button>
        </div>
      </div>

      @if (loading) {
        <p class="loading">Loading products...</p>
      } @else if (products.length === 0) {
        <p class="empty">No products found.</p>
      } @else {
        <div class="product-grid">
          @for (product of products; track product.id) {
            <a [routerLink]="['/products', product.id]" class="product-card">
              <div class="product-image">
                @if (product.imageUrl) {
                  <img [src]="product.imageUrl" [alt]="product.name" />
                }
              </div>
              <div class="product-info">
                <h3>{{ product.name }}</h3>
                @if (product.brand) {
                  <span class="brand">{{ product.brand }}</span>
                }
                <p class="price">{{ product.price | currency:'INR' }}</p>
              </div>
            </a>
          }
        </div>

        <div class="pagination">
          <button [disabled]="page === 0" (click)="loadPage(page - 1)">Previous</button>
          <span>Page {{ page + 1 }} of {{ totalPages }}</span>
          <button [disabled]="page >= totalPages - 1" (click)="loadPage(page + 1)">Next</button>
        </div>
      }
    </div>
  `,
  styles: [`
    .container { max-width: 1200px; margin: 2rem auto; padding: 0 1rem; }
    .header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 2rem; }
    .header h1 { margin: 0; color: #1a1a2e; }
    .search-bar { display: flex; gap: 0.5rem; }
    .search-bar input {
      padding: 0.5rem 1rem;
      border: 1px solid #d1d5db;
      border-radius: 6px;
      width: 300px;
    }
    .search-bar button {
      padding: 0.5rem 1rem;
      background: #1a1a2e;
      color: #fff;
      border: none;
      border-radius: 6px;
      cursor: pointer;
    }
    .product-grid {
      display: grid;
      grid-template-columns: repeat(auto-fill, minmax(250px, 1fr));
      gap: 1.5rem;
    }
    .product-card {
      border: 1px solid #e5e7eb;
      border-radius: 12px;
      overflow: hidden;
      text-decoration: none;
      color: inherit;
      transition: box-shadow 0.2s;
    }
    .product-card:hover { box-shadow: 0 4px 12px rgba(0,0,0,0.1); }
    .product-image {
      background: #f3f4f6;
      height: 220px;
      display: flex;
      align-items: center;
      justify-content: center;
    }
    .product-image img { max-height: 100%; max-width: 100%; object-fit: cover; }
    .product-info { padding: 1rem; }
    .product-info h3 { margin: 0 0 0.25rem; font-size: 1rem; }
    .brand { color: #6b7280; font-size: 0.85rem; }
    .price { color: #1a1a2e; font-weight: 600; margin: 0.5rem 0 0; }
    .pagination {
      display: flex;
      align-items: center;
      justify-content: center;
      gap: 1rem;
      margin-top: 2rem;
    }
    .pagination button {
      padding: 0.5rem 1rem;
      border: 1px solid #d1d5db;
      border-radius: 6px;
      background: #fff;
      cursor: pointer;
    }
    .pagination button:disabled { opacity: 0.5; cursor: not-allowed; }
    .loading, .empty { text-align: center; color: #6b7280; padding: 3rem; }
  `],
})
export class ProductListComponent implements OnInit {
  products: Product[] = [];
  searchTerm = '';
  page = 0;
  totalPages = 1;
  loading = true;

  constructor(private productService: ProductService) {}

  ngOnInit(): void {
    this.loadPage(0);
  }

  loadPage(page: number): void {
    this.page = page;
    this.loading = true;
    const filters = this.searchTerm ? { name: this.searchTerm } : {};
    this.productService.searchProducts(filters, page).subscribe({
      next: (res) => {
        this.products = res.items ?? [];
        this.totalPages = Math.ceil((res.totalCount ?? 1) / (res.numRowsInPage ?? 20));
        this.loading = false;
      },
      error: () => {
        this.loading = false;
      },
    });
  }

  search(): void {
    this.loadPage(0);
  }
}
