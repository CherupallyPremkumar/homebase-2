export const environment = {
  production: true,
  apiBaseUrl: '',
  keycloak: {
    issuer: '${KEYCLOAK_ISSUER_URI}',
    clientId: 'homebase-admin',
    redirectUri: '${ADMIN_URL}',
    scope: 'openid profile email',
  },
};
