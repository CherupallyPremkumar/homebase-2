import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { ApiService } from './api.service';
import { QueryService } from './query.service';
import { Product, SearchResponse, CatalogItem, Category, Collection } from '../../shared/models/api.models';

@Injectable({ providedIn: 'root' })
export class ProductService {
  constructor(
    private api: ApiService,
    private query: QueryService
  ) {}

  getProduct(id: string): Observable<Product> {
    return this.api.get<Product>('product', id);
  }

  searchProducts(filters?: Record<string, any>, page = 0): Observable<SearchResponse<Product>> {
    return this.query.search<Product>('products', { filters, pageNum: page });
  }

  getFeaturedItems(page = 0): Observable<SearchResponse<CatalogItem>> {
    return this.query.search<CatalogItem>('catalogs', {
      filters: { featured: true, active: true },
      pageNum: page,
    });
  }

  getCategories(): Observable<SearchResponse<Category>> {
    return this.query.search<Category>('categoryMenu', {
      filters: { active: true },
      numRowsInPage: 100,
    });
  }

  getCategoryProducts(categoryId: string, page = 0): Observable<SearchResponse<CatalogItem>> {
    return this.query.search<CatalogItem>('catalogByCategory', {
      filters: { categoryId },
      pageNum: page,
    });
  }

  getCollections(): Observable<SearchResponse<Collection>> {
    return this.query.search<Collection>('userCollections', {
      filters: { active: true },
    });
  }

  getCollectionProducts(collectionId: string, page = 0): Observable<SearchResponse<CatalogItem>> {
    return this.query.search<CatalogItem>('browseCatalog', {
      filters: { collectionId },
      pageNum: page,
    });
  }
}
