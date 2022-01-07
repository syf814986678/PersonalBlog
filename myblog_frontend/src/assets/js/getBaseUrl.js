let baseUrl;
if(PRODUCTION){
  baseUrl = '/api/';
}else if(DEVELEPMENT){
  baseUrl = 'http://10.89.55.172:8989';
}
export default {
  baseUrl
}
