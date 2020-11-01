<template>
<div style="margin: 5px auto">
  <div v-for="result in results" :key="result.blogId">
    <div>
      <el-row style="margin-top: 15px">
        <el-col :span="5"  class="myimage">
          <el-image
            :lazy="lazy"
            style="width: 90%; height: 200px;border-radius: 20px"
            :src="result.blogCoverImage"
            fit="fill"></el-image>
        </el-col>
        <el-col :span="18" class="mycontent1">
          <h2 style="background-color: rgba(184,134,11,0.38)"><el-link v-html="result.blogTitle" :href="'/#/index/blog/'+result.blogId" target="_blank" style="font-size: 25px"></el-link></h2>
          <div v-html="result.blogContent" style="text-align:left;overflow: auto;word-break: break-all;margin: 0 10px;font-size: 20px;line-height: 30px;height: 100px"></div>
          <el-row style="margin-bottom: 5px">
            <el-col :span="6">
              <span style="color: grey">Author：{{result.blogUser}}</span>
            </el-col>
            <el-col :span="6">
              <span style="color: grey">Category：{{result.blogCategory}}</span>
            </el-col>
            <el-col :span="6">
              <span style="color: grey">Create：{{result.createGmt}}</span>
            </el-col>
            <el-col :span="6">
              <span style="color: grey">Update：{{result.updateGmt}}</span>
            </el-col>
          </el-row>


        </el-col>
        <el-col :span="24" class="mycontent2">
          <div style="margin: 0 5px">
            <h2 style="background-color: rgba(184,134,11,0.38)"><el-link v-html="result.blogTitle" :href="'/#/index/blog/'+result.blogId" target="_blank" style="font-size: 25px"></el-link></h2>
            <div v-html="result.blogContent" style="text-align:left;overflow: auto;word-break: break-all;margin: 0 10px;font-size: 20px;line-height: 30px;height: 100px"></div>
            <el-row style="margin-bottom: 5px">
              <el-col :span="6">
                <span style="color: grey">Author：{{result.blogUser}}</span>
              </el-col>
              <el-col :span="6">
                <span style="color: grey">Category：{{result.blogCategory}}</span>
              </el-col>
              <el-col :span="6">
                <span style="color: grey">Create：{{result.createGmt}}</span>
              </el-col>
              <el-col :span="6">
                <span style="color: grey">Update：{{result.updateGmt}}</span>
              </el-col>
            </el-row>
          </div>
        </el-col>
      </el-row>
      <el-divider></el-divider>
    </div>
  </div>
  <el-pagination
    style="margin-top: -15px;margin-bottom: 15px;padding: 2px 0;text-align: center"
    background
    :hide-on-single-page="true"
    :current-page.sync="pageNow"
    @current-change="handleCurrentChange"
    :page-size="pageSize"
    :total="total"
    :pager-count="5"
    layout="pager, jumper, total"
  ></el-pagination>
<!--  <el-row style="text-align: center">-->
<!--    <el-pagination-->
<!--      background-->
<!--      :hide-on-single-page="true"-->
<!--      :current-page.sync="pageNow"-->
<!--      @current-change="handleCurrentChange"-->
<!--      :page-size="pageSize"-->
<!--      :total="total"-->
<!--      layout="prev, pager, next, jumper, total"-->
<!--    ></el-pagination>-->
<!--  </el-row>-->
</div>
</template>

<script>
    export default {
        name: "BlogSearchForCommon",
        data(){
          return{
            results:[],
            pageNow:1,
            pageSize:10,
            total:null,
            allresults:[],
            lazy: true,
          }
        },
        methods:{
          handleCurrentChange(page){
            document.documentElement.scrollTop=0
            document.body.scrollTop=0
            document.getElementById("myelmain").scrollTop=1
            this.pageNow=page;
            this.results=this.allresults.slice((this.pageNow-1)*this.pageSize,this.pageNow*this.pageSize);
          },
          refresh(){
            if (this.$store.state.whatsearch!==''){
              window.document.title = '博客搜索: '+this.$store.state.whatsearch
            }
            this.$http.post("/blog/search/"+this.$route.params.whatsearch).then(response=>{
              if (response!=null){
                this.allresults=response.data.msg["results"];
                this.total=response.data.msg["total"]
                this.results=this.allresults.slice((this.pageNow-1)*this.pageSize,this.pageNow*this.pageSize);
                this.$store.commit('setmainloading',false)
                if (this.results.length>4){
                  setTimeout(() => {
                    document.getElementById("myelmain").scrollTop=1
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
        created() {
          this.refresh();
        },
      watch:{
        '$route.params.whatsearch': function (to, from) {
          this.pageNow=1;
          this.$store.commit('setmainloading',true)
          this.allresults.length=0
          this.results.length=0
          this.total=0
          this.refresh();
        }
      }
    }
</script>

<style scoped>
@media only screen and (max-width: 767px) {
  .myimage{
    display: none;
  }
  .mycontent1{
    display: none;
  }
  .mycontent2{
    text-align: center;
    background-color: rgba(239,236,236,0.78);
    border-radius: 20px;
  }
}
@media only screen and (min-width: 768px) {
  .mycontent1{
    text-align: center;
    background-color: rgba(239,236,236,0.78);
    border-radius: 20px;
  }
  .mycontent2{
    display: none;
  }
}
.myimage{
  text-align: center;
}


  .el-divider--horizontal{
    background: #02f5c4;
    margin: 20px 0;
  }
  .el-pagination__jump{
    color: white;
  }
  .el-pagination__total{
    color: white;
  }
</style>
