<template>
  <div style="margin-top: 220px">

    <div style="text-align: center">
      <h3>Login</h3>
    </div>

    <div style="width: 50%;text-align: center;margin: 20px auto">
      <el-switch
        @change="switcher"
        style="display: block"
        v-model="switchValue"
        active-color="#13ce66"
        inactive-color="#ff4949"
        active-text="人脸识别"
        inactive-text="普通登录">
      </el-switch>
    </div>

    <el-form id="commonLogin">
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

    <div id="faceLogin" style="text-align: center;margin: 20px auto">
      <video id="video" style="display: none"></video>
      <canvas id="canvas"></canvas>
    </div>

  </div>
</template>

<script>
import {getHeight, getWidth} from "../assets/js/calc";
    export default {
        name: "login",
        data(){
          return{
            username: '',
            password: '',
            switchValue:false,
            cameraWidth:480,
            cameraHeight:320,
            video:null,
            mediaStreamTrack:null,
            intervalId:null,
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

          switcher(){
            if (this.switchValue){
              document.getElementById("commonLogin").style.display='none'
              document.getElementById("faceLogin").style.display=''
              this.openCamera();
            }
            else {
              document.getElementById("commonLogin").style.display=''
              document.getElementById("faceLogin").style.display='none'
              this.mediaStreamTrack.stop();
              clearInterval(this.intervalId);
            }

          },

          //访问摄像头
          openCamera(){
            if (navigator.mediaDevices.getUserMedia || navigator.getUserMedia) {
              //调用用户媒体设备, 访问摄像头
              this.video = document.getElementById('video');
              this.getUserMedia({video: {width: this.cameraWidth, height: this.cameraHeight}}, this.success, this.error);
            } else {
              alert('不支持访问用户媒体');
            }
          },

          //访问用户媒体设备的兼容方法
          getUserMedia(constraints, success, error) {
            if (navigator.mediaDevices.getUserMedia) {
              //最新的标准API
              navigator.mediaDevices.getUserMedia(constraints).then(success).catch(error);
            }
            else if (navigator.getUserMedia) {
              //旧版API
              navigator.getUserMedia(constraints, success, error);
            }
          },

          //成功回调
          success(stream) {
            console.log('成功');
            //兼容webkit核心浏览器
            // const CompatibleURL = window.URL || window.webkitURL;
            //将视频流设置为video元素的源
            // video.src = CompatibleURL.createObjectURL(stream);
            this.video.srcObject = stream;
            this.video.play();
            this.mediaStreamTrack = typeof stream.stop === 'function' ? stream : stream.getTracks()[0];
            this.intervalId = setInterval(this.drawCanvasImage, 100)
          },

          //失败回调
          error(error) {
            console.log('失败');
            alert("访问用户媒体设备失败"+error);
          },

          drawCanvasImage() {
            const canvas = document.getElementById('canvas');
            canvas.width = this.cameraWidth;
            canvas.height = this.cameraHeight;
            const context = canvas.getContext('2d');
            context.drawImage(this.video, 0, 0, this.cameraWidth, this.cameraHeight, 0, 0, this.cameraWidth, this.cameraHeight);
            //获取图片，数据格式为base64
            const imageData = canvas.toDataURL("image/png");
            // console.log(imageData)
          },
        },
        created() {
          if (this.$store.state.user.userId!=='' && this.$store.state.user.userName!==''&& this.$store.state.token!==''){
            this.$router.push("/admin")
          }
          if (getWidth() <= 1000){
            this.cameraWidth = getWidth() - 20;
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
