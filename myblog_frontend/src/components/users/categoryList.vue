<template>
  <div style="margin: 0 auto;">
    <el-table v-loading="loading" element-loading-text="拼命加载中" element-loading-spinner="el-icon-loading" element-loading-background="rgba(0, 0, 0, 0.8)" border :data="myData.filter(data => !search || data.categoryName.toLowerCase().includes(search.toLowerCase()))" :row-class-name="tableRowClassName">
      <el-table-column :resizable="false" type="index" :index="indexMethod" align="center" label="编号" width="50"></el-table-column>
      <el-table-column :resizable="false" align="center" label="类别ID" width="100"><template slot-scope="scope"><el-tag style="font-size: 15px" effect="dark">{{scope.row.categoryId}}</el-tag></template></el-table-column>
      <el-table-column show-overflow-tooltip align="center" label="类别名称"><template slot-scope="scope"><el-tag effect="dark" type="danger">{{scope.row.categoryName}}</el-tag></template></el-table-column>
      <el-table-column :resizable="false" align="center" label="类别权重" width="100"><template slot-scope="scope"><el-tag effect="dark" type="warning">{{scope.row.categoryRank}}</el-tag></template></el-table-column>
      <el-table-column :resizable="false" sortable align="center" label="创建时间"><template slot-scope="scope"><el-tag effect="dark" type="success">{{scope.row.createGmt}}</el-tag></template></el-table-column>
      <el-table-column :resizable="false" sortable align="center" label="更新时间"><template slot-scope="scope"><el-tag effect="dark" type="success">{{scope.row.updateGmt}}</el-tag></template></el-table-column>
      <el-table-column :resizable="false" align="center">
        <template slot="header" slot-scope="scope">
          <span>操作</span>
        </template>
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="success"
            plain
            @click="selectCategory(scope.$index, scope.row)"
          >编辑</el-button>
          <el-button
            style="margin-top: 10px;margin-left: 0"
            size="mini"
            type="danger"
            plain
            @click="showDeleteDialog(scope.$index, scope.row)"
          >删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination
      class="mobilepagination"
      small
      background
      :hide-on-single-page="true"
      @current-change="handleCurrentChange"
      :current-page.sync="currentPage"
      :page-size="pageSize"
      :total="total"
      layout="prev, pager, next, total"
    >
    </el-pagination>
    <el-pagination
      class="mypagination"
      background
      :hide-on-single-page="true"
      @current-change="handleCurrentChange"
      :current-page.sync="currentPage"
      :page-size="pageSize"
      :total="total"
      layout="prev, pager, next, jumper, total"
    >
    </el-pagination>
    <el-row>
      <el-button :disabled="loading" @click="showDialog" type="success" style="float: right;margin-top: 10px;margin-bottom: 0">添加类别</el-button>
    </el-row>

    <el-dialog v-loading="dialogLoading" element-loading-text="拼命加载中" element-loading-spinner="el-icon-loading" element-loading-background="rgba(0, 0, 0, 0.8)" width="80%" :title="this.addOrUpdate?'添加类别':'更新类别'" :visible.sync="dialogFormVisible" :close-on-click-modal="false">
      <div class="pc">
        <el-row style="box-shadow: 0 0 12px 0 rgb(245,155,106);padding:30px 0">
          <el-col :span="4">
            <el-tag type="danger" effect="dark" style="box-shadow: 0 0 12px 0 rgba(0, 0, 0, 0.8);height: 38px;font-size: 13px;padding-top: 4px;float: right">类别名称</el-tag>
          </el-col>
          <el-col :span="16" style="text-align: center">
            <el-input v-model="categoryName" placeholder="输入类别名称" style="border-radius: 4px;box-shadow: 0 0 12px 0 rgba(0, 0, 0, 0.8);width: 80%"></el-input>
          </el-col>
          <el-col :span="4">
            <el-button style="box-shadow: 0 0 12px 0 rgba(0, 0, 0, 0.8);float: left" type="success" @click="addOrUpdate?addCategory():updateCategory()">{{this.addOrUpdate?'添加':'修改'}}</el-button>
          </el-col>
        </el-row>
      </div>
      <div class="mobile">
        <el-row style="text-align: center">
          <el-tag type="danger" effect="dark"  style="box-shadow: 0 0 12px 0 rgba(0, 0, 0, 0.8);width:200px;height: 38px;font-size: 13px;padding-top: 4px">类别名称</el-tag>
        </el-row>
        <el-row style="margin: 30px auto">
          <el-input v-model="categoryName
         " placeholder="输入类别名称" style="border-radius: 4px;box-shadow: 0 0 12px 0 rgba(0, 0, 0, 0.8);"></el-input>
        </el-row>
        <el-row style="text-align: center">
          <el-button style="box-shadow: 0 0 12px 0 rgba(0, 0, 0, 0.8);width: 200px" type="success" @click="addOrUpdate?addCategory():updateCategory()">{{this.addOrUpdate?'添加':'修改'}}</el-button>
        </el-row>
      </div>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: "categoryList",
  data(){
    return{
      addOrUpdate: true,//true为添加,false为更新
      myData: [],
      categoryName: '',
      categoryId: '',
      search: '',
      loading: true,
      dialogFormVisible: false,
      currentPage: 1,
      pageSize: 10,
      total: null,
      dialogLoading: true
    }
  },
  methods: {
    refreshDate(pageNow,pageSize){
      this.$http.post("/category/admin/selectCategoryForAdmin/"+pageSize+"/"+pageNow).then(response=>{
        if (response!=null){
          this.myData=response.data.data;
          this.$http.post("/category/admin/getTotalCategoriesForAdmin/").then(response=>{
            if (response!=null){
              this.total=response.data.data;
              this.loading=false;
            }
          }).catch(error=> {
            console.log(error)
            this.$store.commit('errorMsg',"请求发出错误！请稍后再试")
          })
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
      this.refreshDate(page,this.pageSize);
    },
    addCategory(){
      if (this.categoryName ===''){
        return this.$store.commit('errorMsg',"请输入类别名称")
      }
      this.dialogLoading=true
      this.$http.post("/category/admin/addCategoryForAdmin/"+this.categoryName).then(response=>{
        if (response!=null){
          this.$notify({
            title: '添加类别',
            message: "添加成功",
            type: 'success',
            duration: 2500
          });
          this.dialogFormVisible=false
          this.refreshDate(this.currentPage,this.pageSize);
        }
      }).catch(error=> {
        console.log(error)
        this.$store.commit('errorMsg',"请求发出错误！请稍后再试")
      })
    },
    updateCategory(){
      if (this.categoryName ===''){
        return this.$store.commit('errorMsg',"请输入类别名称")
      }
      this.dialogLoading=true
      this.$http.post("/category/admin/updateCategoryForAdmin/"+this.categoryId+"/"+this.categoryName).then(response=>{
        if (response!=null){
          this.$notify({
            title: '修改类别',
            message: "修改成功",
            type: 'success',
            duration: 2500
          });
          this.dialogFormVisible=false
          this.refreshDate(this.currentPage,this.pageSize);
          this.categoryName =''
          this.categoryId=''
        }
      }).catch(error=> {
        console.log(error)
        this.$store.commit('errorMsg',"请求发出错误！请稍后再试")
      })
    },
    selectCategory(index, row) {
      this.categoryName=row.categoryName;
      this.categoryId=row.categoryId;
      this.addOrUpdate=false;
      this.dialogLoading=false;
      this.dialogFormVisible=true;
    },
    showDialog(){
      this.categoryName =''
      this.dialogFormVisible=true
      this.addOrUpdate=true
    },
    showDeleteDialog(index, row) {
      this.$confirm('此操作将永久删除该类别, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.deleteCategory(row.categoryId);
      }).catch(() => {
        this.$notify({
          title: '取消',
          message: '删除取消',
          type: 'warning',
          duration: 2500
        });
      });
    },
    deleteCategory(categoryId) {
      this.loading=true;
      this.$http.post("/category/admin/deleteCategoryForAdmin/"+categoryId).then(response=>{
        if (response!=null){
          this.$notify({
            title: '删除类别',
            message: "删除成功",
            type: 'success',
            duration: 2500
          });
          this.refreshDate(this.currentPage,this.pageSize);
        }
      }).catch(error=> {
        console.log(error)
        this.$store.commit('errorMsg',"请求发出错误！请稍后再试")
      })
    },
  },
  created() {
    this.refreshDate(this.currentPage,this.pageSize);
  }
}
</script>

<style scoped>

@media only screen and (max-width: 767px) {
  .pc{
    display: none !important;
  }
  .mobilepagination{
    margin-top:5px;
    padding: 2px 0;
    text-align: center
  }
  .mypagination{
    display: none;
  }
}
@media only screen and (min-width: 768px) {
  .mobile{
    display: none !important;
  }
  .mobilepagination{
    display: none;
  }
  .mypagination{
    margin-top: 5px;
    margin-bottom: -5px;
    text-align: center
  }
}
</style>
