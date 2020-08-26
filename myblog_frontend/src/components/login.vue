<template>
  <div class="login-container" align="center" style="margin-top: 220px">
    <el-form class="login-form">

        <div class="title-container">
          <h3 class="title">Login Form</h3>
        </div>

      <el-form-item>
        <el-row justify="center" type="flex">
          <el-col :xs="24" :xl="6">
            <el-input
              v-model="username"
              placeholder="Username"
              name="username"
              type="text"
            />
          </el-col>
        </el-row>
      </el-form-item>

      <el-form-item>
        <el-row justify="center" type="flex">
          <el-col :xs="24" :xl="6">
            <el-input
              v-model="password"
              placeholder="Password"
              name="password"
              type="password"
            />
          </el-col>
        </el-row>
      </el-form-item>

      <el-row justify="center" type="flex">
        <el-col :xs="24" :xl="6">
          <el-button type="primary" style="width: 100%" @click="login">Login</el-button>
        </el-col>
      </el-row>

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
            this.$http.post("/user/login","username="+this.username+"&password="+this.password).then(response=>{
              if (response!=null){
                this.$store.commit('setmyuser',response.data.msg["myuser"]);
                this.$store.commit('settoken',response.data.msg["token"]);
                this.$router.push("/admin");
                var d = new Date();
                d.setTime(d.getTime()+(60*60*2*1000));
                var expires = "expires="+d.toUTCString();
                document.cookie = "myuserid="+response.data.msg["myuser"].userId+";"+expires;
                document.cookie = "myusername="+response.data.msg["myuser"].userName+";"+expires;
                document.cookie = "token="+response.data.msg["token"]+";"+expires;
              }
            }).catch(error=> {
              console.log(error)
              this.$store.commit('errorMsg',"请求发出错误！请稍后再试")
            })
          },
        },
        created() {
          if (this.$store.state.myuser.userid!='' && this.$store.state.myuser.username!=''&& this.$store.state.token!=''){
            this.$router.push("/admin")
          }
        }
    }
</script>

<style>

</style>
