import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, RouterModule } from '@angular/router';
import { QueryService } from '../../services/query.service';
import { CatalogItem } from '../../models/query.model';

@Component({
  selector: 'app-product-detail',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './product-detail.html',
  styleUrl: './product-detail.css'
})
export class ProductDetailComponent implements OnInit {
  product: CatalogItem | null = null;
  loading = true;
  error: string | null = null;

  constructor(
    private route: ActivatedRoute,
    private queryService: QueryService
  ) {}

  ngOnInit() {
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.loadProduct(id);
    } else {
      this.error = 'Product ID not found.';
      this.loading = false;
    }
  }

  loadProduct(id: string) {
    this.loading = true;
    // For now, we use browseProducts with a filter by ID since we don't have a direct getById query yet
    this.queryService.browseProducts(0, 1, { id }).subscribe({
      next: (response) => {
        if (response.success && response.payload && response.payload.list.length > 0) {
          this.product = response.payload.list[0];
        } else {
          this.error = 'Product not found.';
        }
        this.loading = false;
      },
      error: (err) => {
        this.error = 'Failed to load product details.';
        this.loading = false;
      }
    });
  }
}
