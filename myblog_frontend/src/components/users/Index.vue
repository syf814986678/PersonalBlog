<template>
  <div>
    <div class="no-hd">
      <ul>
                  <li>{{ this.totalBlogNums }}</li>
                  <li>{{ this.todayVisitors }}</li>
                  <li>{{ this.yesterdayVisitors }}</li>
<!--        <li>1234</li>-->
<!--        <li>3456</li>-->
<!--        <li>5678</li>-->
      </ul>
    </div>
    <div class="no-bd">
      <ul>
        <li>总博客数</li>
        <li>今日访问人数</li>
        <li>昨日访问人数</li>
      </ul>
    </div>
    <div class="map">
      <div class="map_bg"></div>
      <div class="map_bg2"></div>
      <div class="map_bg3"></div>
      <div class="chart"></div>-->
    </div>
  </div>
</template>

<script>
    export default {
        name: "Index",
        data(){
          return{
            totalBlogNums:0,
            yesterdayVisitors:0,
            todayVisitors:0,
          }
        },
        methods:{

        },
        created() {
          this.$http.post("/admin/blog/selectTotalBlogsForAdmin").then(response=>{
            if (response!=null){
              this.totalBlogNums=response.data.data;
              this.$notify({
                title: '欢迎',
                message: "欢迎登录本博客",
                type: 'success',
                duration: 2500
              });
            }
          }).catch(error=> {
            console.log(error)
            this.$store.commit('errorMsg',"请求发出错误！请稍后再试")
          })
          // this.$http.post("/blog/getVisitNums/0").then(response=>{
          //   if (response!=null){
          //     this.todayVisitors=response.data.msg["visitNums"];
          //   }
          // }).catch(error=> {
          //   console.log(error)
          //   this.$store.commit('errorMsg',"请求发出错误！请稍后再试")
          // })
          // this.$http.post("/blog/getVisitNums/1").then(response=>{
          //   if (response!=null){
          //     this.yesterdayVisitors=response.data.msg["visitNums"];
          //   }
          // }).catch(error=> {
          //   console.log(error)
          //   this.$store.commit('errorMsg',"请求发出错误！请稍后再试")
          // })
        },
    }
</script>

<style scoped>
.chart {
  /*position: absolute;*/
  /*top: 0;*/
  /*left: 0;*/
  /*width: 100%;*/
  /*height: 30.375rem;*/
}

.map {
  position: relative;
}
.map_bg {
  position: absolute;
  transform: translate(-50%, -50%);
  background: url("https://picture.chardance.cloud/myblog/frontResource/images/map_bg.png");
  background-size: 100% 100%;
  opacity: 0.6;
}
.map_bg2 {
  position: absolute;
  transform: translate(-50%, -50%);
  background: url("https://picture.chardance.cloud/myblog/frontResource/images/map_bg2.png");
  animation: rotate1 15s linear infinite;
  opacity: 1;
  background-size: 100% 100%;
}
.map_bg3{
  position: absolute;
  transform: translate(-50%, -50%);
  background: url("https://picture.chardance.cloud/myblog/frontResource/images/map_bg3.png");
  animation: rotate2 10s linear infinite;
  opacity: 0.8;
  background-size: 100% 100%;
}

@keyframes rotate2 {
  form {
    transform: translate(-50%, -50%) rotate(0deg);
  }
  to {
    transform: translate(-50%, -50%) rotate(-360deg);
  }
}
@keyframes rotate1 {
  from {
    transform: translate(-50%, -50%) rotate(0deg);
  }
  to {
    transform: translate(-50%, -50%) rotate(360deg);
  }
}
@font-face {
  font-family: 'electronicFont';
  src: url("https://picture.chardance.cloud/myblog/frontResource/font/DS-DIGIB-2.ttf");
  font-weight: normal;
  font-style: normal;
}
.no-hd{
  background: rgba(101, 132, 226, 0.2);
  position: relative;
  border: 1px solid rgba(25, 186, 139, 0.17);
}
.no-hd>ul,.no-bd>ul{
  display: flex;
  padding-left: 0 !important;
}
.no-hd:before{
  position: absolute;
  top: 0;
  left: 0;
  content: "";
  width: 30px;
  height: 70px;
  border-top: 2px solid #01e1f8;
  border-left: 2px solid #01e1f8;
  border-bottom: 2px solid #01e1f8;
}
.no-hd:after{
  position: absolute;
  top: 0;
  right: 0;
  content: "";
  width: 30px;
  height: 70px;
  border-top: 2px solid #01e1f8;
  border-right: 2px solid #01e1f8;
  border-bottom: 2px solid #01e1f8;
}
.no-hd>ul>li{
  position: relative;
  flex: 1;
  line-height: 40px;
  color: #ffeb7b;
  text-align: center;
  list-style: none;
  font-family: "electronicFont";
}
.no-hd>ul>li:nth-child(1):after{
  content: "";
  position: absolute;
  right: 0;
  top: -30%;
  height: 170%;
  width: 1px;
  background: rgba(255, 255, 255, 0.2);
}
.no-hd>ul>li:nth-child(2):after{
  content: "";
  position: absolute;
  right: 0;
  top: -30%;
  height: 170%;
  width: 1px;
  background: rgba(255, 255, 255, 0.2);
}
.no-bd>ul>li{
  flex: 1;
  line-height: 10px;
  font-size: 18px;
  text-align: center;
  list-style: none;
  color: #02f5a8;
}

@media only screen and (max-width: 767px) {
  .no-hd>ul>li{
    font-size: 40px;
  }
  .map{
    height:0;
    padding-bottom:100%;
  }
  .map_bg {
    width: 60%;
    height: 60%;
    top: 45%;
    left: 50%;
  }
  .map_bg2 {
    width: 73%;
    height: 73%;
    top: 45%;
    left: 50%;
    opacity: 1;
  }
  .map_bg3{
    width: 65%;
    height: 65%;
    top: 45%;
    left: 50%;
  }
}
@media only screen and (min-width: 768px) {
  .no-hd>ul>li{
    font-size: 75px;
  }
  .map{
    height: 30.375rem;
  }
  .map_bg {
    width: 25.9rem;
    height: 25.9rem;
    top: 55%;
    left: 50%;
  }
  .map_bg2 {
    width: 30rem;
    height: 30rem;
    top: 55%;
    left: 50%;
  }
  .map_bg3{
    width: 28.2rem;
    height: 28.2rem;
    top: 55%;
    left: 50%;
  }
}
</style>
