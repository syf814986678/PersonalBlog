<template>
  <el-container>
    <el-header style="height: 120px">
      <div class="no-hd">
        <ul>
<!--          <li>{{ this.totalBlogNums }}</li>-->
<!--          <li>{{ this.todayVisitors }}</li>-->
<!--          <li>{{ this.yesterdayVisitors }}</li>-->
          <li>1234</li>
          <li>3456</li>
          <li>5678</li>
        </ul>
      </div>
      <div class="no-bd">
        <ul>
          <li>总博客数</li>
          <li>今日访问人数</li>
          <li>昨日访问人数</li>
        </ul>
      </div>
    </el-header>
    <el-container class="map">
        <div class="map_bg"></div>
        <div class="map_bg2"></div>
        <div class="map_bg3"></div>
        <div class="chart"></div>
    </el-container>
    <el-footer>Fotter</el-footer>
  </el-container>
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
        // created() {
        //   this.$http.post("/blog/selectTotalBlogNums").then(response=>{
        //     if (response!=null){
        //       this.totalBlogNums=response.data.msg["totalBlogNums"];
        //     }
        //   }).catch(error=> {
        //     console.log(error)
        //     this.$store.commit('errorMsg',"请求发出错误！请稍后再试")
        //   })
        //   this.$http.post("/blog/getVisitNums","dayNum=0").then(response=>{
        //     if (response!=null){
        //       this.todayVisitors=response.data.msg["visitNums"];
        //     }
        //   }).catch(error=> {
        //     console.log(error)
        //     this.$store.commit('errorMsg',"请求发出错误！请稍后再试")
        //   })
        //   this.$http.post("/blog/getVisitNums","dayNum=1").then(response=>{
        //     if (response!=null){
        //       this.yesterdayVisitors=response.data.msg["visitNums"];
        //     }
        //   }).catch(error=> {
        //     console.log(error)
        //     this.$store.commit('errorMsg',"请求发出错误！请稍后再试")
        //   })
        // }
    }
</script>

<style scoped>
.chart {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 30.375rem;
}

.map{
  position: relative;
  height: 30.375rem;
  margin-top: 60px;
}
.map_bg {
  width: 25.9rem;
  height: 25.9rem;
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  background: url("../../assets/images/map_bg.png");
  background-size: 100% 100%;
  opacity: 0.7;
}
.map_bg2 {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  width: 32.13125rem;
  height: 32.13125rem;
  background: url("../../assets/images/map_bg2.png");
  animation: rotate1 15s linear infinite;
  opacity: 0.8;
  background-size: 100% 100%;
}
.map_bg3{
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  width: 28.2rem;
  height: 28.2rem;
  background: url("../../assets/images/map_bg3.png");
  animation: rotate2 10s linear infinite;
  opacity: 0.6;
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
  src: url("../../assets/font/DS-DIGIB-2.ttf");
  font-weight: normal;
  font-style: normal;
}

.el-header{
  background-color: #1e2064;
  color: #333;
  text-align: center;
}

.no-hd{
  background: rgba(101, 132, 226, 0.2);
  position: relative;
  border: 1px solid rgba(25, 186, 139, 0.17);
  margin: 0 -20px;
}
.no-hd>ul,.no-bd>ul{
  display: flex;
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
  font-size: 75px;
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

</style>
