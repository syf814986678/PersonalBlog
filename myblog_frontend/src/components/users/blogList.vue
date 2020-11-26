<template>
  <div style="margin: 0 auto">
    <el-table v-loading="loading" element-loading-text="拼命加载中" element-loading-spinner="el-icon-loading" element-loading-background="rgba(0, 0, 0, 0.8)" border :row-class-name="tableRowClassName" :data="mydata">
      <el-table-column :resizable="false" type="index" :index="indexMethod" align="center" label="编号" width="50"></el-table-column>
      <el-table-column show-overflow-tooltip align="center" label="博客标题" v-slot="scope"><el-tag effect="dark" style="padding: 0 5px;margin:0 -5px"  type="danger">{{scope.row.blogTitle}}</el-tag></el-table-column>
      <el-table-column align="center" label="封面图片" width="275" v-slot="scope"><el-image style="width: 250px;height: 100px" :src="scope.row.blogCoverImage" fit="fill"></el-image></el-table-column>
      <el-table-column sortable align="center" prop="mycategory.categoryName" label="博客类别" v-slot="scope"><el-tag effect="dark" style="padding: 0 5px;margin:0 -5px" type="warning">{{scope.row.mycategory.categoryName}}</el-tag></el-table-column>
      <el-table-column align="center" label="类别权重" width="78" v-slot="scope"><el-tag effect="dark" type="warning">{{scope.row.mycategory.categoryRank}}</el-tag></el-table-column>
      <el-table-column sortable align="center" prop="createGmt" label="创建时间" width="150" v-slot="scope"><el-tag effect="dark" style="padding: 0 5px;margin:0 -5px" type="success">{{scope.row.createGmt}}</el-tag></el-table-column>
      <el-table-column sortable align="center" prop="updateGmt" label="更新时间" width="150" v-slot="scope"><el-tag effect="dark" style="padding: 0 5px;margin:0 -5px" type="success">{{scope.row.updateGmt}}</el-tag></el-table-column>
      <el-table-column align="center">
        <template slot="header" slot-scope="scope">
          <el-autocomplete
            id="myinput1"
            :minlength="1"
            :maxlength="30"
            v-model="search"
            :fetch-suggestions="querySearchAsync"
            placeholder="请输入搜索内容"
            @select="handleSelect"
            suffix-icon=" el-icon-s-opportunity"
            prefix-icon="el-icon-search">
            <template slot-scope="{ item }">
              <span class="addr">{{ item.value }}</span>
              <i style="float: right;margin-top:10px" class="el-icon-trophy"></i>
            </template>
          </el-autocomplete>
        </template>
        <template slot-scope="scope" >
          <el-button
            size="mini"
            type="primary"
            plain
            @click="showBlog(scope.row)"
          >查看</el-button>
          <el-button
            style="margin-top: 10px;margin-left: 0"
            size="mini"
            type="success"
            plain
            @click="selectblog(scope.$index, scope.row)"
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
    <el-pagination
      class="mobile"
      small
      background
      :hide-on-single-page="true"
      @current-change="handleCurrentChange"
      :current-page.sync="currentPage"
      :page-size="pagesize"
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
      :page-size="pagesize"
      :total="total"
      layout="prev, pager, next, jumper, total"
    >
    </el-pagination>
    <el-dialog v-loading="dialogloading" element-loading-text="拼命加载中" element-loading-spinner="el-icon-loading" element-loading-background="rgba(0, 0, 0, 0.8)" id="eldialog" width="90%" title="修改博客" :visible.sync="dialogFormVisible" :close-on-click-modal="false">
      <el-form inline-message :model="formdata" :rules="rules" ref="form" @submit.native.prevent>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item prop="blogTitle" class="myitem">
              <el-input v-model="formdata.blogTitle" placeholder="博客标题"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item prop="mycategory.categoryId" class="myitem">
              <el-select :loading="selectloading" element-loading-text="拼命加载中" element-loading-spinner="el-icon-loading" element-loading-background="rgba(0, 0, 0, 0.8)" v-model="formdata.mycategory.categoryId"  placeholder="博客类别" style="width: 100%">
                <el-option v-for="option in options" :label="option.categoryName" :value="option.categoryId" :key="option.categoryId"></el-option>
              </el-select>
            </el-form-item>
          </el-col>

        </el-row>

        <el-divider></el-divider>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-row>
              <el-form-item prop="blogCoverImage" class="myitem">
                <el-input :disabled="true" v-model="formdata.blogCoverImage" placeholder="博客封面地址" style="margin-top: 15px"></el-input>
              </el-form-item>
            </el-row>
            <el-row style="margin-top: 15px;text-align: center">
              <el-button type="success" @click="randomImage">随机封面</el-button>
            </el-row>
          </el-col>
          <el-col :span="12">
            <el-form-item class="myitem">
              <div class="avatar-uploader" @click="choosefile">
                <el-image style="width: 100%; height: 120px" v-if="formdata.blogCoverImage" :src="formdata.blogCoverImage" fit="fill"></el-image>
                <i v-else class="el-icon-plus avatar-uploader-icon"></i>
                <input type="file" accept="image/png,image/jpeg" id="file" style="filter:alpha(opacity=0);opacity:0;width: 0;height: 0;" @change="getfile"/>
              </div>
            </el-form-item>
          </el-col>

        </el-row>

        <el-divider></el-divider>

        <el-form-item prop="blogContent" class="myitem">
          <v-md-editor v-model="formdata.blogContent" height="740px" :disabled-menus="[]" @upload-image="handleUploadImage"
                       :include-level="[1, 6]"
                       left-toolbar="undo redo clear | tip customToolbar h bold italic strikethrough image| ul ol table hr | link code"
                       right-toolbar="preview toc sync-scroll fullscreen"
                       :toolbar="toolbar"
                       @copy-code-success="handleCopyCodeSuccess"
          ></v-md-editor>
        </el-form-item>
        <el-form-item class="myitem">
          <el-row style="margin-top: 10px">
            <el-col :offset="12" :span="12">
              <div style="float: right;">
                <el-button type="success" plain @click="submitForm('form')">立即发布</el-button>
                <el-button type="danger" plain @click="cancel">取消</el-button>
              </div>
            </el-col>
          </el-row>
        </el-form-item>
      </el-form>
    </el-dialog>

  </div>
