import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, map } from 'rxjs';
import { environment } from '../../../environments/environment';
import { ChenileResponse } from '../../shared/models/api.models';

@Injectable({ providedIn: 'root' })
export class ApiService {
  private baseUrl = environment.apiBaseUrl;
  constructor(private http: HttpClient) {}

  get<T>(entity: string, id: string): Observable<T> {
    return this.http.get<ChenileResponse<T>>(`${this.baseUrl}/${entity}/${id}`).pipe(map(r => r.data));
  }

  create<T>(entity: string, body: any): Observable<T> {
    return this.http.post<ChenileResponse<T>>(`${this.baseUrl}/${entity}`, body).pipe(map(r => r.data));
  }

  update<T>(entity: string, id: string, body: any): Observable<T> {
    return this.http.put<ChenileResponse<T>>(`${this.baseUrl}/${entity}/${id}`, body).pipe(map(r => r.data));
  }

  triggerEvent<T>(entity: string, id: string, event: string, payload?: any): Observable<T> {
    return this.http.patch<ChenileResponse<T>>(`${this.baseUrl}/${entity}/${id}/${event}`, payload ?? {}).pipe(map(r => r.data));
  }
}
