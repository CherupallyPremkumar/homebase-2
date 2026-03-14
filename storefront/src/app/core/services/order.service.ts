import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { ApiService } from './api.service';
import { QueryService } from './query.service';
import { Order, SearchResponse } from '../../shared/models/api.models';

@Injectable({ providedIn: 'root' })
export class OrderService {
  constructor(
    private api: ApiService,
    private query: QueryService
  ) {}

  getOrder(id: string): Observable<Order> {
    return this.api.get<Order>('order', id);
  }

  getMyOrders(page = 0): Observable<SearchResponse<Order>> {
    return this.query.search<Order>('orders', { pageNum: page });
  }

  cancelOrder(orderId: string, reason: string): Observable<Order> {
    return this.api.triggerEvent<Order>('order', orderId, 'cancelOrder', { reason });
  }

  requestReturn(orderId: string, orderItemId: string, reason: string, quantity: number): Observable<any> {
    return this.api.create('returnrequest', { orderId, orderItemId, reason, quantity });
  }
}
