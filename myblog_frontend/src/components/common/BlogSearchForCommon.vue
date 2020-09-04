<template>
<div style="margin: 5px auto"  v-loading="loading" element-loading-text="拼命加载中" element-loading-spinner="el-icon-loading" element-loading-background="rgba(0, 0, 0, 0.8)">
  <div v-for="result in results" :key="result.blogId">
    <div>
      <el-row>
        <el-col :span="5" style="text-align: center">
          <el-image
            :lazy="lazy"
            style="width: 90%; height: 200px;"
            :src="result.blogCoverImage"
            fit="fill"></el-image>
        </el-col>
        <el-col :span="18" style="text-align: center;background-color: rgba(239,236,236,0.78)">
          <h2 v-html="result.blogTitle" @click="open(result.blogId)" style="cursor:pointer;background-color: rgba(184,134,11,0.38)"></h2>
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
      </el-row>
      <el-divider></el-divider>
    </div>
  </div>
  <el-col style="margin-top: 5px;margin-bottom: 5px;text-align: center">
    <el-pagination
      background
      :hide-on-single-page="true"
      :current-page.sync="pageNow"
      @current-change="handleCurrentChange"
      :page-size="pageSize"
      :total="total"
      layout="prev, pager, next, jumper, total"
    ></el-pagination>
  </el-col>

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
            loading:true,
          }
        },
        methods:{
          open(id){
            window.open("/#/index/blog/"+id)
          },
          handleCurrentChange(page){
            document.getElementById("myelmain").scrollTop=1
            document.documentElement.scrollTop=0
            this.pageNow=page;
            this.results=this.allresults.slice((this.pageNow-1)*this.pageSize,this.pageNow*this.pageSize);
          },
          refresh(){
            this.$http.post("/blog/search/"+this.$route.params.whatsearch).then(response=>{
              if (response!=null){
                this.allresults=response.data.msg["results"];
                this.total=response.data.msg["total"]
                this.results=this.allresults.slice((this.pageNow-1)*this.pageSize,this.pageNow*this.pageSize);
                this.loading=false
                if (this.results.length>4){
                  setTimeout(() => {
                    document.getElementById("myelmain").scrollTop=1
                  }, 500)
                }
                else {
                  this.lazy=false
                }
                document.documentElement.scrollTop=0
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
        '$route': function (to, from) {
          this.pageNow=1;
          this.loading=true
          this.refresh();
        }
      }
    }
</script>

<style scoped>
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
