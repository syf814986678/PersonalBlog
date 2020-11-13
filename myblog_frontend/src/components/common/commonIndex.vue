<template>
  <el-container class="topcontainer">
    <el-header class="myheader"><div class="mytitle"><span class="mytitletext">字符跳动</span></div></el-header>
    <el-container class="mycontainer">
      <el-main :style="{height:this.height+'px'}" class="mymain" id="myelmain" v-loading="this.$store.state.mainloading" element-loading-text="拼命加载中" element-loading-spinner="el-icon-loading" element-loading-background="rgba(0, 0, 0, 0.8)">
        <transition mode="out-in"
                    enter-active-class="animate__animated animate__bounceInDown animate__faster"
                    leave-active-class="animate__animated animate__bounceOutDown animate__faster">
          <router-view/>
        </transition>
      </el-main>
      <el-aside
        class="myaside"
        :style="{height:this.height+'px'}"
        style="max-width: 235px"
        v-loading="loading" element-loading-text="拼命加载中" element-loading-spinner="el-icon-loading" element-loading-background="rgba(0, 0, 0, 0.8)">
        <div class="myautocomplete">
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
        <el-divider></el-divider>
        <div class="mycategory">
          <el-row><el-tag effect="dark" type="danger" style="letter-spacing: 1px;font-size: 14px;margin-top: 10px">TOP10类型</el-tag></el-row>
          <el-row style="margin-top: 10px">
            <el-button class="mybutton" round v-for="item in mycategories" :key="item.categoryId" type="success" plain size="mini" style="font-size: 12px;font-weight: bold" @click="select(item.categoryId,item.categoryName)">{{item.categoryName}}({{item.categoryRank}})</el-button>
          </el-row>
        </div>
        <el-divider></el-divider>
        <div class="mycategory infodiv">
          <span style="display: block" class="info motto-shake"></span>
        </div>
          <!--              <el-carousel height="200px" direction="vertical" :autoplay="false">-->
<!--                <el-carousel-item v-for="item in 3" :key="item">-->
<!--                  <h3 >{{ item }}</h3>-->
<!--                </el-carousel-item>-->
<!--              </el-carousel>-->
      </el-aside>
    </el-container>
    <div class="mobile" v-loading="loading" element-loading-text="拼命加载中" element-loading-spinner="el-icon-loading" element-loading-background="rgba(0, 0, 0, 0.8)">
      <div class="mycategory">
        <el-autocomplete
          id="myinput2"
          style="margin-top: 15px;width: 90%"
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
        <el-row><el-tag effect="dark" type="danger" style="letter-spacing: 1px;font-size: 14px;margin-top: 10px">TOP10类型</el-tag></el-row>
        <el-row style="margin-top: 5px;padding-bottom: 10px">
          <el-button class="mybutton" round v-for="item in mycategories" :key="item.categoryId" type="success" plain size="mini" style="font-size: 12px;font-weight: bold" @click="select(item.categoryId,item.categoryName)">{{item.categoryName}}({{item.categoryRank}})</el-button>
        </el-row>
      </div>
    </div>
    <el-footer class="myfooter">
      <div class="myfooterinfo">
        <el-link style="margin-top: -16px;margin-bottom: 0" href="https://www.chardance.cloud" target="_blank" type="danger"><el-tag type="success" effect="dark" style="padding: 0 2px">字符跳动</el-tag></el-link>
        <el-link style="margin-top: -18px;margin-bottom: 0;cursor:default;font-family: Arial;font-size: 20px; " disabled>||</el-link>
        <el-link style="margin-top: -16px;margin-bottom: 0" href="http://www.beian.miit.gov.cn" target="_blank" type="danger">沪ICP备20013409号</el-link>
        <el-link style="margin-top: -18px;margin-bottom: 0;cursor:default;font-family: Arial;font-size: 20px; " disabled>||</el-link>
        <el-link class="showTime" style="margin-top: -16px;margin-bottom: 0" href="http://time.tianqi.com/" target="_blank" type="success"></el-link>
      </div>
    </el-footer>
  </el-container>
