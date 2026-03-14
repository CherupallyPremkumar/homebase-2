import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { ProductService } from '../../core/services/product.service';
import { Category } from '../../shared/models/api.models';

@Component({
  selector: 'app-catalog',
  standalone: true,
  imports: [CommonModule, RouterLink],
  template: `
    <div class="container">
      <h1>Categories</h1>
      @if (loading) {
        <p class="loading">Loading categories...</p>
      } @else if (categories.length === 0) {
        <p class="empty">No categories available.</p>
      } @else {
        <div class="category-grid">
          @for (cat of categories; track cat.id) {
            <a [routerLink]="['/category', cat.slug]" class="category-card">
              @if (cat.imageUrl) {
                <img [src]="cat.imageUrl" [alt]="cat.name" />
              } @else {
                <div class="placeholder-img"></div>
              }
              <h3>{{ cat.name }}</h3>
              @if (cat.description) {
                <p>{{ cat.description }}</p>
              }
            </a>
          }
        </div>
      }
    </div>
  `,
  styles: [`
    .container { max-width: 1200px; margin: 2rem auto; padding: 0 1rem; }
    h1 { color: #1a1a2e; margin-bottom: 1.5rem; }
    .category-grid {
      display: grid;
      grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
      gap: 1.5rem;
    }
    .category-card {
      border: 1px solid #e5e7eb;
      border-radius: 12px;
      overflow: hidden;
      text-decoration: none;
      color: inherit;
      transition: box-shadow 0.2s;
    }
    .category-card:hover { box-shadow: 0 4px 12px rgba(0,0,0,0.1); }
    .category-card img, .placeholder-img {
      width: 100%;
      height: 180px;
      object-fit: cover;
    }
    .placeholder-img { background: #f3f4f6; }
    .category-card h3 { margin: 1rem 1rem 0.25rem; font-size: 1.1rem; }
    .category-card p { margin: 0 1rem 1rem; color: #6b7280; font-size: 0.9rem; }
    .loading, .empty { text-align: center; color: #6b7280; padding: 3rem; }
  `],
})
export class CatalogComponent implements OnInit {
  categories: Category[] = [];
  loading = true;

  constructor(private productService: ProductService) {}

  ngOnInit(): void {
    this.productService.getCategories().subscribe({
      next: (res) => {
        this.categories = res.items ?? [];
        this.loading = false;
      },
      error: () => (this.loading = false),
    });
  }
}
