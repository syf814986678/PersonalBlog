<template>
  <div style="margin-top: 220px">
    <el-form class="myform">
        <div style="text-align: center">
          <h3>Login</h3>
        </div>
      <el-form-item>
        <div class="myloginrow">
            <el-input
              v-model="username"
              placeholder="Username"
              name="username"
              type="text"
            />
        </div>
      </el-form-item>

      <el-form-item>
        <div class="myloginrow">
            <el-input
              v-model="password"
              placeholder="Password"
              name="password"
              type="password"
            />
        </div>
      </el-form-item>

      <div class="myloginrow">
          <el-button type="primary" style="width: 100%" @click="login">Login</el-button>
      </div>

    </el-form>
  </div>
</template>

<script>
    export default {
        name: "login",
        data(){
          return{
            username: '',
            password: '',
          }
        },
        methods:{
          login(){
            this.$http.post("/admin/login","username="+this.username+"&password="+this.password).then(response=>{
              if (response!=null){
                const user = {
                  userId:response.data.data[0],
                  userName:this.username,
                };
                this.$store.commit('setUser',user);
                this.$store.commit('setToken',response.data.data[1]);
                this.$router.push("/admin");
                var d = new Date();
                d.setTime(d.getTime()+(60*60*2*1000));
                var expires = "expires="+d.toUTCString();
                document.cookie = "userId="+user.userId+";"+expires;
                document.cookie = "userName="+user.userName+";"+expires;
                document.cookie = "token="+response.data.data[1]+";"+expires;
              }
            }).catch(error=> {
              console.log(error)
              this.$store.commit('errorMsg',"请求发出错误！请稍后再试")
            })
          },
        },
        created() {
          if (this.$store.state.user.userId!=='' && this.$store.state.user.userName!==''&& this.$store.state.token!==''){
            this.$router.push("/admin")
          }
        }
    }
</script>

<style>

@media only screen and (max-width: 767px){

}
@media only screen and (min-width: 768px) {
  .myloginrow{
    text-align: center;
    width: 20%;
    margin: 0 auto;
  }
}
</style>
