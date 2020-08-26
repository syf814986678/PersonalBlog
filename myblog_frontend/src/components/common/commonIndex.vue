<template>
    <div>
      <el-container>
        <el-header class="myheader"><span class="my-index-title">字符跳动</span></el-header>
        <el-container>
              <el-col :xl="21" :xs="24" class="bg">
                <el-main class="mymain" id="myelmain">
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
                      <el-button round class="mybutton" v-for="item in mycategories" :key="item.categoryId" type="success" plain size="mini" style="font-size: 12px;font-weight: bold" @click="select(item.categoryId)">{{item.categoryName}}({{item.categoryRank}})</el-button>
                    </el-row>
                  </div>
                </el-col>
              </el-col>

              <el-col :xl="3" :xs="0" class="test">
                <el-aside
                  style="height: 815px"
                  class="myaside"
                  v-loading="loading" element-loading-text="拼命加载中" element-loading-spinner="el-icon-loading" element-loading-background="rgba(0, 0, 0, 0.8)">
                  <div class="mydiv2">
                    <el-row><el-tag effect="dark" type="danger" style="letter-spacing: 1px;font-size: 14px; font-family: Arial;margin-top: 10px">TOP10类型</el-tag></el-row>
                    <el-row style="margin-top: 10px">
                      <el-button round class="mybutton" v-for="item in mycategories" :key="item.categoryId" type="success" plain size="mini" style="font-size: 12px;font-weight: bold" @click="select(item.categoryId)">{{item.categoryName}}({{item.categoryRank}})</el-button>
                    </el-row>
                  </div>

                </el-aside>
              </el-col>
        </el-container>
        <el-footer class="myfooter">
          <span class="footer">
            <el-link style="margin-top: -18px;margin-bottom: 0" href="http://chardance.cloud" target="_blank" type="danger"><el-tag type="success" effect="dark" style="padding: 0 2px">字符跳动</el-tag></el-link>
            <el-link style="margin-top: -18px;margin-bottom: 0;cursor:default;font-family: Arial;font-size: 20px; " disabled>||</el-link>
            <el-link style="margin-top: -18px;margin-bottom: 0" href="http://www.beian.miit.gov.cn" target="_blank" type="danger">沪ICP备20013409号</el-link>
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
          loading: true,
        }
      },
      methods:{
        select(id){
          this.$router.push("/index/bloglist/category/"+id)
          this.$store.commit('clearMyBlogs')
          this.$store.commit('setMyBlogsTotal',null)
          this.$store.commit('setCommonCurrentPage',1)
          this.$store.commit('setHeight',0)
        },
      },
      created() {
        this.$http.post("/category/selectAllCategoryForCommon").then(response=>{
          if (response!=null){
            this.mycategories=response.data.msg["mycategories"]
            this.loading=false
          }
        }).catch(error=> {
          console.log(error)
          this.$store.commit('errorMsg',"请求发出错误！请稍后再试")
        })
      }
    }
</script>

<style scoped>
  .myheader {
    background-color: #ffffff;
    margin-top: 5px;
    text-align: center;
  }
  .myfooter  {
    background-color: #ffffff;
    text-align: center;
    line-height: 60px;
    margin-top: 3px;
    margin-bottom: -10px;
  }
  .test{
    max-width: 235px;
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
    height: 815px;
    margin: 5px auto;
  }
  .myaside {
    background-image: url("https://picture.chardance.cloud/myblog/bg/bg2.png");
    background-size: cover;
    color: #333;
    text-align: center;
    margin: 5px auto;
    max-width: 235px;
  }
  .mybutton{
    margin: 5px 5px;
  }
  @media only screen and (max-width: 767px) {
    .mydiv{
      box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.5);
      margin: 20px 10px;
      text-align: center;
      background-color: rgba(255, 234, 216, 0.63);
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
    }
  }



  /*.mydiv{*/
  /*  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.5);*/
  /*  margin: 20px 10px;*/
  /*  text-align: center;*/
  /*  background-color: rgba(255, 234, 216, 0.63);*/
  /*}*/
  /*.mydiv2{*/
  /*  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.5);*/
  /*  margin: 20px 10px;*/
  /*  text-align: center;*/
  /*  background-color: rgba(255, 234, 216, 0.63);*/
  /*}*/

</style>
