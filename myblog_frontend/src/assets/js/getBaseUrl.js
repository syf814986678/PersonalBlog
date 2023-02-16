let baseUrl;
if(PRODUCTION){
  baseUrl = '/api/';
}else if(DEVELEPMENT){
  baseUrl = 'http://10.235.59.204:8989';
}
export default {
  baseUrl
}
