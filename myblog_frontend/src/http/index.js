import axios from "axios";
import store from "../store";
const http = axios.create({
                                                    baseURL: 'http://localhost:8989'
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
    case 200:return response;
    case 201:store.commit('errorMsg',response.data.msg);break;
    case 999:store.commit('errorMsg',response.data.msg);break;
    case 301:store.commit('errorMsg',response.data.msg);store.commit('logout');break;
    case 701:store.commit('errorMsg',response.data.msg);store.commit('logout');break;
    case 702:store.commit('errorMsg',response.data.msg);store.commit('logout');break;
    // case 998:store.commit('errorMsg',response.data.OSS_EXCEPTION);break;
    default:break;
  }
}, function (error) {
  return Promise.reject(error)
});
export default http
