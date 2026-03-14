import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { ProductService } from '../../core/services/product.service';
import { CartService } from '../../core/services/cart.service';
import { ToastService } from '../../shared/components/toast/toast.component';
import { SpinnerComponent } from '../../shared/components/spinner/spinner.component';
import { Product, ProductMedia, CatalogItem } from '../../shared/models/api.models';

@Component({
  selector: 'app-product-detail',
  standalone: true,
  imports: [CommonModule, RouterLink, SpinnerComponent],
  template: `
    @if (loading) {
      <app-spinner />
    } @else if (product) {
      <div class="container">
        <a routerLink="/products" class="back-link">&larr; Back to Products</a>

        <div class="product-detail">
          <!-- Image Gallery -->
          <div class="product-gallery">
            <div class="main-image">
              @if (selectedImage) {
                <img [src]="selectedImage" [alt]="product.name" />
              } @else if (product.imageUrl) {
                <img [src]="product.imageUrl" [alt]="product.name" />
              } @else {
                <div class="no-image">No image available</div>
              }
            </div>
            @if (product.media && product.media.length > 1) {
              <div class="thumbnail-strip">
                @for (media of product.media; track media.id) {
                  <button
                    class="thumbnail"
                    [class.active]="selectedImage === media.url"
                    (click)="selectImage(media.url)"
                  >
                    <img [src]="media.url" [alt]="media.altText || product.name" />
                  </button>
                }
              </div>
            }
          </div>

          <!-- Product Info -->
          <div class="product-info">
            @if (product.brand) {
              <span class="brand">{{ product.brand }}</span>
            }
            <h1>{{ product.name }}</h1>
            <p class="price">{{ product.price | currency:'INR' }}</p>
            <p class="description">{{ product.description }}</p>

            @if (product.stateId) {
              <span class="availability" [class.in-stock]="product.stateId === 'ACTIVE'">
                {{ product.stateId === 'ACTIVE' ? 'In Stock' : product.stateId }}
              </span>
            }

            <div class="actions">
              <div class="quantity-control">
                <button (click)="quantity > 1 && quantity = quantity - 1">-</button>
                <span>{{ quantity }}</span>
                <button (click)="quantity = quantity + 1">+</button>
              </div>
              <button class="btn-add-cart" (click)="addToCart()" [disabled]="adding">
                {{ adding ? 'Adding...' : 'Add to Cart' }}
              </button>
            </div>

            <!-- Attributes -->
            @if (product.sku || product.categoryId) {
              <div class="attributes">
                <h3>Product Details</h3>
                @if (product.sku) {
                  <div class="attr-row">
                    <span class="attr-label">SKU</span>
                    <span class="attr-value">{{ product.sku }}</span>
                  </div>
                }
                @if (product.categoryId) {
                  <div class="attr-row">
                    <span class="attr-label">Category</span>
                    <span class="attr-value">{{ product.categoryId }}</span>
                  </div>
                }
                @if (product.brand) {
                  <div class="attr-row">
                    <span class="attr-label">Brand</span>
                    <span class="attr-value">{{ product.brand }}</span>
                  </div>
                }
              </div>
            }
          </div>
        </div>

        <!-- Related Products -->
        @if (relatedProducts.length > 0) {
          <div class="related-section">
            <h2>You May Also Like</h2>
            <div class="related-grid">
              @for (item of relatedProducts; track item.id) {
                <a [routerLink]="['/products', item.productId]" class="related-card">
                  <div class="related-image-placeholder"></div>
                  <h4>{{ item.name }}</h4>
                  <p class="related-price">{{ item.price | currency:'INR' }}</p>
                </a>
              }
            </div>
          </div>
        }
      </div>
    } @else {
      <div class="container">
        <a routerLink="/products" class="back-link">&larr; Back to Products</a>
        <div class="not-found">
          <h2>Product Not Found</h2>
          <p>The product you are looking for does not exist or has been removed.</p>
          <a routerLink="/products" class="btn-browse">Browse Products</a>
        </div>
      </div>
    }
  `,
  styles: [`
    .container { max-width: 1200px; margin: 2rem auto; padding: 0 1rem; }
    .back-link { color: #6b7280; text-decoration: none; font-size: 0.9rem; display: inline-block; margin-bottom: 1rem; }
    .back-link:hover { color: #1a1a2e; }

    .product-detail { display: grid; grid-template-columns: 1fr 1fr; gap: 3rem; margin-top: 0.5rem; }

    /* Gallery */
    .product-gallery { display: flex; flex-direction: column; gap: 0.75rem; }
    .main-image {
      background: #f3f4f6;
      border-radius: 12px;
      height: 450px;
      display: flex;
      align-items: center;
      justify-content: center;
      overflow: hidden;
    }
    .main-image img { max-height: 100%; max-width: 100%; object-fit: contain; }
    .no-image { color: #9ca3af; font-size: 1rem; }
    .thumbnail-strip {
      display: flex;
      gap: 0.5rem;
      overflow-x: auto;
      padding-bottom: 0.25rem;
    }
    .thumbnail {
      width: 72px;
      height: 72px;
      border: 2px solid #e5e7eb;
      border-radius: 8px;
      overflow: hidden;
      cursor: pointer;
      background: #f9fafb;
      padding: 0;
      flex-shrink: 0;
    }
    .thumbnail.active { border-color: #1a1a2e; }
    .thumbnail img { width: 100%; height: 100%; object-fit: cover; }

    /* Info */
    .brand { color: #6b7280; font-size: 0.85rem; text-transform: uppercase; letter-spacing: 0.05em; }
    h1 { margin: 0.5rem 0; color: #1a1a2e; font-size: 1.75rem; }
    .price { font-size: 1.5rem; font-weight: 700; color: #1a1a2e; margin: 0.5rem 0; }
    .description { color: #4b5563; line-height: 1.7; margin-bottom: 1rem; }
    .availability {
      display: inline-block;
      padding: 0.25rem 0.75rem;
      border-radius: 20px;
      font-size: 0.8rem;
      font-weight: 600;
      background: #fef3c7;
      color: #92400e;
      margin-bottom: 1rem;
    }
    .availability.in-stock { background: #d1fae5; color: #065f46; }

    .actions { display: flex; gap: 1rem; align-items: center; margin-top: 1rem; }
    .quantity-control {
      display: flex;
      align-items: center;
      border: 1px solid #d1d5db;
      border-radius: 8px;
      overflow: hidden;
    }
    .quantity-control button {
      padding: 0.5rem 1rem;
      border: none;
      background: #f3f4f6;
      cursor: pointer;
      font-size: 1rem;
      transition: background 0.15s;
    }
    .quantity-control button:hover { background: #e5e7eb; }
    .quantity-control span { padding: 0.5rem 1rem; min-width: 40px; text-align: center; }
    .btn-add-cart {
      background: #1a1a2e;
      color: #fff;
      border: none;
      padding: 0.75rem 2rem;
      border-radius: 8px;
      font-weight: 600;
      cursor: pointer;
      transition: background 0.2s;
    }
    .btn-add-cart:hover { background: #16213e; }
    .btn-add-cart:disabled { opacity: 0.6; cursor: not-allowed; }

    /* Attributes */
    .attributes { margin-top: 2rem; border-top: 1px solid #e5e7eb; padding-top: 1.5rem; }
    .attributes h3 { margin: 0 0 1rem; font-size: 1rem; color: #374151; }
    .attr-row {
      display: flex;
      justify-content: space-between;
      padding: 0.5rem 0;
      border-bottom: 1px solid #f3f4f6;
      font-size: 0.9rem;
    }
    .attr-label { color: #6b7280; }
    .attr-value { color: #1a1a2e; font-weight: 500; }

    /* Related */
    .related-section { margin-top: 4rem; padding-top: 2rem; border-top: 1px solid #e5e7eb; }
    .related-section h2 { margin: 0 0 1.5rem; color: #1a1a2e; font-size: 1.3rem; }
    .related-grid {
      display: grid;
      grid-template-columns: repeat(auto-fill, minmax(200px, 1fr));
      gap: 1.5rem;
    }
    .related-card {
      text-decoration: none;
      color: inherit;
      border: 1px solid #e5e7eb;
      border-radius: 10px;
      overflow: hidden;
      transition: box-shadow 0.2s;
    }
    .related-card:hover { box-shadow: 0 4px 12px rgba(0,0,0,0.08); }
    .related-image-placeholder { height: 160px; background: #f3f4f6; }
    .related-card h4 { margin: 0.75rem; font-size: 0.95rem; color: #1a1a2e; }
    .related-price { margin: 0 0.75rem 0.75rem; font-weight: 600; color: #374151; font-size: 0.9rem; }

    /* Not found */
    .not-found { text-align: center; padding: 4rem 1rem; }
    .not-found h2 { color: #1a1a2e; margin-bottom: 0.5rem; }
    .not-found p { color: #6b7280; margin-bottom: 1.5rem; }
    .btn-browse {
      background: #1a1a2e;
      color: #fff;
      padding: 0.75rem 2rem;
      border-radius: 8px;
      text-decoration: none;
      font-weight: 600;
    }

    @media (max-width: 768px) {
      .product-detail { grid-template-columns: 1fr; gap: 1.5rem; }
      .main-image { height: 300px; }
      .related-grid { grid-template-columns: repeat(2, 1fr); }
    }
  `],
})
export class ProductDetailComponent implements OnInit {
  product: Product | null = null;
  relatedProducts: CatalogItem[] = [];
  loading = true;
  quantity = 1;
  adding = false;
  selectedImage: string | null = null;

