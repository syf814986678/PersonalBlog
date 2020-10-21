<template>
  <el-container>
    <el-header class="myheader">
      <div class="mytitle">
        <el-row>
          <el-col :span="12">
            <span class="mytitletext">
              字符跳动
            </span>
          </el-col>
          <el-col :span="12">
            <div class="myavator">
              <el-avatar
                v-popover:popover
                shape="square"
                :size="45"
                fit="fill"
                :src="url">
              </el-avatar>
              <el-popover
                ref="popover"
                width="160"
                v-model="visible">
                <el-row>
                  <el-col :span="12"><span style="font-size: medium">id：</span></el-col>
                  <el-col :span="12"><el-tag style="font-size: medium;height: 1.5rem;line-height: 23px;width: 100%;text-align: center" effect="dark" type="success">1</el-tag></el-col>
                </el-row>
                <el-row style="margin-top: 0.5rem">
                  <el-col :span="12"><span style="font-size: medium">用户名：</span></el-col>
                  <el-col :span="12"><el-tag style="font-size: medium;height: 1.5rem;line-height: 23px;width: 100%;text-align: center" effect="dark" type="success">admin</el-tag></el-col>
                </el-row>
                <el-row>
                  <el-col :span="24">
                    <el-button @click="logout"
                               style="text-align: center;width: 100%;height: 0.5rem;margin-top: 0.5rem;line-height: 0;font-size: medium; letter-spacing: 5px"
                               type="danger" plain>注销
                    </el-button>
                  </el-col>
                </el-row>
              </el-popover>
            </div>
          </el-col>
        </el-row>
      </div>
    </el-header>
            <el-container style="margin-top: 5px;margin-left: 5px">
              <el-aside
                class="myaside"
                :width="this.$store.state.isCollapse?'65px':'200px'"
                @mouseenter.native="collapseOpen"
                @mouseleave.native="collapseClose">
                <el-menu router :default-active="$route.path" :collapse="this.$store.state.isCollapse" :collapse-transition="false">
                  <el-menu-item @click="reverse" style="text-align: center"><i class="el-icon-s-grid" style="font-size: 30px;margin-left: -5px"></i></el-menu-item>
                  <el-menu-item index="/admin/index"><i class="el-icon-s-platform"></i><span>主页</span></el-menu-item>
                  <el-submenu index="/admin/blog">
                    <template slot="title"><i class="el-icon-data-board"></i><span>博客管理</span></template>
                    <el-menu-item-group>
                      <el-menu-item index="/admin/showBlogList"><template slot="title"><i class="el-icon-s-operation"></i><span>博客列表</span></template></el-menu-item>
                      <el-menu-item index="/admin/showAddBlog"><template slot="title"><i class="el-icon-plus"></i><span>添加博客</span></template></el-menu-item>
                    </el-menu-item-group>
                  </el-submenu>
                  <el-submenu index="/admin/category">
                    <template slot="title"><i class="el-icon-collection-tag"></i><span>类别管理</span></template>
                    <el-menu-item-group>
                      <el-menu-item index="/admin/showCategoryList"><template slot="title"><i class="el-icon-s-operation"></i><span>类别列表</span></template></el-menu-item></el-menu-item-group>
                  </el-submenu>
                </el-menu>
              </el-aside>
              <el-main class="mymain" >
                <transition mode="out-in"
                            enter-active-class="animate__animated animate__bounceInDown animate__faster"
                            leave-active-class="animate__animated animate__bounceOutDown animate__faster">
                  <router-view/>
                </transition>
              </el-main>

            </el-container>
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
import "../assets/js/calc"
import {getHeight} from "../assets/js/calc";
export default {
  name: "myLayout",
  data() {
    return {
      visible: false,
      url: 'https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1593937482731&di=bece2a0fc9049db36a1cb5324a016222&imgtype=0&src=http%3A%2F%2Fdmimg.5054399.com%2Fallimg%2Fpkm%2Fpk%2F13.jpg'
    }
  },
  // components:{
  //   myheader: myHeader,
  //   myaside: myAside,
  //   mymain: myMain,
  //   myfooter: myFooter
  // },
  methods: {
    collapseOpen() {
      this.$store.commit('collapseOpen')
    },
    collapseClose() {
      this.$store.commit('collapseClose')
    },
    logout() {
      this.$store.commit('logout')
    },
  },
  created() {
    document.getElementsByClassName("mymain").style.height=getHeight();
  }
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
.myheader{
  background-color: #ffffff;
  margin-top: -6px;
}
.mytitle{
  background-color: #11c69c;
  border-radius: 20px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.5);
  margin: 3px -16px;
  padding: 5px 10px 0;
}
.mytitletext{
  font-size: xx-large;
  color: #fff;
  display: block;
  padding: 0 10px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.5);
  width: fit-content;
  margin-left: 5px;
  border-radius: 10px;
}
.myavator{
  float: right;
  margin-right: 5px;
}




@media only screen and (max-width: 767px) {
  .showTime {
    display: none;
  }
}

.myfooter{
  background-color: #fff;
  text-align: center;
  line-height: 60px;
  margin-top: 6px;
  margin-bottom: -10px;
  height: 48px !important;
}
.myfooterinfo{
  background-color: #333;
  border-radius: 20px;
  display: block;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.8);
  color: #ffffff;
  font-size: 15px;
  letter-spacing: 1px;
  height: 48px;
  text-align: center;
  margin: 0 -16px;
}



/*.my-el-aside {*/
/*  background-color: #3a92f1;*/
/*  color: #333;*/
/*  text-align: left;*/
/*}*/


/*.my-el-main {*/
/*  !*background-color: rgb(255, 215, 180);*!*/
/*  background: url("https://picture.chardance.cloud/myblog/frontResource/images/bg.png");*/
/*  background-size: cover;*/
/*  color: #333;*/
/*  padding-bottom: 0*/
/*}*/



/*.my-information {*/
/*  !*浅色投影*!*/
/*  margin-top: 10px;*/
/*  float: right;*/
/*}*/

</style>
