import { Component, EventEmitter, Output } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-product-filters',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './product-filters.html',
  styleUrl: './product-filters.css'
})
export class ProductFiltersComponent {
  @Output() filterChange = new EventEmitter<any>();

  categories = [
    { id: 'silk', name: 'Premium Silk' },
    { id: 'cotton', name: 'Fine Cotton' },
    { id: 'handloom', name: 'Traditional Handloom' }
  ];

  makers = [
    { id: 'maker1', name: 'Artisan Workshop' },
    { id: 'maker2', name: 'Elite Designs' }
  ];

  selectedCategory: string = '';
  selectedMaker: string = '';
  searchQuery: string = '';

  applyFilters() {
    const filters: any = {};
    if (this.selectedCategory) filters.categoryId = this.selectedCategory;
    if (this.selectedMaker) filters.makerId = this.selectedMaker;
    if (this.searchQuery) filters.searchQuery = this.searchQuery;

    this.filterChange.emit(filters);
  }

  resetFilters() {
    this.selectedCategory = '';
    this.selectedMaker = '';
    this.searchQuery = '';
    this.applyFilters();
  }
}
