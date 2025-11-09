export const environment = {
  production: true,
  keycloakUrl: 'http://keycloak:8080',  // Docker name du conteneur
  realm: 'Charity',
  clientId: 'charity-client',
  apiUrl: '/api'  // Relative URL for Nginx proxy
};
