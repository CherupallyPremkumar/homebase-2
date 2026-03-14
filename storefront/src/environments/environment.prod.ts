export const environment = {
  production: true,
  apiBaseUrl: '',
  keycloak: {
    issuer: '${KEYCLOAK_ISSUER_URI}',
    clientId: 'homebase-storefront',
    redirectUri: '${APP_URL}',
    scope: 'openid profile email',
  },
};