</template>
<script>
export default {
  name: "blogList",
  data() {
    return {
      toolbar: {
        customToolbar: {
          icon: 'v-md-icon-tip',
          title: '插入彩色字体',
          menus: [
            {
              name: '红色字体',
              text: "红色字体",
              action(editor) {
                editor.insert(function (selected) {
                  const prefix = '<font color="#dd0000">';
                  const suffix = '</font>';
                  const placeholder = '请输入文本';
                  const content = selected || placeholder;
                  return {
                    text: `${prefix}${content}${suffix}`,
                    selected: content,
                  };
                });
              }
            },
            {
              name: '蓝色字体',
              text: "蓝色字体",
              action(editor) {
                editor.insert(function (selected) {
                  const prefix = '<font color="#0000dd">';
                  const suffix = '</font>';
                  const placeholder = '请输入文本';
                  const content = selected || placeholder;
                  return {
                    text: `${prefix}${content}${suffix}`,
                    selected: content,
                  };
                });
              }
            },
            {
              name: '绿色字体',
              text: "绿色字体",
              action(editor) {
                editor.insert(function (selected) {
                  const prefix = '<font color="#00FF7F">';
                  const suffix = '</font>';
                  const placeholder = '请输入文本';
                  const content = selected || placeholder;
                  return {
                    text: `${prefix}${content}${suffix}`,
                    selected: content,
                  };
                });
              }
            },
          ],
        },
      },
      mydata: [],
      formdata: {
        blogTitle: '',
        blogCoverImage: '',
        blogContent: '',
        mycategory: {
          categoryId: '',
          categoryName: '',
        },
      },
      options: [],
      search: '',
      loading: true,
      dialogloading: true,
      selectloading: false,
      dialogFormVisible: false,
      currentPage: this.$store.state.currentPage,
      pagesize: 5,
      total: null,
      rules: {
        blogTitle: [
          {required: true, message: '请输入博客标题', trigger: 'blur' },
        ],
        mycategory: {
          categoryId: [
            { required: true, message: '请选择博客类型', trigger: 'change' }
          ],
        },
        blogCoverImage: [
          {required: true, message: '请上传博客封面图片', trigger: 'change' },
        ],
        blogContent: [
          { required: true, message: '请输入博客内容', trigger: 'change' }
        ],
      },
      filename:'',
    }
  },
  methods:{
    choosefile(){
      document.getElementById("file").click()
    },
    async getfile(){
      // console.log(document.getElementById("file").files[0])
      const mymessage = this.$message({
        message: '封面图片上传中',
        iconClass: "el-icon-loading",
        center: true,
        duration: 0,
      });
      // let now= Date.parse(new Date()) / 1000;
      // if ((this.$store.state.OSS.expire < now + 3) || this.$store.state.OSS.expire===0)
      // {
      //   await this.gettoken(0);
      // }
      await this.getupload(document.getElementById("file").files[0],0)
      mymessage.close()
    },
    querySearchAsync(queryString,cb){
      this.$http.post("/blog/hotkeys").then(response=>{
        if (response!=null){
          this.hotkeys=response.data.msg["hotkeys"]
          cb(queryString ? this.hotkeys.filter(this.createStateFilter(queryString)) : this.hotkeys);
        }
      }).catch(error=> {
        console.log(error)
        this.$store.commit('errorMsg',"请求发出错误！请稍后再试")
      })
      cb(queryString ? this.hotkeys.filter(this.createStateFilter(queryString)) : this.hotkeys);
    },
    createStateFilter(queryString) {
      return (input) => {
        return (input.value.toLowerCase().indexOf(queryString.toLowerCase()) === 0);
      };
    },
    handleSelect(){
      this.searchblog()
    },
    searchblog(){
      if (this.search.length>=1){
        window.open("/#/index/search/"+this.search)
      }
    },
    handleCopyCodeSuccess(){
      this.$message({
        message: '代码复制成功',
        type: 'success',
        duration: 2000
      });
    },
    randomImage(){
      this.$http.post("/upload/randomBlogCoverImage").then(response=>{
        if (response!=null){
          this.formdata.blogCoverImage=response.data.msg["blogCoverImage"];
        }
      }).catch(error=> {
        console.log(error)
        this.$store.commit('errorMsg',"请求发出错误！请稍后再试")
      })
    },
    refreshdate(pageNow,pageSize){
      this.$http.post("/blog/selectBlogsByPage/"+pageSize+"/"+pageNow).then(response=>{
        if (response!=null){
          this.mydata=response.data.msg["myblogs"];
          this.total=response.data.msg["totalBlogNums"];
          this.loading=false;
        }
      }).catch(error=> {
        console.log(error)
        this.$store.commit('errorMsg',"请求发出错误！请稍后再试")
      })
    },
    handleCurrentChange(page){
      this.$store.commit('setCurrentPage',page)
      this.refreshdate(page,this.pagesize);
    },
    tableRowClassName({row, rowIndex}) {
      if (rowIndex % 2 ) {
        return 'warning-row';
      }
      else {
        return 'success-row';
      }
    },
    indexMethod(index) {
      return (this.currentPage - 1) * this.pagesize + index + 1;
    },
    showdeletedialog(index, row) {
      this.$confirm('此操作将永久删除该条博客, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.deleteblog(row.blogId,row.mycategory.categoryId);
      }).catch(() => {
        this.$notify({
          title: '取消',
          message: '删除取消',
          type: 'warning',
          duration: 2500
        });
      });
    },
    deleteblog(blogid,categoryid) {
      this.$http.post("/blog/deleteBlog/"+blogid+"/"+categoryid).then(response=>{
        if (response!=null){
          this.$notify({
            title: '删除成功',
            message: response.data.msg["delete"],
            type: 'success',
            duration: 2500
          });
          this.refreshdate(this.currentPage,this.pagesize);
        }
      }).catch(error=> {
        console.log(error)
        this.$store.commit('errorMsg',"请求发出错误！请稍后再试")
      })
    },
    cancel(){
      this.dialogFormVisible=false;
    },
    selectblog(index, row) {
      this.$http.post("/blog/selectBlogById/"+row.blogId).then(response=>{
        if (response!=null){
          this.formdata=response.data.msg["myblog"];
          this.$http.post("/category/selectAllBlogCategory").then(response=>{
            if (response!=null){
              this.options=response.data.msg["mycategories"];
              this.dialogFormVisible=true;
              this.dialogloading=false;
              setTimeout(() => {
                document.getElementById("eldialog").scrollTop=143
              }, 100)
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
    submitForm(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          this.dialogloading=true;
          this.$notify({
            title: '更新中',
            message: "更新中，请稍等！",
            type: 'warning',
            duration: 1000
          });
          this.$http.post("/blog/updateBlog",this.formdata).then(response=>{
            if (response!=null){
              this.$notify({
                title: '更新成功',
                message: response.data.msg["update"],
                type: 'success',
                duration: 2500
              });
              this.refreshdate(this.currentPage,this.pagesize)
              this.dialogFormVisible=false
            }
          }).catch(error=> {
            console.log(error)
            this.$store.commit('errorMsg',"请求发出错误！请稍后再试")
          })
        }
        else {
          return false;
        }
      });
    },
    showBlog(row){
      window.open("/#/index/blog/"+row.blogId)
    },
    randomName(filename,len) {
      len = len || 32;
      var chars = 'ABCDEFGHJKMNPQRSTWXYZabcdefhijkmnprstwxyz2345678';
      var maxPos = chars.length;
      var pwd = '';
      for (var i = 0; i < len; i++) {
        pwd += chars.charAt(Math.floor(Math.random() * maxPos));
      }
      return pwd+filename.substring(filename.lastIndexOf("."))
    },
    async handleUploadImage(event, insertImage, files) {
      // 拿到 files 之后上传到文件服务器，然后向编辑框中插入对应的内容
      // let now= Date.parse(new Date()) / 1000;
      // if ((this.$store.state.OSS.expire < now + 3) || this.$store.state.OSS.expire===0)
      // {
      //   await this.gettoken(1);
      // }
      await this.getupload(files[0],1)
      insertImage({
        url:this.filename,
        desc: '博客图片',
      });
    },
    async gettoken(type){
      await this.$http.get("/upload/getOssToken/"+type).then((response) => {
        if (response!=null){
          this.$store.commit('setOSS',response.data)
        }
      }).catch(error=> {
        console.log(error)
        this.$store.commit('errorMsg',"请求发出错误！请稍后再试")
      })
    },
    async getupload(file,type){
      await this.gettoken(type)
      var formdata = new FormData();
      formdata.append('key', this.$store.state.OSS.dir+this.randomName(file.name,10));
      formdata.append('policy', this.$store.state.OSS.policy);
      formdata.append('OSSAccessKeyId', this.$store.state.OSS.accessid);
      formdata.append('success_action_status', '200');
      formdata.append('callback', this.$store.state.OSS.callback);
      formdata.append('signature', this.$store.state.OSS.signature);
      formdata.append('file', file);
      await this.$http({
        url: this.$store.state.OSS.host,
        method: 'post',
        data: formdata,
        headers: {
          'Content-Type': 'multipart/form-data',
        },
      }).then((response) => {
        if (response!=null){
          if (type===0){
            this.formdata.blogCoverImage=response.data.filename
          }
          else if (type===1) {
            this.filename=response.data.filename
          }
        }
      }).catch(error=> {
        console.log(error)
        this.$store.commit('errorMsg',"请求发出错误！请稍后再试")
      })
    },
  },
  created() {
    const that = this;
    this.refreshdate(this.currentPage,this.pagesize);
    document.onkeydown = function (e) {
      // 回车提交表单
      // 兼容FF和IE和Opera
      var theEvent = window.event || e;
      var code = theEvent.keyCode || theEvent.which || theEvent.charCode;
      if (document.activeElement.id==="myinput1"&& code === 13) {
        that.searchblog()
        document.getElementById('myinput1').blur();
      }
    }
  },
  beforeRouteLeave (to, from, next) {
    document.onkeydown = undefined
    next()
  },
}
</script>
<style>
.el-table .warning-row {
  background: #d1fcf8;
}
.el-table .success-row {
  background: #e4daff;
}
</style>
<style scoped>
@media only screen and (max-width: 767px) {
  .mobile{
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
    display: none;
  }
  .mypagination{
    margin-top: 5px;
    margin-bottom: -5px;
    text-align: center
  }
}
.v-md-editor__menu-item-红色字体{
  color: #dd0000;
}
.v-md-editor__menu-item-蓝色字体{
  color: #0000dd;
}
.v-md-editor__menu-item-绿色字体{
  color: #00FF7F;
}
.el-divider--horizontal{
  margin: 10px 0;
  background: #ef15e4;
}
.myitem {
  margin-bottom: 0;
}
.avatar-uploader{
  border: 2px solid #fd1e01;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  width: 100%;
  height: 120px;
}
.avatar-uploader:hover {
  border-color: #45ef27;
}
.avatar-uploader-icon {
  font-size: 28px;
  color: #45ef27;
  position: absolute;
  top: 40%;
  left: 50%;
  right: 50%;
  bottom: 50%;
}
</style>

