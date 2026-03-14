// Chenile API response wrapper
export interface ChenileResponse<T> {
  code: number;
  severity: number;
  description?: string;
  data: T;
  errors?: ChenileError[];
}

export interface ChenileError {
  code: number;
  field?: string;
  description: string;
  severity: number;
  subErrorCode?: number;
  params?: string[];
}

// Query API models
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

// Domain models
export interface Money {
  amount: number;
  currency: string;
}

export interface Product {
  id: string;
  name: string;
  description: string;
  brand?: string;
  categoryId?: string;
  sku?: string;
  price?: number;
  imageUrl?: string;
  stateId?: string;
  media?: ProductMedia[];
}

export interface ProductMedia {
  id: string;
  url: string;
  altText?: string;
  mediaType?: string;
  displayOrder?: number;
}

export interface CartItem {
  id: string;
  productId: string;
  productName?: string;
  quantity: number;
  priceAmount: number;
  priceCurrency: string;
  sellerId?: string;
  status: string;
}

export interface Cart {
  id: string;
  userId?: string;
  items: CartItem[];
  totalAmount?: Money;
  shippingAddress?: any;
  billingAddress?: any;
  appliedPromoCode?: string;
  discountAmount?: Money;
  stateId?: string;
}

export interface Order {
  id: string;
  userId: string;
  status: string;
  totalAmount: Money;
  taxAmount?: Money;
  shippingAmount?: Money;
  items: OrderItem[];
  shippingAddress?: any;
  billingAddress?: any;
  appliedPromoCode?: string;
  discountAmount?: number;
  createdAt?: string;
  stateId?: string;
}

export interface OrderItem {
  id: string;
  productId: string;
  productName: string;
  quantity: number;
  unitPrice: Money;
  totalPrice: Money;
  status: string;
}

export interface Category {
  id: string;
  name: string;
  slug: string;
  description?: string;
  parentId?: string;
  imageUrl?: string;
  active: boolean;
  children?: Category[];
}

export interface Collection {
  id: string;
  name: string;
  slug: string;
  description?: string;
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
  tags?: string[];
}

export interface Address {
  id?: string;
  label?: string;
  line1: string;
  line2?: string;
  city: string;
  state?: string;
  postalCode: string;
  country: string;
  phone?: string;
  isDefault?: boolean;
}

export interface UserProfile {
  id: string;
  email: string;
  firstName: string;
  lastName: string;
  phone?: string;
  displayName?: string;
  avatarUrl?: string;
}
