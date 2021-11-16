let baseUrl;
if(PRODUCTION){
  baseUrl = '/api/';
}else if(DEVELEPMENT){
  baseUrl = 'http://localhost:8989';
}
export default {
  baseUrl
}
