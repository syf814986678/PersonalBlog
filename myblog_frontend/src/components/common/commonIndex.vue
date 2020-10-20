<template>
    <div>
      <el-container>
        <el-header class="myheader"><span class="my-index-title">字符跳动</span></el-header>
        <el-container>
              <el-col :xl="21" :xs="24" class="bg">

                <el-main v-loading="this.$store.state.mainloading" element-loading-text="拼命加载中" element-loading-spinner="el-icon-loading" element-loading-background="rgba(0, 0, 0, 0.8)" class="mymain" id="myelmain">
                  <transition mode="out-in"
                              enter-active-class="animate__animated animate__bounceInDown animate__faster"
                              leave-active-class="animate__animated animate__bounceOutDown animate__faster">
                      <router-view/>
                  </transition>
                </el-main>

                <el-col :xs="24" class="bordercolor">
                  <div class="mydiv" v-loading="loading" element-loading-text="拼命加载中" element-loading-spinner="el-icon-loading" element-loading-background="rgba(0, 0, 0, 0.8)">
                    <el-row><el-tag effect="dark" type="danger" style="letter-spacing: 1px;font-size: 14px; font-family: Arial;margin-top: 10px">TOP10类型</el-tag></el-row>
                    <el-row style="margin-top: 10px">
                      <el-button round class="mybutton" v-for="item in mycategories" :key="item.categoryId" type="success" plain size="mini" style="font-size: 12px;font-weight: bold" @click="select(item.categoryId,item.categoryName)">{{item.categoryName}}({{item.categoryRank}})</el-button>
                    </el-row>
                    <el-autocomplete
                      style="width: 80%;margin: 10px auto"
                      id="myinput2"
                      size="small"
                      :minlength="1"
                      :maxlength="30"
                      v-model="input"
                      :fetch-suggestions="querySearchAsync"
                      placeholder="请输入搜索内容"
                      @select="handleSelect"
                      suffix-icon=" el-icon-s-opportunity"
                      prefix-icon="el-icon-search">
                      <template slot-scope="{ item }">
                        <span>{{ item.value }}</span>
                        <i style="float: right;margin-top:10px" class="el-icon-trophy"></i>
                      </template>
                    </el-autocomplete>
                  </div>
                </el-col>

              </el-col>

              <el-col :xl="3" :xs="0" style="max-width: 235px;">
                <el-aside
                  style="height: 830px;width: 235px"
                  class="myaside"
                  v-loading="loading" element-loading-text="拼命加载中" element-loading-spinner="el-icon-loading" element-loading-background="rgba(0, 0, 0, 0.8)">
                  <div class="mydiv2">
                    <el-autocomplete
                      id="myinput1"
                      :minlength="1"
                      :maxlength="30"
                      v-model="input"
                      :fetch-suggestions="querySearchAsync"
                      placeholder="请输入搜索内容"
                      @select="handleSelect"
                      suffix-icon=" el-icon-s-opportunity"
                      prefix-icon="el-icon-search">
                      <template slot-scope="{ item }">
                        <span>{{ item.value }}</span>
                        <i style="float: right;margin-top:10px" class="el-icon-trophy"></i>
                      </template>
                    </el-autocomplete>
                  </div>
                  <div class="mydiv2">
                    <el-row><el-tag effect="dark" type="danger" style="letter-spacing: 1px;font-size: 14px; font-family: Arial;margin-top: 10px">TOP10类型</el-tag></el-row>
                    <el-row style="margin-top: 10px">
                      <el-button round class="mybutton" v-for="item in mycategories" :key="item.categoryId" type="success" plain size="mini" style="font-size: 12px;font-weight: bold" @click="select(item.categoryId,item.categoryName)">{{item.categoryName}}({{item.categoryRank}})</el-button>
                    </el-row>
                  </div>

                </el-aside>
              </el-col>
        </el-container>
        <el-footer class="myfooter">
          <span class="footer">
            <el-link style="margin-top: -16px;margin-bottom: 0" href="https://www.chardance.cloud" target="_blank" type="danger"><el-tag type="success" effect="dark" style="padding: 0 2px">字符跳动</el-tag></el-link>
            <el-link style="margin-top: -18px;margin-bottom: 0;cursor:default;font-family: Arial;font-size: 20px; " disabled>||</el-link>
            <el-link style="margin-top: -16px;margin-bottom: 0" href="http://www.beian.miit.gov.cn" target="_blank" type="danger">沪ICP备20013409号</el-link>
            <el-link style="margin-top: -18px;margin-bottom: 0;cursor:default;font-family: Arial;font-size: 20px; " disabled>||</el-link>
            <el-link class="showTime" style="margin-top: -16px;margin-bottom: 0" href="http://time.tianqi.com/" target="_blank" type="success"></el-link>
          </span>
        </el-footer>
      </el-container>
    </div>
</template>


