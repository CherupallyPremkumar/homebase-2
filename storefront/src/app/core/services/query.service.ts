import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { SearchRequest, SearchResponse } from '../../shared/models/api.models';

/**
 * Wraps Chenile Query API: POST /q/{queryName}
 *
 * The query module uses MyBatis mapper XML/JSON definitions
 * to execute parameterized SQL queries with pagination.
 */
@Injectable({ providedIn: 'root' })
export class QueryService {
  private baseUrl = environment.apiBaseUrl;

  constructor(private http: HttpClient) {}

  search<T>(queryName: string, request: SearchRequest = {}): Observable<SearchResponse<T>> {
    const body = {
      ...request,
      pageNum: request.pageNum ?? 0,
      numRowsInPage: request.numRowsInPage ?? 20,
    };
    return this.http.post<SearchResponse<T>>(`${this.baseUrl}/q/${queryName}`, body);
  }

  getById<T>(queryName: string, id: string): Observable<T> {
    return this.http.post<T>(`${this.baseUrl}/q/${queryName}/${id}`, {});
  }
}
