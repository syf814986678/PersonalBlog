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
        <el-form-item prop="category.categoryId" class="myitem">
          <el-select :loading="loading" v-model="form.category.categoryId" placeholder="博客类别" style="width: 100%">
            <el-option v-for="option in options" :label="option.categoryName" :value="option.categoryId"
                       :key="option.categoryId"></el-option>
          </el-select>
        </el-form-item>
      </el-col>
    </el-row>

    <el-divider></el-divider>

    <el-row :gutter="20">
      <el-col :span="12">
        <el-row>
          <el-form-item prop="blogCoverImage" class="myitem">
            <el-input :disabled="true" v-model="form.blogCoverImage" placeholder="博客封面地址"
                      style="margin-top: 15px"></el-input>
          </el-form-item>
        </el-row>
        <el-row style="margin-top: 15px;text-align: center">
          <el-button type="success" @click="randomImage">随机封面</el-button>
        </el-row>
      </el-col>
      <el-col :span="12">
        <el-form-item class="myitem">
          <div class="avatar-uploader" @click="chooseFile">
            <el-image style="width: 100%; height: 120px" v-if="form.blogCoverImage" :src="form.blogCoverImage"
                      fit="fill"></el-image>
            <i v-else class="el-icon-plus avatar-uploader-icon"></i>
            <input type="file" accept="image/png,image/jpeg" id="file"
                   style="filter:alpha(opacity=0);opacity:0;width: 0;height: 0;" @change="getFile"/>
          </div>
        </el-form-item>
      </el-col>

    </el-row>

    <el-divider></el-divider>

    <el-form-item prop="blogContent" class="myitem">
      <v-md-editor v-model="form.blogContent" height="762px" :disabled-menus="[]" @upload-image="handleUploadImage"
                   :include-level="[1, 6]"
                   left-toolbar="undo redo clear | tip customToolbar h bold italic strikethrough image| ul ol table hr | link code"
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
      options: [],
      form: {
        blogTitle: '',
        blogCoverImage: '',
        blogContent: '',
        user: {
          userId: this.$store.state.user.userId,
          userName: this.$store.state.user.userName,
        },
        category: {
          categoryId: '',
          categoryName: '',
        },
      },
      rules: {
        blogTitle: [
          {required: true, message: '请输入博客标题', trigger: 'blur'},
        ],
        category: {
          categoryId: [
            {required: true, message: '请选择博客类型', trigger: 'change'}
          ],
        },
        blogCoverImage: [
          {required: true, message: '请上传博客封面图片', trigger: 'change'},
        ],
        blogContent: [
          {required: true, message: '请输入博客内容', trigger: 'change'}
        ],
      },
      loading: true,
      filename: '',
      type: -1,
    }
  },
  methods: {
    chooseFile() {
      document.getElementById("file").click()
    },
    getFile() {
      // console.log(document.getElementById("file").files[0])
      const message = this.$message({
        message: '封面图片上传中',
        iconClass: "el-icon-loading",
        center: true,
        duration: 0,
      });
      this.getUpLoad(document.getElementById("file").files[0], 0)
      message.close()
    },
    handleCopyCodeSuccess() {
      this.$message({
        message: '代码复制成功',
        type: 'success',
        duration: 2000
      });
    },
    randomImage() {
      this.$http.post("/upload/admin/randomBlogCoverImage").then(response => {
        if (response != null) {
          this.form.blogCoverImage = response.data.data;
        }
      }).catch(error => {
        console.log(error)
        this.$store.commit('errorMsg', "请求发出错误！请稍后再试")
      })
    },
    submitForm(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          this.loading=true
          this.$notify({
            title: '发布博客',
            message: "发布中，请稍等！",
            type: 'warning',
            duration: 1000
          });
          this.$http.post("/blog/admin/addBlogForAdmin",this.form).then(response=>{
            if (response!=null){
              this.$notify({
                title: '发布博客',
                message: "发布成功",
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
    cancel() {
      this.$refs.form.clearValidate()
      this.$refs.form.resetFields()
    },
    randomName(filename, len) {
      len = len || 32;
      const chars = 'ABCDEFGHJKMNPQRSTWXYZabcdefhijkmnprstwxyz2345678';
      const maxPos = chars.length;
      let pwd = '';
      for (let i = 0; i < len; i++) {
        pwd += chars.charAt(Math.floor(Math.random() * maxPos));
      }
      return pwd + filename.substring(filename.lastIndexOf("."))
    },
    handleUploadImage(event, insertImage, files) {
      this.getUpLoad(files[0],1)
      insertImage({
        url:this.filename,
        desc: '博客图片',
      });
    },
    getToken(type) {
      let now = Date.parse(new Date()) / 1000;
      if (this.$store.state.OSS.expire < now + 3 || this.$store.state.OSS.expire === 0 || this.type !== type) {
        this.type = type;
        this.$http.post("/upload/admin/getToken/" + type).then((response) => {
          if (response != null) {
            this.$store.commit('setOSS', response.data.data)
          }
        }).catch(error => {
          console.log(error)
          this.$store.commit('errorMsg', "请求发出错误！请稍后再试")
        })
      }

    },
    getUpLoad(file, type) {
      this.getToken(type)
      const formData = new FormData();
      formData.append('key', this.$store.state.OSS.dir + this.randomName(file.name, 10));
      formData.append('policy', this.$store.state.OSS.policy);
      formData.append('OSSAccessKeyId', this.$store.state.OSS.accessId);
      formData.append('success_action_status', '200');
      formData.append('callback', this.$store.state.OSS.callback);
      formData.append('signature', this.$store.state.OSS.signature);
      formData.append('file', file);
      this.$http({
        url: this.$store.state.OSS.host,
        method: 'post',
        data: formData,
        headers: {
          'Content-Type': 'multipart/form-data',
        },
      }).then((response) => {
        if (response != null) {
          if (type === 0) {
            this.form.blogCoverImage = response.data.data["filename"]
          } else if (type === 1) {
            this.filename = response.data.data["filename"]
          }
        }
      }).catch(error => {
        console.log(error)
        this.$store.commit('errorMsg', "请求发出错误！请稍后再试")
      })
    },
  },
  created() {
    this.$http.post("/category/admin/selectCategoryForAdmin/0/0").then(response => {
      if (response != null) {
        this.options = response.data.data;
        this.loading = false;
      }
    }).catch(error => {
      console.log(error)
      this.$store.commit('errorMsg', "请求发出错误！请稍后再试")
    })
    this.$http.post("/blog/admin/getTempBlogForAdmin").then(response=>{
      if (response!=null){
        if (response.data.data!==null){
          this.$notify({
            title: '加载博客',
            message: "加载暂存博客成功",
            type: 'success',
            duration: 2500
          });
          if (response.data.data.blogTitle!==""){
            this.form.blogTitle=response.data.data.blogTitle
          }
          if (response.data.data.blogCoverImage!==""){
            this.form.blogCoverImage=response.data.data.blogCoverImage
          }
          if (response.data.data.blogContent!==""){
            this.form.blogContent=response.data.data.blogContent
          }
          if (response.data.data.category.categoryId!==""){
            this.form.category.categoryId=response.data.data.category.categoryId
          }
          if (response.data.data.category.categoryName!==""){
            this.form.category.categoryName=response.data.data.category.categoryName
          }
        }
      }
    }).catch(error=> {
      console.log(error)
      this.$store.commit('errorMsg',"请求发出错误！请稍后再试")
    })
  },
  beforeRouteLeave (to, from, next) {
    if (this.form.blogContent!==""){
      this.$http.post("/blog/admin/setTempBlogForAdmin",this.form).then(response=>{
        if (response!=null){
          this.$notify({
            title: '暂存博客',
            message: "暂存博客成功",
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
<style scoped>
.avatar-uploader {
  border: 2px solid #fd1e01;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  width: 99%;
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

.v-md-editor__menu-item-红色字体 {
  color: #dd0000;
}

.v-md-editor__menu-item-蓝色字体 {
  color: #0000dd;
}

.v-md-editor__menu-item-绿色字体 {
  color: #00FF7F;
}

.myitem {
  margin-bottom: 0;
}

.el-divider--horizontal {
  margin: 10px 0;
  background: #ef15e4;
}

</style>