<script>
    export default {
        name: "commonIndex",
      data(){
        return{
          mycategories:[],
          loading: false,
          input:'',
          hotkeys: []
        }
      },
      methods:{
        querySearchAsync(queryString,cb){
          this.$http.post("/blog/hotkeys").then(response=>{
            if (response!=null){
              this.hotkeys=response.data.msg["hotkeys"]
              cb(queryString ? this.hotkeys.filter(this.createStateFilter(queryString)) : this.hotkeys);
            }
          }).catch(error=> {
            console.log(error)
            this.$store.commit('errorMsg',"请求发出错误！请稍后再试")
          })
          cb(queryString ? this.hotkeys.filter(this.createStateFilter(queryString)) : this.hotkeys);
        },
        createStateFilter(queryString) {
          return (input) => {
            return (input.value.toLowerCase().indexOf(queryString.toLowerCase()) === 0);
          };
        },
        handleSelect(){
          this.search()
        },
        search(){
          if (this.input.length>=1 && (this.$route.params.whatsearch!==this.input)){
            this.$router.push("/index/search/"+this.input)
            this.$store.commit('setmainloading',true)
            this.$store.commit('setsearch',this.input)
            document.documentElement.scrollTop=0
            document.body.scrollTop=0
          }
        },
        select(id,name){
          this.$store.commit('setHeight',0)
          this.$store.commit('setCommonCurrentPage',1)
          this.$router.push("/index/bloglist/category/"+id)
          document.documentElement.scrollTop=0
          document.body.scrollTop=0
        },
      },
      created() {
        const that =this;
        if (this.$route.params.whatsearch!=null){
          this.input=this.$route.params.whatsearch
        }
        this.$http.post("/category/selectAllCategoryForCommon").then(response=>{
          if (response!=null){
            this.mycategories=response.data.msg["mycategories"]
            this.loading=false
            document.onkeydown = function (e) {
              // 回车提交表单
              // 兼容FF和IE和Opera
              var theEvent = window.event || e;
              var code = theEvent.keyCode || theEvent.which || theEvent.charCode;
              if ((document.activeElement.id==="myinput1" ||  document.activeElement.id==="myinput2")&& code === 13) {
                that.search()
                document.getElementById('myinput1').blur();
                document.getElementById('myinput2').blur();
              }
            }
          }
        }).catch(error=> {
          console.log(error)
          this.$store.commit('errorMsg',"请求发出错误！请稍后再试")
        })
      },
      beforeRouteLeave (to, from, next) {
        document.onkeydown = undefined
        next()
      },
    }
    var t = null;
    t = setTimeout(time, 1000); //開始运行
    function time() {
      clearTimeout(t); //清除定时器
      const dt = new Date();
      var y = dt.getFullYear();
      var mt = dt.getMonth() + 1;
      var day = dt.getDate();
      var h = dt.getHours(); //获取时
      var m = dt.getMinutes(); //获取分
      var s = dt.getSeconds(); //获取秒
      document.querySelector(".showTime").innerHTML =
        y +
        "年" +
        mt +
        "月" +
        day +
        "日" +
        h +
        "时" +
        m +
        "分" +
        s +
        "秒";
      t = setTimeout(time, 1000); //设定定时器，循环运行
    }
</script>

<style scoped>
  .myheader {
    background-color: #ffffff;
    margin-top: -6px;
    text-align: center;
  }
  .myfooter  {
    background-color: #ffffff;
    text-align: center;
    line-height: 60px;
    margin-top: 6px;
    margin-bottom: -10px;
    height: 48px !important;

  }
  .footer {
    background-color: #333;
    border-radius: 20px;
    display: block;
    box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.8);
    color: #ffffff;
    font-family: Arial;
    font-size: 15px;
    letter-spacing: 1px;
    height: 48px;
    text-align: center;
    margin: 0 -16px;
  }
  .my-index-title{
    background-color: #11c69c;
    border-radius: 20px;
    font-size: xx-large;
    box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.5);
    margin: 3px -16px;
    padding: 5px 10px;
    font-family: Arial;
    color: #fff;
    letter-spacing: 1px;
    display: block;
  }
  .mymain {
    background-size: cover;
    padding: 0px 1px;
    height: 830px;
    margin: 5px auto;
  }
  .myaside {
    background-image: url("https://picture.chardance.cloud/myblog/bg/bg2.png");
    background-size: cover;
    text-align: center;
    margin: 5px auto;
    max-width: 235px;
  }
  .mybutton{
    margin: 5px 5px;
  }
  @media only screen and (max-width: 767px) {
    .showTime{
      display: none;
    }
    .mydiv{
      box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.5);
      margin: 20px 10px;
      text-align: center;
      background-color: rgba(255, 234, 216, 0.63);
      border-radius: 10px
    }
    .mydiv2{
      display: none !important;
    }
    .bg{
      background-image: url("https://picture.chardance.cloud/myblog/bg/bg4.png");
      background-size: cover;
    }
    .bordercolor{
      border-top: cyan 1px solid;
    }

  }
  @media only screen and (min-width: 768px) {
    .mymain{
      background-image: url("https://picture.chardance.cloud/myblog/bg/bg1.png");
    }
    .mydiv{
      display: none !important;
    }
    .mydiv2{
      box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.5);
      margin: 20px 10px;
      text-align: center;
      background-color: rgba(255, 234, 216, 0.63);
      border-radius: 10px
    }
  }

</style>
