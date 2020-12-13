<template>
    <div>
      <el-row style="margin-bottom: 5px">
        <el-col class="myblogtitle"  style="font-size: 45px;font-weight: bold;font-family: Arial;word-break: break-all;text-align: center">
          {{this.blog.blogTitle}}
        </el-col>
      </el-row>
      <el-row style="margin-bottom: 5px">
        <el-image
          style="width: 100%;height: 300px"
          fit="fill"
          :src="this.blog.blogCoverImage">
        </el-image>
      </el-row>
      <el-divider></el-divider>
      <el-row style="margin-top: 2px;box-shadow: 0 0 50px 0 rgba(0,0,0,0.8);padding: 2px 0px;text-align: center">

        <el-col style=" font-size: medium;font-weight: bold;font-family: Arial;word-break: break-all;background-color: #ff7f50" :span="6">
          作者:{{this.blog.user.userName}}
        </el-col>

        <el-col style=" font-size: medium;font-weight: bold;font-family: Arial;word-break: break-all;background-color: greenyellow" :span="6">
          类别:{{this.blog.category.categoryName}}
        </el-col>

        <el-col style=" font-size: medium;font-weight: bold;font-family: Arial;background-color: powderblue" :span="6">
          发布时间:{{this.blog.createGmt}}
        </el-col >

        <el-col style=" font-size: medium;font-weight: bold;font-family: Arial;background-color: deepskyblue" :span="6">
          更新时间:{{this.blog.updateGmt}}
        </el-col>
      </el-row>
      <el-divider></el-divider>
      <el-row style="margin-top: 30px;margin-bottom: 30px;">
        <v-md-editor :value="blog.blogContent" mode="preview" @copy-code-success="handleCopyCodeSuccess"></v-md-editor>
      </el-row>
    </div>
</template>

<script>
    export default {
        name: "showBlogDetailForCommon",
        data(){
          return{
            blog:{
              blogId: '',
              blogTitle: '',
              blogCoverImage: '',
              blogContent: '',
              user: {
                userId: '',
                userName: ''
              },
              category: {
                categoryName: '',
                categoryRank: '',
              },
              createGmt: '',
              updateGmt: ''
            },
          }
        },
        methods:{
          cleanBlog(){
            this.blog.blogId=''
            this.blog.blogTitle=''
            this.blog.blogCoverImage=''
            this.blog.blogContent=''
            this.blog.user.userId=''
            this.blog.user.userName=''
            this.blog.category.categoryName=''
            this.blog.category.categoryRank=''
            this.blog.createGmt=''
            this.blog.updateGmt=''
          },
          handleCopyCodeSuccess(){
            this.$message({
              message: '代码复制成功',
              type: 'success',
              duration: 2000
            });
          },
          refresh(){
            this.$http.post("/common/blog/selectBlogByIdForCommon/"+this.$route.params.blogid).then(response=>{
              if (response!=null){
                this.blog=response.data.data
                document.title=this.blog.blogTitle
                this.$store.commit('setMainLoading',false)
              }
          }).catch(error=> {
              console.log(error)
              this.$store.commit('errorMsg',"请求发出错误！请稍后再试")
          })},
        },
        created() {
          document.documentElement.scrollTop=0
          document.body.scrollTop=0
          this.refresh();
        },
        watch: {
          '$route.params.blogid': function (to, from) {
            this.cleanBlog()
            this.$store.commit('setMainLoading',true)
            document.documentElement.scrollTop=0
            document.body.scrollTop=0
            this.refresh()
          }
        },
    }
</script>

<style scoped>
  .el-divider--horizontal{
    margin: 10px 0;
    background: #02f5c4;
  }
  @media only screen and (max-width: 767px) {
    .myblogtitle{
      color: white;
    }
  }
</style>
