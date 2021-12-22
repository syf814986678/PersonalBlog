let baseUrl;
if(PRODUCTION){
  baseUrl = '/api/';
}else if(DEVELEPMENT){
  baseUrl = 'http://192.168.43.157:8989';
}
export default {
  baseUrl
}
