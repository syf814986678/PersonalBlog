<template>
  <el-form
    v-loading="loading"
    element-loading-text="拼命加载中"
    element-loading-spinner="el-icon-loading"
    element-loading-background="rgba(0, 0, 0, 0.8)"

    inline-message
    :model="form"
    :rules="rules"
    ref="form"
    @submit.native.prevent>

    <el-row :gutter="20">
      <el-col :span="12">
        <el-form-item prop="blogTitle" class="myitem">
          <el-input v-model="form.blogTitle" placeholder="博客标题"></el-input>
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item prop="mycategory.categoryId" class="myitem">
          <el-select :loading="loading" v-model="form.mycategory.categoryId" placeholder="博客类别" style="width: 100%">
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
            <el-input :disabled="true" v-model="form.blogCoverImage" placeholder="博客封面地址" style="margin-top: 15px"></el-input>
          </el-form-item>
        </el-row>
        <el-row style="margin-top: 15px;text-align: center">
          <el-button type="success" @click="randomImage">随机封面</el-button>
        </el-row>
      </el-col>
      <el-col :span="12">
        <el-form-item class="myitem">
          <el-upload
            style="margin-bottom: -13px"
            class="avatar-uploader"
            :disabled="available"
            :headers="headers"
            :on-progress="uploadonprogress"
            :on-success="uploadsuccess"
            :on-error="uploaderror"
            action="/upload/uploadBlogCoverImage"
            :before-upload="beforeUpload"
            :show-file-list="false">
            <el-image style="width: 100%; height: 120px" v-if="form.blogCoverImage" :src="form.blogCoverImage" fit="fill"></el-image>
            <i v-else class="el-icon-plus avatar-uploader-icon"></i>
          </el-upload>
        </el-form-item>
      </el-col>

    </el-row>

    <el-divider></el-divider>

    <el-form-item prop="blogContent" class="myitem">
      <v-md-editor v-model="form.blogContent" height="762px" :disabled-menus="[]" @upload-image="handleUploadImage"
                   :include-level="[1, 6]"
                   left-toolbar="undo redo clear | tip customToolbar h bold italic strikethrough | ul ol table hr | link code"
                   right-toolbar="preview toc sync-scroll fullscreen"
                   :toolbar="toolbar"
                   @copy-code-success="handleCopyCodeSuccess"
      ></v-md-editor>

    </el-form-item>
    <el-form-item class="myitem">
      <el-row style="margin: 5px 0">
        <el-col :offset="12" :span="12">
          <div style="float: right;">
            <el-button :disabled="loading" type="primary" @click="submitForm('form')">立即发布</el-button>
            <el-button type="warning" @click="cancel">清空</el-button>
          </div>
        </el-col>
      </el-row>
    </el-form-item>
  </el-form>
