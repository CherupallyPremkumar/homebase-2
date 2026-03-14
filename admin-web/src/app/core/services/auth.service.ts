import { Injectable } from '@angular/core';
import { OAuthService, AuthConfig } from 'angular-oauth2-oidc';
import { Router } from '@angular/router';
import { environment } from '../../../environments/environment';
import { BehaviorSubject } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class AuthService {
  private isAuthenticatedSubject = new BehaviorSubject<boolean>(false);
  isAuthenticated$ = this.isAuthenticatedSubject.asObservable();
  private userProfileSubject = new BehaviorSubject<any>(null);
  userProfile$ = this.userProfileSubject.asObservable();

  constructor(private oauthService: OAuthService, private router: Router) {}

  async configure(): Promise<void> {
    const authConfig: AuthConfig = {
      issuer: environment.keycloak.issuer,
      clientId: environment.keycloak.clientId,
      redirectUri: environment.keycloak.redirectUri,
      scope: environment.keycloak.scope,
      responseType: 'code',
      requireHttps: false,
      showDebugInformation: !environment.production,
    };
    this.oauthService.configure(authConfig);
    this.oauthService.events.subscribe((event) => {
      if (event.type === 'token_received' || event.type === 'token_refreshed') {
        this.isAuthenticatedSubject.next(true);
        this.loadUserProfile();
      }
    });
    await this.oauthService.loadDiscoveryDocumentAndTryLogin();
    if (this.oauthService.hasValidAccessToken()) {
      this.isAuthenticatedSubject.next(true);
      this.loadUserProfile();
      if (!this.hasRole('ADMIN')) {
        this.router.navigate(['/unauthorized']);
      }
    } else {
      this.login();
    }
  }

  login(): void { this.oauthService.initCodeFlow(); }
  logout(): void { this.oauthService.logOut(); }
  get accessToken(): string { return this.oauthService.getAccessToken(); }
  get isLoggedIn(): boolean { return this.oauthService.hasValidAccessToken(); }

  get userRoles(): string[] {
    const claims = this.oauthService.getIdentityClaims() as any;
    return claims?.realm_access?.roles ?? [];
  }

  hasRole(role: string): boolean { return this.userRoles.includes(role); }

  get userName(): string {
    const claims = this.oauthService.getIdentityClaims() as any;
    return claims?.preferred_username ?? claims?.email ?? 'Admin';
  }

  private loadUserProfile(): void {
    this.oauthService.loadUserProfile().then((p) => this.userProfileSubject.next(p));
  }
}
