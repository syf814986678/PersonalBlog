<template>
  <div
      v-infinite-scroll="load"
      :infinite-scroll-disabled="disabled">
      <div class="all" v-if="calcScreen" v-for="blog in myblogs" :key="blog.blogId" style="text-align: center;margin: 10px auto 5px;width: 95%;">
          <div class="maoboli">
            <el-tag effect="dark" type="danger" @click="showBlog(blog.blogId)" style="cursor:pointer;font-size: 20px">{{blog.blogTitle}}</el-tag>
            <el-row style="margin-top: 10px">
              <el-col :span="6">
                <el-tag effect="dark" type="warning" @click="selectauthor(blog.myuser.userId)" style="cursor:pointer;font-size: 14px ">作者：{{blog.myuser.userName}}</el-tag>
              </el-col>
              <el-col :span="6">
                <el-tag effect="dark" type="success" @click="select(blog.mycategory.categoryId)" style="cursor:pointer;font-size: 14px">类别：{{blog.mycategory.categoryName}}</el-tag>
              </el-col>
              <el-col :span="6">
                <el-tag effect="dark" type="primary" style="font-size: 14px">发布时间：{{blog.createGmt}}</el-tag>
              </el-col>
              <el-col :span="6">
                <el-tag effect="dark" type="primary" style="font-size: 14px">修改时间：{{blog.updateGmt}}</el-tag>
              </el-col>
            </el-row>
            <el-image
              style="height: 640px;width: 95%;cursor:pointer;margin-top: 5px;border-radius: 20px"
              :src="blog.blogCoverImage"
              :lazy="lazy"
              @click="showBlog(blog.blogId)"
              fit="fill" ></el-image>
          </div>
        <el-divider></el-divider>

      </div>

      <div v-if="!calcScreen" v-for="blog in myblogs" :key="blog.blogId" style="text-align: center;margin: 5px auto;width: 98%">
        <div class="maoboli2">
          <el-tag effect="dark" type="danger" @click="showBlog(blog.blogId)" style="cursor:pointer;font-size: 13px">{{blog.blogTitle}}</el-tag>
          <el-row style="margin-top: 10px;margin-bottom: 3px">
            <el-col :span="12">
              <el-tag effect="dark" type="warning" @click="selectauthor(blog.myuser.userId)" style="cursor:pointer;font-size: 11px;">作者:{{blog.myuser.userName}}</el-tag>
            </el-col>
            <el-col :span="12">
              <el-tag effect="dark" type="success" @click="select(blog.mycategory.categoryId)" style="cursor:pointer;font-size: 11px;">类别:{{blog.mycategory.categoryName}}</el-tag>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="12">
              <el-tag effect="dark" type="primary" style="font-size: 11px;width: 95%;margin-right: -6px;">Create:{{blog.createGmt}}</el-tag>
            </el-col>
            <el-col :span="12">
              <el-tag effect="dark" type="primary" style="font-size: 11px;width: 95%;margin-left: -6px;">Update:{{blog.updateGmt}}</el-tag>
            </el-col>
          </el-row>
          <el-image
          style="height: 180px;width: 95%;cursor:pointer;margin-top: 5px;border-radius: 20px"
          :src="blog.blogCoverImage"
          :lazy="lazy"
          @click="showBlog(blog.blogId)"
          fit="fill" ></el-image>
        </div>
        <el-divider></el-divider>
      </div>
    </div>
</template>

