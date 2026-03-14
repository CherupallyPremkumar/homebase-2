import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { QueryService } from '../../services/query.service';
import { CatalogItem } from '../../models/query.model';
import { ProductFiltersComponent } from '../product-filters/product-filters';

@Component({
  selector: 'app-product-gallery',
  standalone: true,
  imports: [CommonModule, RouterModule, ProductFiltersComponent],
  templateUrl: './product-gallery.html',
  styleUrl: './product-gallery.css'
})
export class ProductGalleryComponent implements OnInit {
  products: CatalogItem[] = [];
  loading = false;
  error: string | null = null;
  
  currentPage = 0;
  pageSize = 12;
  totalElements = 0;
  filters: any = {};

  constructor(private queryService: QueryService) {}

  ngOnInit() {
    this.loadProducts();
  }

  loadProducts() {
    this.loading = true;
    this.error = null;

    this.queryService.browseProducts(this.currentPage, this.pageSize, this.filters).subscribe({
      next: (response) => {
        if (response.success && response.payload) {
          this.products = response.payload.list;
          this.totalElements = response.payload.numRowsReturned;
        }
        this.loading = false;
      },
      error: (err: any) => {
        this.error = 'Failed to load products. Please try again later.';
        this.loading = false;
        console.error(err);
      }
    });
  }

  onFilterChange(newFilters: any) {
    this.filters = newFilters;
    this.currentPage = 0;
    this.loadProducts();
  }

  nextPage() {
    if ((this.currentPage + 1) * this.pageSize < this.totalElements) {
      this.currentPage++;
      this.loadProducts();
    }
  }

  prevPage() {
    if (this.currentPage > 0) {
      this.currentPage--;
      this.loadProducts();
    }
  }
}
