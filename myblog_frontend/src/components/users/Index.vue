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
    <el-container>
      <el-aside width="200px">Aside1</el-aside>
      <el-main>
        <el-container>
          <el-header>header1</el-header>
          <el-container>
            <el-aside width="200px">aside1</el-aside>
            <el-main>main1</el-main>
            <el-aside width="200px">aside2</el-aside>
          </el-container>
          <el-footer>footer1</el-footer>
        </el-container>
      </el-main>
      <el-aside width="200px">Aside2</el-aside>
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
        created() {
          // this.$http.post("/blog/selectTotalBlogNums").then(response=>{
          //   if (response!=null){
          //     this.totalBlogNums=response.data.msg["totalBlogNums"];
          //   }
          // }).catch(error=> {
          //   console.log(error)
          //   this.$store.commit('errorMsg',"请求发出错误！请稍后再试")
          // })
          // this.$http.post("/blog/getVisitNums","dayNum=0").then(response=>{
          //   if (response!=null){
          //     this.todayVisitors=response.data.msg["visitNums"];
          //   }
          // }).catch(error=> {
          //   console.log(error)
          //   this.$store.commit('errorMsg',"请求发出错误！请稍后再试")
          // })
          // this.$http.post("/blog/getVisitNums","dayNum=1").then(response=>{
          //   if (response!=null){
          //     this.yesterdayVisitors=response.data.msg["visitNums"];
          //   }
          // }).catch(error=> {
          //   console.log(error)
          //   this.$store.commit('errorMsg',"请求发出错误！请稍后再试")
          // })
        }
    }
</script>

<style scoped>

@font-face {
  font-family: 'electronicFont';
  src: url("../../assets/font/DS-DIGIB-2.ttf");
  font-weight: normal;
  font-style: normal;
}

.el-header, .el-footer {
  background-color: #10125b;
  color: #333;
  text-align: center;
}

.el-aside {
  background-color: #D3DCE6;
  color: #333;
  text-align: center;
  line-height: 200px;
}

.el-main {
  background-color: #E9EEF3;
  color: #333;
  text-align: center;
  line-height: 160px;
}

body > .el-container {
  margin-bottom: 40px;
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
