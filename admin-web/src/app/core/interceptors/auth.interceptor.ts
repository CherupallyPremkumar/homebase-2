import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { AuthService } from '../services/auth.service';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const authService = inject(AuthService);
  const headers: Record<string, string> = { 'x-chenile-tenant-id': 'homebase' };
  if (authService.isLoggedIn) {
    headers['Authorization'] = `Bearer ${authService.accessToken}`;
  }
  return next(req.clone({ setHeaders: headers }));
};
