<template>
  <el-container>
        <el-header class="my-el-header"><myheader/></el-header>
        <el-container>
            <el-aside
              class="my-el-aside"
              :width="this.$store.state.isCollapse?'65px':'200px'"
              style="height: 815px;margin-left: -5px"
              @mouseenter.native="collapseOpen"
              @mouseleave.native="collapseClose">
              <myaside ></myaside>
            </el-aside>
          <el-main class="my-el-main"><mymain/></el-main>
        </el-container>
        <el-footer class="my-el-footer"><myfooter/></el-footer>
  </el-container>
</template>

<script>
    import myHeader from "../components/myHeader";
    import myFooter from "../components/myFooter";
    import myAside from "../components/myAside";
    import myMain from "../components/myMain";
    export default {
        name: "myLayout",
        components:{
          myheader: myHeader,
          myaside: myAside,
          mymain: myMain,
          myfooter: myFooter
        },
        methods:{
          collapseOpen(){
            this.$store.commit('collapseOpen')
          },
          collapseClose(){
            this.$store.commit('collapseClose')
          }

        }
    }
</script>

<style scoped>
  .my-el-aside {
    background-color: #3a92f1;
    color: #333;
    text-align: left;
  }
  .my-el-footer  {
    background-color: #333;
    color: #333;
    text-align: center;
    line-height: 60px;
    margin-left: -5px
  }
  .my-el-header {
    background-color: #11c69c;
    color: #333;
    margin-top: 15px;
    margin-left: -5px
  }
  .my-el-main {
    background-color: rgb(255, 215, 180);
    color: #333;
    height: 815px;
    padding-bottom: 0
  }
</style>
