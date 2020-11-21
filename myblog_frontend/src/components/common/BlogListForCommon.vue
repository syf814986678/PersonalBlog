<template>
  <div
      v-infinite-scroll="load"
      :infinite-scroll-disabled="disabled">
      <div v-for="blog in myblogs" :key="blog.blogId" style="text-align: center;margin: 10px auto 5px;width: 98%  ">
          <div class="maoboli">
            <h2 class="blogTitle" @click="showBlog(blog.blogId)">{{blog.blogTitle}}</h2>
            <el-row style="margin-top: 10px;margin-bottom: 0">
              <el-col :span="24">
                <p class="blogCategory" @click="select(blog.mycategory.categoryId,blog.mycategory.categoryName)"><i class="el-icon-collection-tag myi"></i>{{blog.mycategory.categoryName}}</p>
              </el-col>
            </el-row>
            <el-row style="margin-top: 5px">
              <el-col :span="12">
                <p class="blogCreateGmt"><i class="el-icon-date" style="margin-right: 2px"></i>{{blog.createGmt}}</p>
              </el-col>
              <el-col :span="12">
                <p class="blogCreateGmt"><i class="el-icon-edit" style="margin-right: 2px"></i>{{blog.updateGmt}}</p>
              </el-col>
            </el-row>
            <el-image
              class="myimage"
              :src="blog.blogCoverImage"
              :lazy="lazy"
              @click="showBlog(blog.blogId)"
              fit="fill" ></el-image>
            <el-row style="margin-bottom: 7px" class="myrow">
              <el-col :span="24">
                <el-tag effect="dark" type="danger" @click="showBlog(blog.blogId)" style="cursor:pointer;font-size: 10px;width: 95%;border-radius: 15px">阅读全文</el-tag>
              </el-col>
            </el-row>
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
        pageSize: 5,
        total: null,
        disabled: true,
        lazy: true,
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
            if (this.$route.params.bloglist==="all" && this.myblogs.length>=10){
              this.$message({
                message: '请选择右侧类型查看更多博客！',
                type: 'error',
                duration: 2500,
                center: true
              });
            }
            else {
              this.$message({
                message: '博客加载中',
                type: 'warning',
                duration: 500
              });
              this.$store.commit('setCommonCurrentPage',++this.$store.state.commonCurrentPage)
              this.refresh(this.$store.state.commonCurrentPage)
            }
          }
      },
      select(id){
        this.$router.push("/index/bloglist/category/"+id)
      },
      refresh(page){
        if (this.$route.params.bloglist==="all"){
          this.$http.post("/blog/selectBlogsByPageForCommon/"+this.pageSize+"/"+page).then(response=>{
            if (response!=null){
              this.myblogs=response.data.msg["myblogs"]
              this.total=response.data.msg["nums"]
              setTimeout(() => {
                if (this.total>1){
                  this.disabled=false
                }
                else {
                  this.lazy=false
                }
              }, 200)
              this.$store.commit('setmainloading',false)
            }
          }).catch(error=> {
            console.log(error)
            this.$store.commit('errorMsg',"请求发出错误！请稍后再试")
          })
        }
        else if (this.$route.params.bloglist==="category"){
          this.$http.post("/blog/selectBlogsByCategoryIdAndPageForCommon/"+this.$route.params.bloglist2+"/"+this.pageSize+"/"+page).then(response=>{
            if (response!=null){
              this.myblogs=response.data.msg["myblogs"]
              this.total=response.data.msg["nums"]
              setTimeout(() => {
                if (this.total>1){
                  this.disabled=false
                }
                else {
                  this.lazy=false
                }
                window.document.title = '博客类别: '+this.$store.state.category[this.$route.params.bloglist2]
              }, 200)
              this.$store.commit('setmainloading',false)
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
      this.refresh(this.$store.state.commonCurrentPage)
      setTimeout(() => {
        document.getElementById("myelmain").scrollTop=5
        document.getElementById("myelmain").scrollTop=this.$store.state.height
      }, 200)

    },
    beforeRouteLeave (to, from, next) {
      this.disabled=true
      this.$store.commit('setHeight',document.getElementById("myelmain").scrollTop)
      next()
    },
    watch: {
      '$route.params.bloglist2': function (to, from) {
        this.disabled=true
        this.$store.commit('setCommonCurrentPage',1)
        this.refresh(this.$store.state.commonCurrentPage)
        setTimeout(() => {
          document.documentElement.scrollTop=0
          document.body.scrollTop=0
          document.getElementById("myelmain").scrollTop=5
          document.getElementById("myelmain").scrollTop=0
        }, 200)
      }
    },
  }
</script>

<style scoped>
@media only screen and (max-width: 767px) {
  .maoboli{
    position: relative;
    z-index: 1;
    background-position: center top;
    background-size: cover;
    overflow: hidden;
    padding-top: 15px;
    padding-bottom: 5px;
    border-radius: 20px;
  }
  .maoboli::before{
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
  .blogTitle{
    text-align: center;
    font-size: 30px;
    color: #ae02f8;
    cursor:pointer;
    margin: 0 auto;
    padding: 0 10px;
    width: fit-content;
  }
  .blogCategory{
    color: #fa0e0e;
    width: fit-content;
    margin: 0 auto;
    padding: 5px 10px;
    border-radius: 10px;
    cursor:pointer;
    font-size: 16px;
  }
  .blogCreateGmt{
    width: fit-content;
    margin: 0 auto;
    padding: 5px 0;
    border-radius: 10px;
    cursor:pointer;
    font-size: 14px;
    white-space: nowrap;
  }
  .myimage{
    height: 180px;
  }
  .myi{
    color: black;
  }
}
@media only screen and (min-width: 768px) {
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
    background: linear-gradient(90deg, #070707,#ddf864,#05ddfa,#05ddfa,#ddf864,#070707);
    opacity: 0.4;
  }

  .blogTitle{
    text-align: center;
    font-size: 30px;
    color: #ae02f8;
    cursor:pointer;
    -webkit-transition: all 0.3s ease;
    transition: all 0.3s ease;
    margin: 0 auto;
    padding: 0 10px;
    width: fit-content;
  }
  .blogTitle:hover{
    color: #fff;
    -webkit-animation: Glow 0.3s ease infinite alternate;
    animation: Glow 1s ease infinite alternate;
    background: none;
  }
  @-webkit-keyframes Glow {
    from {
      text-shadow: 0 0 10px #fff,
      0 0 20px #fff,
      0 0 30px #fff,
      0 0 40px #05ddfa,
      0 0 70px #05ddfa,
      0 0 80px #05ddfa,
      0 0 100px #05ddfa,
      0 0 150px #05ddfa;
    }
    to {
      text-shadow: 0 0 5px #fff,
      0 0 10px #fff,
      0 0 15px #fff,
      0 0 20px #05ddfa,
      0 0 35px #05ddfa,
      0 0 40px #05ddfa,
      0 0 50px #05ddfa,
      0 0 75px #05ddfa;
    }
  }
  @keyframes Glow {
    from {
      text-shadow: 0 0 10px #fff,
      0 0 20px #fff,
      0 0 30px #fff,
      0 0 40px #05ddfa,
      0 0 70px #05ddfa,
      0 0 80px #05ddfa,
      0 0 100px #05ddfa,
      0 0 150px #05ddfa;
    }
    to {
      text-shadow: 0 0 5px #fff,
      0 0 10px #fff,
      0 0 15px #fff,
      0 0 20px #05ddfa,
      0 0 35px #05ddfa,
      0 0 40px #05ddfa,
      0 0 50px #05ddfa,
      0 0 75px #05ddfa;
    }
  }
  .blogCategory{
    background: #333331;
    font-weight: bold;
    color: #ffffff;
    width: fit-content;
    margin: 0 auto;
    padding: 5px 10px;
    border-radius: 10px;
    cursor:pointer;
    font-size: 16px;
  }
  .blogCategory:hover{
    background: #fa5979;
    transition: 0.3s;
  }
  .blogCreateGmt{
    font-weight: bold;
    width: fit-content;
    margin: 0 auto;
    padding: 5px 10px;
    border-radius: 10px;
    cursor:default;
    font-size: 16px;
  }
  .myimage{
    height: 640px;
  }
  .myrow{
    display: none;
  }
}
.myimage{
  width: 95%;
  cursor:pointer;
  margin-top: 5px;
  border-radius: 20px;
}
.myi{
  margin-right: 5px;
}
.el-divider--horizontal{
    background: #02f5c4;
  }
</style>