  constructor(
    private route: ActivatedRoute,
    private productService: ProductService,
    private cartService: CartService,
    private toast: ToastService
  ) {}

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id')!;
    this.productService.getProduct(id).subscribe({
      next: (product) => {
        this.product = product;
        this.loading = false;

        // Set initial gallery image
        if (product.media && product.media.length > 0) {
          this.selectedImage = product.media[0].url;
        }

        // Fetch related products by category
        if (product.categoryId) {
          this.productService.getCategoryProducts(product.categoryId).subscribe({
            next: (res) => {
              this.relatedProducts = res.items
                .filter((item) => item.productId !== product.id)
                .slice(0, 4);
            },
            error: () => { /* silently ignore */ },
          });
        }
      },
      error: () => {
        this.loading = false;
        this.toast.error('Failed to load product details.');
      },
    });
  }

  selectImage(url: string): void {
    this.selectedImage = url;
  }

  addToCart(): void {
    if (!this.product) return;
    this.adding = true;
    this.cartService.addItem(this.product.id, this.quantity).subscribe({
      next: () => {
        this.adding = false;
        this.toast.success(`${this.product!.name} added to cart!`);
      },
      error: () => {
        this.adding = false;
        this.toast.error('Failed to add item to cart. Please try again.');
      },
    });
  }
}
