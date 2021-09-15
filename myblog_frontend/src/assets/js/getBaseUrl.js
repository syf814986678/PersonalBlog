let baseUrl;
if(PRODUCTION){
<<<<<<< Updated upstream
  baseUrl = '/api/';
=======
  // baseUrl = 'http://218.78.69.30:8989/';
  baseUrl = 'https://www.chardance.cloud/api/';
  // baseUrl = 'https://blog-application:8989/';
>>>>>>> Stashed changes
}else if(DEVELEPMENT){
  baseUrl = 'http://localhost:8989';
}
export default {
  baseUrl
}
