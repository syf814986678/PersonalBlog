import Vue from 'vue'
import Vuex from 'vuex'
import createPersistedState from "vuex-persistedstate"
import router from "../router";
import ca from "element-ui/src/locale/lang/ca";
Vue.use(Vuex)

export default new Vuex.Store({
  state:{
    collapseBtnClick: false,
    isCollapse: true,
    myuser:{
      userid: '',
      username: '',
    },
    token: '',
    OSS: {
      accessid: '',
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
    mainloading:true,
    category: {},
    whatsearch:'',
  },
  mutations:{
    setsearch(state,search){
      state.whatsearch=search
    },
    setCategory(state,category){
      state.category[category[0]] = category[1]
    },
    setmainloading(state,status){
      state.mainloading=status
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
      state.OSS.accessid=data.msg["accessid"]
      state.OSS.host=data.msg["host"]
      state.OSS.policy=data.msg["policy"]
      state.OSS.signature=data.msg["signature"]
      state.OSS.callback=data.msg["callback"]
      state.OSS.expire=data.msg["expire"]
      state.OSS.dir=data.msg["dir"]
    },
    setmyuser(state,myuser){
      state.myuser.userid=myuser.userId
      state.myuser.username=myuser.userName
    },
    setmyuserid(state,myuserid){
      state.myuser.userid=myuserid
    },
    setmyusername(state,myusername){
      state.myuser.username=myusername
    },
    settoken(state,token){
      state.token=token
    },
    logout(state){
      state.myuser.username=''
      state.myuser.userid=''
      state.token=''
      sessionStorage.removeItem('vuex')
      router.push("/login")
      var d = new Date();
      d.setTime(d.getTime()+(-1*1000));
      var expires = "expires="+d.toUTCString();
      document.cookie = "myuserid=;"+expires;
      document.cookie = "myusername=;"+expires;
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
        myuser: {
          userid: val.myuser.userid,
          username: val.myuser.username,
        },
        token: val.token,
      }
    }
  })]
})
