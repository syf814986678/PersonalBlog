<template>

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


</template>

<script>
    export default {
        name: "myAside",
        methods:{
          reverse(){
            this.$store.commit('collapseReverse')
          }
        }
    }
</script>

<style>

</style>
