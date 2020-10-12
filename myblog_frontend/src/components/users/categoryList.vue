<template>
  <div style="margin: 0 auto;">
    <el-table v-loading="loading" element-loading-text="拼命加载中" element-loading-spinner="el-icon-loading" element-loading-background="rgba(0, 0, 0, 0.8)" border :data="mydata.filter(data => !search || data.categoryName.toLowerCase().includes(search.toLowerCase()))" :row-class-name="tableRowClassName">
      <el-table-column :resizable="false" type="index" :index="indexMethod" align="center" label="编号" width="50"></el-table-column>
      <el-table-column :resizable="false" align="center" label="类别ID" width="100"><template slot-scope="scope"><el-tag style="font-size: 15px" effect="dark">{{scope.row.categoryId}}</el-tag></template></el-table-column>
      <el-table-column show-overflow-tooltip align="center" label="类别名称"><template slot-scope="scope"><el-tag effect="dark" type="danger">{{scope.row.categoryName}}</el-tag></template></el-table-column>
      <el-table-column :resizable="false" align="center" label="类别权重" width="100"><template slot-scope="scope"><el-tag effect="dark" type="warning">{{scope.row.categoryRank}}</el-tag></template></el-table-column>
      <el-table-column :resizable="false" sortable align="center" label="创建时间"><template slot-scope="scope"><el-tag effect="dark" type="success">{{scope.row.createGmt}}</el-tag></template></el-table-column>
      <el-table-column :resizable="false" sortable align="center" label="更新时间"><template slot-scope="scope"><el-tag effect="dark" type="success">{{scope.row.updateGmt}}</el-tag></template></el-table-column>
      <el-table-column :resizable="false" align="center">
        <template slot="header" slot-scope="scope">
          <el-input
            size="medium"
            placeholder="输入关键字搜索"/>
        </template>
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="success"
            plain
            @click="selectcategory(scope.$index, scope.row)"
          >编辑</el-button>
          <el-button
            style="margin-top: 10px;margin-left: 0"
            size="mini"
            type="danger"
            plain
            @click="showdeletedialog(scope.$index, scope.row)"
          >删除</el-button>
        </template>

      </el-table-column>
    </el-table>
    <el-row>
      <el-button :disabled="loading" @click="showdialog" type="success" style="float: right;margin-top: 10px;margin-bottom: 0">添加类别</el-button>
    </el-row>

    <el-row>
      <el-col :span="24">
        <el-pagination
          style="margin-top: 20px"
          background
          :hide-on-single-page="true"
          @current-change="handleCurrentChange"
          :current-page.sync="currentPage"
          :page-size="pagesize"
          :total="total"
          layout="prev, pager, next, jumper, total"
        >
        </el-pagination>
      </el-col>
    </el-row>

    <el-dialog :title="this.addOrUpdate?'添加类别':'更新类别'" :visible.sync="dialogFormVisible" :close-on-click-modal="false">
      <el-row>
        <el-col :span="2" >
          <el-tag type="danger" effect="dark"  style="box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.8);margin-left: -13px;height: 40px;font-size: 13px;padding-top: 4px">类别名称</el-tag>
        </el-col>
        <el-col :span="20">
          <el-input v-model="cagetoryname" placeholder="输入类别名称" style="border-radius: 4px;margin-left:10px;width: 95%;box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.8);"></el-input>
        </el-col>
        <el-col :span="2">
          <el-button style="box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.8);" type="success" @click="addOrUpdate?addcategory():updatecategory()">{{this.addOrUpdate?'添加':'修改'}}</el-button>
        </el-col>
      </el-row>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: "categoryList",
  data(){
    return{
      addOrUpdate: true,//true为添加,false为更新
      mydata: [],
      cagetoryname: '',
      categoryId: '',
      search: '',
      loading: true,
      dialogFormVisible: false,
      currentPage: 1,
      pagesize: 11,
      total: null,
    }
  },
  methods: {
    refreshdate(userid,pageNow,pageSize){
      this.$http.post("/category/selectAllCategoryByPage","pageNow="+pageNow+"&pageSize="+pageSize).then(response=>{
        if (response!=null){
          this.mydata=response.data.msg["mycategories"];
          this.total=response.data.msg["totalCategoryNums"];
          this.loading=false;
        }
      }).catch(error=> {
        console.log(error)
        this.$store.commit('errorMsg',"请求发出错误！请稍后再试")
      })
    },
    indexMethod(index) {
      return (1 - 1) * 12 + index + 1;
    },
    tableRowClassName({row, rowIndex}) {
      if (rowIndex % 2 ) {
        return 'warning-row';
      }
      else {
        return 'success-row';
      }
    },
    handleCurrentChange(page){
      this.refreshdate(this.$store.state.myuser.userid,page,this.pagesize);
    },
    addcategory(){
      if (this.cagetoryname===''){
        return this.$store.commit('errorMsg',"请输入类别名称")
      }
      this.$http.post("/category/addCategory","categoryname="+this.cagetoryname).then(response=>{
        if (response!=null){
          this.$notify({
            title: '添加成功',
            message: response.data.msg["add"],
            type: 'success',
            duration: 2500
          });
          this.dialogFormVisible=false
          this.refreshdate(this.$store.state.myuser.userid,this.currentPage,this.pagesize);
          this.cagetoryname=''
        }
      }).catch(error=> {
        console.log(error)
        this.$store.commit('errorMsg',"请求发出错误！请稍后再试")
      })
    },
    updatecategory(){
      if (this.cagetoryname===''){
        return this.$store.commit('errorMsg',"请输入类别名称")
      }
      this.$http.post("/category/updateCategory","categoryname="+this.cagetoryname+"&categoryid="+this.categoryId).then(response=>{
        if (response!=null){
          this.$notify({
            title: '修改成功',
            message: response.data.msg["update"],
            type: 'success',
            duration: 2500
          });
          this.dialogFormVisible=false
          this.refreshdate(this.$store.state.myuser.userid,this.currentPage,this.pagesize);
          this.cagetoryname=''
          this.categoryId=''
        }
      }).catch(error=> {
        console.log(error)
        this.$store.commit('errorMsg',"请求发出错误！请稍后再试")
      })
    },
    selectcategory(index, row) {
      this.cagetoryname=''
      this.categoryId=''
      this.addOrUpdate=false
      this.$http.post("/category/selectCategoryById","categoryid="+row.categoryId).then(response=>{
        if (response!=null){
          this.cagetoryname=response.data.msg["mycategory"].categoryName;
          this.categoryId=response.data.msg["mycategory"].categoryId;
          this.dialogFormVisible=true;
        }
      }).catch(error=> {
        console.log(error)
        this.$store.commit('errorMsg',"请求发出错误！请稍后再试")
      })
    },
    showdialog(){
      this.cagetoryname=''
      this.dialogFormVisible=true
      this.addOrUpdate=true
    },
    showdeletedialog(index, row) {
      this.$confirm('此操作将永久删除该类别, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.deletecategory(row.categoryId);
      }).catch(() => {
        this.$notify({
          title: '取消',
          message: '删除取消',
          type: 'warning',
          duration: 2500
        });
      });
    },
    deletecategory(categoryid) {
      this.$http.post("/category/deleteCategory","categoryid="+categoryid).then(response=>{
        if (response!=null){
          this.$notify({
            title: '删除成功',
            message: response.data.msg["delete"],
            type: 'success',
            duration: 2500
          });
          this.refreshdate(this.$store.state.myuser.userid,this.currentPage,this.pagesize);
        }
      }).catch(error=> {
        console.log(error)
        this.$store.commit('errorMsg',"请求发出错误！请稍后再试")
      })
    },
  },
  created() {
    this.refreshdate(this.$store.state.myuser.userid,this.currentPage,this.pagesize);
  }
}
</script>

<style scoped>
.el-table .warning-row {
  background: #fce3d1;

}

.el-table .success-row {
  background: #e6ffda;
}
</style>