</template>
<script>
  import store from "../../store";
  export default {
    name: "addBlog",
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
        headers:{
          Authorization: 'Bearer ' + this.$store.state.token
        },
        options:[],
        form: {
          blogTitle: '',
          blogCoverImage: '',
          blogContent: '',
          myuser: {
            userId: this.$store.state.myuser.userid,
            userName: this.$store.state.myuser.username,
          },
          mycategory: {
            categoryId: '',
            categoryName: '',
          },
        },
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
        loading: true,
        available: false,
        filename:'',
      }
    },
    methods: {
      handleCopyCodeSuccess(){
        this.$message({
          message: '代码复制成功',
          type: 'success',
          duration: 2000
        });
      },
      async handleUploadImage(event, insertImage, files) {
        // 拿到 files 之后上传到文件服务器，然后向编辑框中插入对应的内容
        let now= Date.parse(new Date()) / 1000;
        if ((this.$store.state.OSS.expire < now + 3) || this.$store.state.OSS.expire===0)
        {
          await this.gettoken();
        }
        var formdata = new FormData();
        formdata.append('key', this.$store.state.OSS.dir+this.randomName(files[0].name,10));
        formdata.append('policy', this.$store.state.OSS.policy);
        formdata.append('OSSAccessKeyId', this.$store.state.OSS.accessid);
        formdata.append('success_action_status', '200');
        formdata.append('callback', this.$store.state.OSS.callback);
        formdata.append('signature', this.$store.state.OSS.signature);
        formdata.append('file', files[0]);
        await this.getupload(formdata)
        // this.$refs.md.$img2Url(pos,this.filename)
        // 此处只做示例
        insertImage({
          url:this.filename,
          desc: '博客图片',
        });
      },
      randomImage(){
        this.$http.post("/upload/RandomBlogCoverImage").then(response=>{
          if (response!=null){
            this.form.blogCoverImage=response.data.msg["blogCoverImage"];
          }
        }).catch(error=> {
          console.log(error)
          this.$store.commit('errorMsg',"请求发出错误！请稍后再试")
        })
      },
      submitForm(formName) {
        this.$refs[formName].validate((valid) => {
          if (valid) {
            this.loading=true
            this.$notify({
              title: '发布中',
              message: "发布中，请稍等！",
              type: 'warning',
              duration: 1000
            });
            this.$http.post("/blog/addBlog",this.form).then(response=>{
              if (response!=null){
                this.$notify({
                  title: '发布成功',
                  message: response.data.msg["add"],
                  type: 'success',
                  duration: 2500
                });
                this.cancel();
                this.$router.push("/admin/showBlogList");
              }
            }).catch(error=> {
              console.log(error)
              this.$store.commit('errorMsg',"请求发出错误！请稍后再试")
            })
          }
          else {
            this.$store.commit('errorMsg',"有非空数据未填写!")
            return false;
          }
        });
      },
      cancel(){
        this.$refs.form.clearValidate()
        this.$refs.form.resetFields()
      },
      beforeUpload(file) {
        const isJPG = file.type === 'image/jpeg';
        const isLt2M = file.size / 1024 / 1024 < 2;

        if (!isJPG) {
          this.$message.error('上传头像图片只能是 JPG 格式!');
        }
        if (!isLt2M) {
          this.$message.error('上传头像图片大小不能超过 2MB!');
        }
        return isJPG && isLt2M;
      },
      uploadonprogress(event){
        this.$message({
          message: '封面图片上传中',
          iconClass: "el-icon-loading",
          center: true,
          duration: 0,
        });
      },
      uploadsuccess(response){
        switch (response.codeState) {
          case 200:this.$message.closeAll();this.form.blogCoverImage=response.msg["blogCoverImage"];this.available=true;break;
          case 701:store.commit('errorMsg',response.msg["tokenError"]);this.$store.commit('logout');break;
          case 999:store.commit('errorMsg',response.msg["exception"]);this.$store.commit('logout');break;
          default:break;
        }
      },
      uploaderror(err){
        this.$store.commit('errorMsg',"请求发出错误！请稍后再试")
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
      async gettoken(){
        await this.$http.get("/upload/getOssToken").then((response) => {
          if (response!=null){
            this.$store.commit('setOSS',response.data)
          }
        }).catch(error=> {
          console.log(error)
          this.$store.commit('errorMsg',"请求发出错误！请稍后再试")
        })
      },
      async getupload(formdata){
        await this.$http({
          url: this.$store.state.OSS.host,
          method: 'post',
          data: formdata,
          headers: {
            'Content-Type': 'multipart/form-data',
          },
        }).then((response) => {
          if (response!=null){
            this.filename=response.data.filename
          }
        }).catch(error=> {
          console.log("上传oss错误")
          console.log(error)
          this.$store.commit('errorMsg',"请求发出错误！请稍后再试")
        })
      },
    },
    created() {
      this.$http.post("/category/selectAllCategoryForBlog").then(response=>{
        if (response!=null){
          this.options=response.data.msg["mycategories"];
          this.loading=false;
        }
      }).catch(error=> {
        console.log(error)
        this.$store.commit('errorMsg',"请求发出错误！请稍后再试")
      })
      this.$http.post("/blog/getTempBlog").then(response=>{
        if (response!=null){
          var myblog=response.data.msg["myblog"];
          if (myblog!=null){
            this.$notify({
              title: '加载博客',
              message: "加载暂存博客成功",
              type: 'success',
              duration: 2500
            });
            if (myblog.blogTitle!==""){
              this.form.blogTitle=myblog.blogTitle
            }
            if (myblog.blogCoverImage!==""){
              this.form.blogCoverImage=myblog.blogCoverImage
            }
            if (myblog.blogContent!==""){
              this.form.blogContent=myblog.blogContent
            }
            if (myblog.mycategory.categoryId!==""){
              this.form.mycategory.categoryId=myblog.mycategory.categoryId
            }
            if (myblog.mycategory.categoryName!==""){
              this.form.mycategory.categoryName=myblog.mycategory.categoryName
            }
          }
        }
      }).catch(error=> {
        console.log(error)
        this.$store.commit('errorMsg',"请求发出错误！请稍后再试")
      })
      this.gettoken();
    },
    beforeRouteLeave (to, from, next) {
      if (this.form.blogContent!==""){
        this.$http.post("/blog/setTempBlog",this.form).then(response=>{
          if (response!=null){
            this.$notify({
              title: '暂存博客',
              message: response.data.msg["setTempBlog"],
              type: 'success',
              duration: 2500
            });
          }
        }).catch(error=> {
          console.log(error)
          this.$store.commit('errorMsg',"请求发出错误！请稍后再试")
        })
      }
      next();
    }
  }
</script>
<style>
  .avatar-uploader .el-upload {
    border: 2px solid #45ef27;
    border-radius: 6px;
    cursor: pointer;
    position: relative;
    overflow: hidden;
    width: 100%;
    height: 120px;
  }
  .avatar-uploader .el-upload:hover {
    border-color: #409EFF;
  }

</style>
<style scoped>
  .v-md-editor__menu-item-红色字体{
    color: #dd0000;
  }
  .v-md-editor__menu-item-蓝色字体{
    color: #0000dd;
  }
  .v-md-editor__menu-item-绿色字体{
    color: #00FF7F;
  }
  .myitem {
    margin-bottom: 0;
  }
  .el-divider--horizontal{
    margin: 10px 0;
    background: #ef15e4;
  }
  .avatar-uploader-icon {
    font-size: 28px;
    color: #1369e3;
    margin-top: 45px;
  }
</style>
