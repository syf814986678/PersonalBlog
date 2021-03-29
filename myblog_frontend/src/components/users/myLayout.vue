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
                  <el-col :span="12"><el-tag style="font-size: medium;height: 1.5rem;line-height: 23px;width: 100%;text-align: center" effect="dark" type="success">{{ this.$store.state.user.userId}}</el-tag></el-col>
                </el-row>
                <el-row style="margin-top: 0.5rem">
                  <el-col :span="12"><span style="font-size: medium">用户名：</span></el-col>
                  <el-col :span="12"><el-tag style="font-size: medium;height: 1.5rem;line-height: 23px;width: 100%;text-align: center" effect="dark" type="success">{{this.$store.state.user.userName}}</el-tag></el-col>
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
    <el-container class="mycontainer">
      <el-aside
        class="myaside"
        :width="this.$store.state.isCollapse?'65px':'200px'"
        :style="{height:this.height+'px'}"
        @mouseenter.native="collapseOpen"
        @mouseleave.native="collapseClose">
        <el-menu background-color="#545c64" text-color="#fff" active-text-color="#ffd04b" router :default-active="$route.path" :collapse="this.$store.state.isCollapse" :collapse-transition="false">
          <el-menu-item @click="reverse" style="text-align: center"><i class="el-icon-s-grid" style="font-size: 30px;margin-left: -5px"></i></el-menu-item>
          <el-menu-item index="/admin/index"><i class="el-icon-s-platform"></i><span>主页</span></el-menu-item>
          <el-submenu index="1">
            <template slot="title"><i class="el-icon-data-board"></i><span>博客管理</span></template>
            <el-menu-item-group>
              <el-menu-item index="/admin/showBlogList"><template slot="title"><i class="el-icon-s-operation"></i><span>博客列表</span></template></el-menu-item>
              <el-menu-item index="/admin/showAddBlog"><template slot="title"><i class="el-icon-plus"></i><span>添加博客</span></template></el-menu-item>
            </el-menu-item-group>
          </el-submenu>
          <el-submenu index="2">
            <template slot="title"><i class="el-icon-collection-tag"></i><span>类别管理</span></template>
            <el-menu-item-group>
              <el-menu-item index="/admin/showCategoryList"><template slot="title"><i class="el-icon-s-operation"></i><span>类别列表</span></template></el-menu-item></el-menu-item-group>
          </el-submenu>
        </el-menu>
      </el-aside>
      <el-main class="mymain" :style="{height:this.height+'px'}" >
        <div class="mobile">
          <el-menu background-color="#545c64" text-color="#fff" active-text-color="#ffd04b" mode="horizontal" router :default-active="$route.path">
            <el-menu-item index="/admin/index"><i class="el-icon-s-platform"></i><span>主页</span></el-menu-item>
            <el-submenu index="1">
              <template slot="title"><i class="el-icon-data-board"></i><span>博客</span></template>
              <el-menu-item-group>
                <el-menu-item index="/admin/showBlogList"><template slot="title"><i class="el-icon-s-operation"></i><span>博客列表</span></template></el-menu-item>
                <el-menu-item index="/admin/showAddBlog"><template slot="title"><i class="el-icon-plus"></i><span>添加博客</span></template></el-menu-item>
              </el-menu-item-group>
            </el-submenu>
            <el-submenu index="2">
              <template slot="title"><i class="el-icon-collection-tag"></i><span>类别</span></template>
              <el-menu-item-group>
                <el-menu-item index="/admin/showCategoryList"><template slot="title"><i class="el-icon-s-operation"></i><span>类别列表</span></template></el-menu-item></el-menu-item-group>
            </el-submenu>
          </el-menu>
        </div>
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
        <el-link style="margin-top: -16px;margin-bottom: 0" href="https://beian.miit.gov.cn/" target="_blank" type="danger">沪ICP备20013409号-1</el-link>
        <el-link style="margin-top: -18px;margin-bottom: 0;cursor:default;font-family: Arial;font-size: 20px; " disabled>||</el-link>
        <el-link class="showTime" style="margin-top: -16px;margin-bottom: 0" href="http://time.tianqi.com/" target="_blank" type="success"></el-link>
      </div>
    </el-footer>
  </el-container>
</template>

<script>
import {time} from "../../assets/js/showTime"
import {getHeight, getWidth} from "../../assets/js/calc";
export default {
  name: "myLayout",
  data() {
    return {
      visible: false,
      url: 'https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1593937482731&di=bece2a0fc9049db36a1cb5324a016222&imgtype=0&src=http%3A%2F%2Fdmimg.5054399.com%2Fallimg%2Fpkm%2Fpk%2F13.jpg',
      height: 0,
    }
  },
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
    reverse(){
      this.$store.commit('collapseReverse')
    },
    // handleSelect(index,indexPath){
    //   // console.log(indexPath)
    //   // console.log(indexPath[1])
    //   // this.$refs.mymenu.open(index)
    //   this.$refs.mymenu.open(indexPath[1])
    //
    // }
  },
  created() {
    const that=this
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
      }}
    )
    time()
  }
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
  box-shadow: 0 0 30px 0 rgba(0, 0, 0, 0.5);
  margin: 3px -16px;
  padding: 5px 10px 0;
}
.mytitletext{
  font-size: xx-large;
  color: #fff;
  display: block;
  padding: 0 10px;
  box-shadow: 0 0 12px 0 rgba(0, 0, 0, 0.5);
  width: fit-content;
  margin-left: 5px;
  border-radius: 10px;
}
.myavator{
  float: right;
  margin-right: 5px;
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

@media only screen and (max-width: 767px) {
  .mycontainer{
    background-image: url("https://picture.chardance.cloud/myblog/bg/bgMobile.png");
    background-size: cover;
  }
  .mobile{
    margin-bottom: 15px;
    text-align: center;
  }
  .myaside {
    display: none;
  }
  .mymain{
    padding: 13px 5px 0;
  }
  .showTime {
    display: none;
  }

}
@media only screen and (min-width: 768px) {
  .mycontainer{
    background-image: url("https://picture.chardance.cloud/myblog/frontResource/images/bg.png ");
    background-size: cover;
  }
  .myaside {
    background-color: #545c64;
    color: #333;
    text-align: left;
  }
  .mobile{
    display: none;
  }
}
</style>
