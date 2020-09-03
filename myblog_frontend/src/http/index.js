import axios from "axios";
import store from "../store";
const http = axios.create({
                            // baseURL: 'http://192.168.3.2:8989'
})
//请求拦截
http.interceptors.request.use(function (config) {
  if (config.url==="http://chardance-picture.oss-cn-shanghai.aliyuncs.com"){
    return config
  }
  else if(store.state.token!==''){
    config.headers.Authorization = 'Bearer ' + store.state.token
  }
  return config
}, function (error) {
  return Promise.reject(error)
});
//响应拦截
http.interceptors.response.use(function (response) {
  switch (response.data.codeState) {
    case 200:return response;break;
    case 201:store.commit('errorMsg',response.data.msg["loginError"]);store.commit('logout');break;
    case 202:store.commit('errorMsg',response.data.msg["operationError"]);break;
    case 701:store.commit('errorMsg',response.data.msg["tokenError"]);store.commit('logout');break;
    case 999:store.commit('errorMsg',response.data.msg["exception"]);break;
    default:break;
  }
}, function (error) {
  return Promise.reject(error)
});
export default http
