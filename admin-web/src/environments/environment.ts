export const environment = {
  production: false,
  apiBaseUrl: '',
  keycloak: {
    issuer: 'http://localhost:8180/realms/homebase',
    clientId: 'homebase-admin',
    redirectUri: 'http://localhost:4300',
    scope: 'openid profile email',
  },
};
