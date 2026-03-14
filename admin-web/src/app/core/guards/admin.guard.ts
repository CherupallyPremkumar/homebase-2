import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from '../services/auth.service';

export const adminGuard: CanActivateFn = () => {
  const auth = inject(AuthService);
  const router = inject(Router);
  if (auth.isLoggedIn && auth.hasRole('ADMIN')) return true;
  if (!auth.isLoggedIn) { auth.login(); return false; }
  router.navigate(['/unauthorized']);
  return false;
};
