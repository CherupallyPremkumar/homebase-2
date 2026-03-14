export interface ChenileResponse<T> {
  code: number;
  severity: number;
  description?: string;
  data: T;
}

export interface SearchRequest {
  filters?: Record<string, any>;
  pageNum?: number;
  numRowsInPage?: number;
  sortBy?: string;
  sortOrder?: 'asc' | 'desc';
}

export interface SearchResponse<T> {
  items: T[];
  totalCount: number;
  pageNum: number;
  numRowsInPage: number;
}

export interface Money {
  amount: number;
  currency: string;
}

// Admin domain models
export interface Supplier {
  id: string;
  name: string;
  email: string;
  userId?: string;
  phone?: string;
  upiId?: string;
  address?: string;
  commissionPercentage?: number;
  stateId?: string;
  description?: string;
}

export interface Shipment {
  id: string;
  orderId: string;
  carrier: string;
  trackingNumber: string;
  trackingUrl?: string;
  shippedAt?: string;
  estimatedDelivery?: string;
  deliveredAt?: string;
  stateId?: string;
}

export interface Settlement {
  id: string;
  supplierId: string;
  periodMonth: number;
  periodYear: number;
  totalSalesAmount: number;
  commissionAmount: number;
  netPayoutAmount: number;
  currency: string;
  stateId?: string;
}

export interface ReturnRequest {
  id: string;
  orderId: string;
  orderItemId: string;
  reason: string;
  quantity: number;
  refundAmount?: number;
  returnType?: string;
  stateId?: string;
}

export interface PromoCode {
  id: string;
  code: string;
  description?: string;
  discountType: string;
  discountValue: number;
  minOrderAmount?: number;
  maxDiscountAmount?: number;
  expiryDate?: string;
  usageCount: number;
  maxUsage?: number;
  stateId?: string;
}

export interface Category {
  id: string;
  name: string;
  slug: string;
  description?: string;
  parentId?: string;
  imageUrl?: string;
  active: boolean;
}

export interface CatalogItem {
  id: string;
  productId: string;
  name: string;
  price: number;
  featured: boolean;
  active: boolean;
}

export interface Cart {
  id: string;
  userId?: string;
  items: any[];
  totalAmount?: Money;
  stateId?: string;
  lastModifiedTime?: string;
}

export interface Order {
  id: string;
  userId: string;
  status: string;
  totalAmount: Money;
  items: any[];
  createdAt?: string;
  stateId?: string;
}

export interface Product {
  id: string;
  name: string;
  description?: string;
  brand?: string;
  sku?: string;
  stateId?: string;
}

export interface Inventory {
  id: string;
  productId: string;
  quantity: number;
  availableQuantity: number;
  reservedQuantity: number;
  status: string;
}

export interface DashboardStats {
  totalOrders: number;
  totalRevenue: number;
  activeSuppliers: number;
  pendingReturns: number;
  activeCarts: number;
  lowStockItems: number;
}
