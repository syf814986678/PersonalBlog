<template>
    <div>
      <el-row style="margin-bottom: 5px">
        <el-col class="myblogtitle"  style="font-size: 45px;font-weight: bold;font-family: Arial;word-break: break-all;text-align: center">
          {{this.myblog.blogTitle}}
        </el-col>
      </el-row>
      <el-row style="margin-bottom: 5px">
        <el-image
          style="width: 100%;height: 300px"
          fit="fill"
          :src="this.myblog.blogCoverImage">
        </el-image>
      </el-row>
      <el-divider></el-divider>
      <el-row style="margin-top: 2px;box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.2);padding: 2px 0px;text-align: center">

        <el-col style=" font-size: medium;font-weight: bold;font-family: Arial;word-break: break-all;background-color: #ff7f50" :span="6">
          作者:{{this.myblog.myuser.userName}}
        </el-col>

        <el-col style=" font-size: medium;font-weight: bold;font-family: Arial;word-break: break-all;background-color: greenyellow" :span="6">
          类别:{{this.myblog.mycategory.categoryName}}
        </el-col>

        <el-col style=" font-size: medium;font-weight: bold;font-family: Arial;background-color: powderblue" :span="6">
          发布时间:{{this.myblog.createGmt}}
        </el-col >

        <el-col style=" font-size: medium;font-weight: bold;font-family: Arial;background-color: deepskyblue" :span="6">
          更新时间:{{this.myblog.updateGmt}}
        </el-col>
      </el-row>
      <el-divider></el-divider>
      <el-row style="margin-top: 30px;margin-bottom: 30px;">
        <v-md-editor :value="myblog.blogContent" mode="preview" @copy-code-success="handleCopyCodeSuccess"></v-md-editor>
      </el-row>
    </div>
</template>

<script>
    export default {
        name: "showBlogDetailForCommon",
        data(){
          return{
            myblog:{
              blogId: '',
              blogTitle: '',
              blogCoverImage: '',
              blogContent: '',
              myuser: {
                userId: '',
                userName: ''
              },
              mycategory: {
                categoryName: '',
                categoryRank: '',
              },
              createGmt: '',
              updateGmt: ''
            },
          }
        },
        methods:{
          handleCopyCodeSuccess(){
            this.$message({
              message: '代码复制成功',
              type: 'success',
              duration: 2000
            });
          },
          refresh(){
            this.$http.post("/blog/selectBlogByIdForCommon","blogId="+this.$route.params.blogid).then(response=>{
              if (response!=null){
                this.myblog=response.data.msg["myblog"]
                document.title=this.myblog.blogTitle
                this.$store.commit('setmainloading',false)
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
