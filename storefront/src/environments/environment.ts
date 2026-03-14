export const environment = {
  production: false,
  apiBaseUrl: '',
  keycloak: {
    issuer: 'http://localhost:8180/realms/homebase',
    clientId: 'homebase-storefront',
    redirectUri: 'http://localhost:4200',
    scope: 'openid profile email',
  },
};