<script>
  export default {
    name: "showBlogListForCommon",
    data() {
      return  {
        myblogs:[],
        currentPage: this.$store.state.commonCurrentPage,
        pageSize: 5,
        total: null,
        disabled: true,
        lazy: true
      }
    },
    computed:{
      calcScreen(){
        return window.screen.width>=1080
      }
    },
    methods:{
      load(){
         this.disabled=true
          if (this.myblogs.length >= this.total){
            this.$message({
              message: '博客全部加载完毕',
              type: 'error',
              duration: 1000
            });
          }
          else {
            this.$message({
              message: '博客加载中',
              type: 'warning',
              duration: 500
            });
            this.$store.commit('setCommonCurrentPage',++this.currentPage)
            this.refresh(this.currentPage)
          }
      },
      selectauthor(id){
        this.$router.push("/index/bloglist/author/"+id)
      },
      select(id){
        this.$router.push("/index/bloglist/category/"+id)
      },
      refresh(page){
        if (this.$route.params.bloglist==="all" && this.$route.params.bloglist2==="all"){
          this.$http.post("/blog/selectLastestBlogByPagForCommon","pageNow="+page+"&pageSize="+this.pageSize).then(response=>{
            if (response!=null){
              this.$store.commit('setMyBlogs',response.data.msg["myblogs"])
              this.$store.commit('setMyBlogsTotal',response.data.msg["nums"])
              this.myblogs=this.$store.state.myblogs
              this.total=this.$store.state.total
              if (this.total>2){
                setTimeout(() => {
                  this.disabled=false
                }, 500)
              }
              else {
                this.lazy=false
              }
            }
          }).catch(error=> {
            console.log(error)
            this.$store.commit('errorMsg',"请求发出错误！请稍后再试")
          })
        }
        else if (this.$route.params.bloglist==="category"){
          this.$http.post("/blog/selectBlogByCategoryIdAndPageForCommon","pageNow="+page+"&pageSize="+this.pageSize+"&categoryid="+this.$route.params.bloglist2).then(response=>{
            if (response!=null){
              this.$store.commit('setMyBlogs',response.data.msg["myblogs"])
              this.$store.commit('setMyBlogsTotal',response.data.msg["nums"])
              this.myblogs=this.$store.state.myblogs
              this.total=this.$store.state.total
              if (this.total>2){
                setTimeout(() => {
                  this.disabled=false
                }, 500)
              }
              else {
                this.lazy=false
              }
            }
          }).catch(error=> {
            console.log(error)
            this.$store.commit('errorMsg',"请求发出错误！请稍后再试")
          })
        }
        else if (this.$route.params.bloglist==="author"){
          this.$http.post("/blog/selectBlogByAuthorForCommon","pageNow="+page+"&pageSize="+this.pageSize+"&userid="+this.$route.params.bloglist2).then(response=>{
            if (response!=null){
              this.$store.commit('setMyBlogs',response.data.msg["myblogs"])
              this.$store.commit('setMyBlogsTotal',response.data.msg["nums"])
              this.myblogs=this.$store.state.myblogs
              this.total=this.$store.state.total
              if (this.total>2){
                setTimeout(() => {
                  this.disabled=false
                }, 500)
              }
              else {
                this.lazy=false
              }
            }
          }).catch(error=> {
            console.log(error)
            this.$store.commit('errorMsg',"请求发出错误！请稍后再试")
          })
        }
      },
      showBlog(blogId){
        this.$router.push("/index/blog/"+blogId)
      },
    },
    created() {
      if(this.$store.state.myblogs.length===0){
        this.refresh(this.currentPage)
      }
      else {
        this.myblogs = this.$store.state.myblogs
        this.total = this.$store.state.total
      }
      if(this.$store.state.height===0){
        if (this.total>2){
          setTimeout(() => {
            document.getElementById("myelmain").scrollTop=1
            this.disabled=false
          }, 500)
        }
        else {
          this.lazy=false
        }
      }
      else {
        if (this.total>2){
          setTimeout(() => {
            document.getElementById("myelmain").scrollTop=this.$store.state.height
            this.disabled=false
          }, 500)
        }
        else {
          this.lazy=false
        }
      }
      this.$store.commit('setmainloading',false)
    },
    beforeRouteLeave (to, from, next) {
      this.disabled= true
      this.$store.commit('setHeight',document.getElementById("myelmain").scrollTop)
      next()
    },
    watch: {
      '$route': function (to, from) {
        this.disabled=true
        this.currentPage=1
        this.$store.commit('clearMyBlogs')
        this.$store.commit('setMyBlogsTotal',null)
        this.$store.commit('setCommonCurrentPage',1)
        this.$store.commit('setHeight',0)
        this.refresh(this.currentPage)
        setTimeout(() => {
          document.documentElement.scrollTop=0
          document.getElementById("myelmain").scrollTop=1
        }, 100)
      }
    },
  }
</script>

<style>
.maoboli{
  position: relative;
  z-index: 1;
  background-position: center top;
  background-size: cover;
  overflow: hidden;
  padding-top: 15px;
  padding-bottom: 15px;
  border-radius: 20px
}

.maoboli:before{
  margin: 10px;
  content: '';
  position: absolute;
  top: -10px;
  left: 0;
  right: 0;
  bottom: -10px;
  background-color: rgba(255, 255, 255, 0.5);
  z-index: -1;
  background-position: center top;
  background-size: cover;
  background-attachment: fixed;
  -webkit-filter: blur(20px);
  -moz-filter: blur(20px);
  -ms-filter: blur(20px);
  -o-filter: blur(20px);
  filter: blur(10px);
  box-shadow: 0 0 12px 15px rgba(0, 0, 0, 0.8);
}

.maoboli:hover:before{
  background: linear-gradient(90deg, #ddf864, #05ddfa, #070707,#05ddfa, #ddf864);
  opacity: 0.4;
}

.maoboli2{
  position: relative;
  z-index: 1;
  background-position: center top;
  background-size: cover;
  overflow: hidden;
  padding-top: 15px;
  padding-bottom: 5px;
  border-radius: 20px;
}

.maoboli2::before{
  margin: 10px;
  content: '';
  position: absolute;
  top: -10px;
  left: -10px;
  right: -10px;
  bottom: -10px;
  background-color: rgba(255, 255, 255, 0.7);
  z-index: -1;
  background-position: center top;
  background-size: cover;
  background-attachment: fixed;
  -webkit-filter: blur(20px);
  -moz-filter: blur(20px);
  -ms-filter: blur(20px);
  -o-filter: blur(20px);
  filter: blur(10px);
  box-shadow: 0 0 12px 15px rgba(0, 0, 0, 0.8);

}
  .el-divider--horizontal{
    background: #02f5c4;
  }
</style>
