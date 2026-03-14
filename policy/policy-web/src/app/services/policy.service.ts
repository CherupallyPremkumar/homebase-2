import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, of } from 'rxjs';
import { map } from 'rxjs/operators';
import { Policy, Decision, FactDefinition } from '../models/policy.model';

@Injectable({
    providedIn: 'root'
})
export class PolicyService {
    private apiUrl = '/api/policies'; // For GET collections and lookup
    private stmUrl = '/policy';       // For STM operations (POST/PATCH)

    constructor(private http: HttpClient) { }

    private mapResponse(response: any): any {
        const data = response.payload || response.data || response;
        if (data && data.mutatedEntity) {
            return data.mutatedEntity;
        }
        return data;
    }

    getPolicies(): Observable<Policy[]> {
        return this.http.get<Policy[]>(this.apiUrl).pipe(
            map(res => this.mapResponse(res))
        );
    }

    getDecisions(): Observable<Decision[]> {
        return this.http.get<Decision[]>('/api/decisions').pipe(
            map(res => this.mapResponse(res))
        );
    }

    evaluatePolicy(id: string, context: any): Observable<Decision> {
        return this.http.post<Decision>(`${this.apiUrl}/${id}/evaluate`, context).pipe(
            map(res => this.mapResponse(res))
        );
    }

    getPolicy(id: string): Observable<Policy> {
        return this.http.get<Policy>(`${this.apiUrl}/${id}`).pipe(
            map(res => this.mapResponse(res))
        );
    }

    createPolicy(policy: Policy): Observable<Policy> {
        return this.http.post<Policy>(this.stmUrl, policy).pipe(
            map(res => this.mapResponse(res))
        );
    }

    updatePolicy(id: string, policy: Policy): Observable<Policy> {
        return this.http.put<Policy>(`${this.apiUrl}/${id}`, policy).pipe(
            map(res => this.mapResponse(res))
        );
    }

    deletePolicy(id: string): Observable<void> {
        return this.http.delete<void>(`${this.apiUrl}/${id}`);
    }

    evaluate(policyId: string | null, context: any, targetModule?: string): Observable<Decision> {
        return this.http.post<Decision>(`${this.stmUrl}/evaluate`, { policyId, facts: context, targetModule }).pipe(
            map(res => this.mapResponse(res))
        );
    }

    getFacts(): Observable<FactDefinition[]> {
        return this.http.get<FactDefinition[]>('/api/policy/metadata/facts').pipe(
            map(res => this.mapResponse(res))
        );
    }
}