</template>
<script>
    import {time} from "../../assets/js/showTime";
    import {getHeight} from "../../assets/js/calc";
    import {getWidth} from "../../assets/js/calc";
    export default {
        name: "commonIndex",
      data(){
        return{
          mycategories:[],
          loading: true,
          input:'',
          hotkeys: [],
          height:0,
          info:["经汝之手，晓后世之荣耀,吾等世界终将拨乱反正，永垂不朽","我一个人来到这个世界，给我勇气一个人离开这个世界"]
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
        select(id){
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
            for (let i = 0; i < this.mycategories.length; i++) {
              this.$store.commit('setCategory',[this.mycategories[i].categoryId,this.mycategories[i].categoryName])
            }
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
        if (getWidth()>1000){
          this.height=getHeight()-125
        }
        else {
          this.height=getHeight()-60
        }
        window.addEventListener("resize",function (){
          if (getWidth()>1000){
            that.height=getHeight()-125
          }
          else {
            that.height=getHeight()-60
          }
        })
        time()
        window.onload=function(){
          var i=0;
          new Motto('.info', {
            lyric: that.info[i],
            showUpSpeed: 150,
            callback: function () {
              setTimeout(function (){
                document.getElementsByClassName("info")[0].classList.remove('motto-shake')
              },5000)
            }
          })
        };
      },
      beforeRouteLeave (to, from, next) {
        document.onkeydown = undefined
        next()
      },
    }
</script>
<style scoped>
  .myheader {
    background-color: #ffffff;
    margin-top: -6px;
    text-align: center;
  }
  .mytitle{
    background-color: #11c69c;
    border-radius: 20px;
    box-shadow: 0 0 30px 0 rgba(0, 0, 0, 0.5);
    margin: 3px -16px;
    padding: 3px 10px ;
    text-align: center;
  }
  .mytitletext{
    font-size: 35px;
    color: #fff;
    display: block;
    text-align: center;
  }

  .myfooter{
    background-color: #fff;
    text-align: center;
    line-height: 60px;
    margin-top: 3px;
    /*margin-bottom: -10px;*/
    height: 48px !important;
  }
  .myfooterinfo{
    background-color: #333;
    border-radius: 20px;
    display: block;
    box-shadow: 0 0 30px 0 rgba(0, 0, 0, 0.8);
    color: #ffffff;
    font-size: 15px;
    letter-spacing: 1px;
    height: 48px;
    text-align: center;
    margin: 3px -20px;
  }

  .mymain {
    padding: 0 3px;
  }

  .myautocomplete{
    box-shadow: 0 0 30px 5px rgba(106, 245, 152,0.7);
    margin: 10px 10px 0;
    text-align: center;
    background-color: rgba(255, 234, 216, 0.63);
    border-radius: 10px
  }
  .mycategory{
    box-shadow: 0 0 30px 5px rgba(106, 245, 152,0.7);
    margin: 0 10px 10px;
    text-align: center;
    background-color: rgba(255, 234, 216, 0.63);
    border-radius: 10px;
  }



  @media only screen and (max-width: 767px) {
    .topcontainer{
      background-image: url("https://picture.chardance.cloud/myblog/bg/bg4.png");
      background-size: cover;
    }
    .myaside{
      display: none;
    }
    .mobile{
      text-align: center;
      padding-top: 15px;
      border-top: cyan solid 1px;
    }
    .showTime {
      display: none;
    }
  }
  @media only screen and (min-width: 768px) {
    .mycontainer{
      background-image: url("https://picture.chardance.cloud/myblog/bg/bg.jpg");
      background-size: cover;
    }
    .myaside {
      text-align: center;
      margin: 0 auto;
      padding: 5px 0;
    }
    .mobile{
      display: none;
    }
    .info{
      font-weight: bold;
      font-size: 20px;
      color: #5d07fc;
    }
    .infodiv{
      padding: 15px;
    }
  }
  .mybutton{
    margin: 4px;
  }
  .el-divider--horizontal{
    background: cyan;
  }

</style>
