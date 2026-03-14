export interface GenericResponse<T> {
  success: boolean;
  payload: T;
  errors?: ResponseMessage[];
}

export interface ResponseMessage {
  code: number;
  subErrorCode: number;
  severity: string;
  description: string;
}

export interface SearchRequest {
  queryName: string;
  pageNum: number;
  numRowsInPage: number;
  filters?: {
    [key: string]: any;
    categoryId?: string;
    makerId?: string;
    minPrice?: number;
    maxPrice?: number;
    searchQuery?: string;
  };
  sortColumn?: string;
  sortDirection?: 'ASC' | 'DESC';
}

export interface CatalogItem {
  id: string;
  name: string;
  description: string;
  price: number;
  currency: string;
  imageUrl: string;
  makerName: string;
  makerId: string;
  categoryName: string;
  categoryId: string;
  stockStatus: 'IN_STOCK' | 'LOW_STOCK' | 'OUT_OF_STOCK';
  rating: number;
  reviewCount: number;
  allowedActions?: string[];
}

export interface SearchResponse {
  list: CatalogItem[];
  numRowsReturned: number;
  pageNum: number;
  numRowsInPage: number;
}
