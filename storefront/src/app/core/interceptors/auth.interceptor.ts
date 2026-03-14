import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { AuthService } from '../services/auth.service';

/**
 * Attaches Keycloak JWT token and Chenile tenant header to outgoing API requests.
 */
export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const authService = inject(AuthService);

  if (req.url.startsWith('/q/') || req.url.startsWith('/') && !req.url.startsWith('//')) {
    const headers: Record<string, string> = {
      'x-chenile-tenant-id': 'homebase',
    };

    if (authService.isLoggedIn) {
      headers['Authorization'] = `Bearer ${authService.accessToken}`;
    }

    const cloned = req.clone({ setHeaders: headers });
    return next(cloned);
  }

  return next(req);
};
