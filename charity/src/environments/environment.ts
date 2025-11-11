export const environment = {
  production: false,
  keycloakUrl: 'http://localhost:8080',
  realm: 'Charity',
  clientId: 'charity-client',

  // Utilisez maintenant l'API Gateway comme point d'entrée unique
  //apiUrl: 'http://localhost:8980', // Port de votre API Gateway

  // Vous pouvez garder les URLs individuelles pour le développement
  notificationServiceUrl: 'http://localhost:8985',
  reportServiceUrl: 'http://localhost:8086',
  donationServiceUrl: 'http://localhost:8282',
  eventServiceUrl: 'http://localhost:9091',
  formationUrl: 'http://localhost:1920',
  blogServiceUrl: 'http://localhost:8989',
  userServiceUrl: 'http://localhost:8081'
};
