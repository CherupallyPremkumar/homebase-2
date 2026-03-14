import { ApplicationConfig, APP_INITIALIZER } from '@angular/core';
import { provideRouter } from '@angular/router';
import { provideHttpClient, withInterceptors } from '@angular/common/http';
import { provideOAuthClient } from 'angular-oauth2-oidc';

import { routes } from './app.routes';
import { authInterceptor } from './core/interceptors/auth.interceptor';
import { AuthService } from './core/services/auth.service';

function initAuth(authService: AuthService) {
  return () => authService.configure();
}

export const appConfig: ApplicationConfig = {
  providers: [
    provideRouter(routes),
    provideHttpClient(withInterceptors([authInterceptor])),
    provideOAuthClient(),
    { provide: APP_INITIALIZER, useFactory: initAuth, deps: [AuthService], multi: true },
  ],
};
