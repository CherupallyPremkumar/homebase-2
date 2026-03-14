import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { SearchRequest, SearchResponse } from '../../shared/models/api.models';

@Injectable({ providedIn: 'root' })
export class QueryService {
  private baseUrl = environment.apiBaseUrl;
  constructor(private http: HttpClient) {}

  search<T>(queryName: string, request: SearchRequest = {}): Observable<SearchResponse<T>> {
    return this.http.post<SearchResponse<T>>(`${this.baseUrl}/q/${queryName}`, {
      ...request, pageNum: request.pageNum ?? 0, numRowsInPage: request.numRowsInPage ?? 20,
    });
  }
}
