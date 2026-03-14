import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { SearchRequest, SearchResponse, CatalogItem, GenericResponse } from '../models/query.model';

@Injectable({
  providedIn: 'root'
})
export class QueryService {
  private baseUrl = '/api/catalog';

  constructor(private http: HttpClient) {}

  browseProducts(pageNum: number, numRowsInPage: number, filters: any = {}): Observable<GenericResponse<SearchResponse>> {
    const request: SearchRequest = {
      queryName: 'CatalogItem.browse',
      pageNum: pageNum + 1, // Chenile is 1-indexed for pages
      numRowsInPage,
      filters: { ...filters, active: true }
    };
    return this.http.post<GenericResponse<SearchResponse>>(this.baseUrl, request);
  }
}
