import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable, map } from 'rxjs';
import { environment } from '../../../environments/environment';
import { ChenileResponse } from '../../shared/models/api.models';

/**
 * Base HTTP client for Chenile REST APIs.
 *
 * Chenile endpoints follow these patterns:
 * - GET    /{entity}/{id}          → fetch entity
 * - POST   /{entity}               → create entity
 * - PUT    /{entity}/{id}          → update entity
 * - PATCH  /{entity}/{id}/{event}  → trigger STM event
 * - POST   /q/{queryName}          → query (search/list)
 */
@Injectable({ providedIn: 'root' })
export class ApiService {
  private baseUrl = environment.apiBaseUrl;

  constructor(private http: HttpClient) {}

  // GET /{entity}/{id}
  get<T>(entity: string, id: string): Observable<T> {
    return this.http
      .get<ChenileResponse<T>>(`${this.baseUrl}/${entity}/${id}`)
      .pipe(map((res) => res.data));
  }

  // POST /{entity}
  create<T>(entity: string, body: any): Observable<T> {
    return this.http
      .post<ChenileResponse<T>>(`${this.baseUrl}/${entity}`, body)
      .pipe(map((res) => res.data));
  }

  // PUT /{entity}/{id}
  update<T>(entity: string, id: string, body: any): Observable<T> {
    return this.http
      .put<ChenileResponse<T>>(`${this.baseUrl}/${entity}/${id}`, body)
      .pipe(map((res) => res.data));
  }

  // PATCH /{entity}/{id}/{eventId} — Chenile STM transition
  triggerEvent<T>(entity: string, id: string, event: string, payload?: any): Observable<T> {
    return this.http
      .patch<ChenileResponse<T>>(`${this.baseUrl}/${entity}/${id}/${event}`, payload ?? {})
      .pipe(map((res) => res.data));
  }

  // Raw HTTP for custom endpoints
  post<T>(url: string, body: any): Observable<T> {
    return this.http.post<T>(`${this.baseUrl}${url}`, body);
  }

  getRaw<T>(url: string): Observable<T> {
    return this.http.get<T>(`${this.baseUrl}${url}`);
  }
}
