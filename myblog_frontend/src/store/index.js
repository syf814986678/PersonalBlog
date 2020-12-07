import Vue from 'vue'
import Vuex from 'vuex'
import createPersistedState from "vuex-persistedstate"
import router from "../router";
Vue.use(Vuex)

export default new Vuex.Store({
  state:{
    collapseBtnClick: false,
    isCollapse: true,
    user:{
      userId: '',
      userName: '',
    },
    token: '',
    OSS: {
      accessId: '',
      host: '',
      policy: '',
      signature: '',
      callback: '',
      dir: '',
      expire: 0,
    },
    commonCurrentPage: 1,
    currentPage: 1,
    height:0,
    mainLoading:true,
    category: {},
    whatSearch:'',
  },
  mutations:{
    setSearch(state,search){
      state.whatSearch=search
    },
    setCategory(state,category){
      state.category[category[0]] = category[1]
    },
    setMainLoading(state,status){
      state.mainLoading=status
    },
    setHeight(state,height){
      state.height=height
    },
    setCommonCurrentPage(state,page){
      state.commonCurrentPage=page
    },
    setCurrentPage(state,page){
      state.commonCurrentPage=page
    },
    setOSS(state,data){
      state.OSS.accessId=data.msg["accessId"]
      state.OSS.host=data.msg["host"]
      state.OSS.policy=data.msg["policy"]
      state.OSS.signature=data.msg["signature"]
      state.OSS.callback=data.msg["callback"]
      state.OSS.expire=data.msg["expire"]
      state.OSS.dir=data.msg["dir"]
    },
    setUser(state,user){
      state.user.userId=user.userId
      state.user.userName=user.userName
    },
    // setmyuserid(state,myuserid){
    //   state.myuser.userid=myuserid
    // },
    // setmyusername(state,myusername){
    //   state.myuser.username=myusername
    // },
    setToken(state,token){
      state.token=token
    },
    logout(state){
      state.user.userName=''
      state.user.userId=''
      state.token=''
      sessionStorage.removeItem('vuex')
      router.push("/login")
      var d = new Date();
      d.setTime(d.getTime()+(-1*1000));
      var expires = "expires="+d.toUTCString();
      document.cookie = "userId=;"+expires;
      document.cookie = "userName=;"+expires;
      document.cookie = "token=;"+expires;
    },
    collapseOpen(state) {
      if (state.collapseBtnClick) return;
      state.isCollapse = false
    },
    collapseClose(state) {
      if (state.collapseBtnClick) return;
      state.isCollapse = true;
    },
    collapseReverse(state) {
      state.collapseBtnClick = !state.collapseBtnClick
    },
    errorMsg(state,string){
      Vue.prototype.$notify({
        title: '失败',
        message: string,
        type: 'error',
        duration: 2500
      });
    },
  },
  plugins: [createPersistedState({
    storage: window.sessionStorage,
    reducer(val) {
      return {
        // 只储存state中的user
        user: {
          userId: val.user.userId,
          userName: val.user.userName,
        },
        token: val.token,
      }
    }
  })]
})
