<template>
  <el-carousel indicator-position="outside" :interval="2000" height="720px">
    <el-carousel-item v-for="blog in blogs" :key="blog.blogId" :label="blog.blogTitle" style="text-align: center">
        <el-col :span="24" style="margin-bottom: 10px;text-align: center"><h3>{{blog.blogTitle}}</h3></el-col>
        <el-image
          style="height: 720px;width: 1500px;cursor:pointer"
          :src="blog.blogCoverImage"
          @click="open(blog.blogId)"
          fit="fill">
        </el-image>
    </el-carousel-item>
  </el-carousel>
</template>

<script>
    export default {
        name: "Index",
        data(){
          return{
            blogs:[],
          }
        },
        methods:{
          open(myurl){
            window.open("/#/index/blog/"+myurl)
          },
        },
        created() {
          this.$http.post("/blog/index").then(response=>{
            if (response!=null){
              this.blogs=response.data.msg["myblogs"];
            }
          }).catch(error=> {
            console.log(error)
            this.$store.commit('errorMsg',"请求发出错误！请稍后再试")
          })

        }
    }
</script>

<style scoped>

</style>
