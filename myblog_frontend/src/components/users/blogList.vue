<template>
  <div style="margin: 0 auto">
    <div v-loading="categoriesLoading" element-loading-text="拼命加载中" element-loading-spinner="el-icon-loading"
         element-loading-background="rgba(0, 0, 0, 0.8)"
         style="margin-bottom: 10px">
      <el-button size="medium" v-for="item in categories" :key="item.categoryId" type="primary"
                 style="font-size: 12px;font-weight: bold;margin-left: 0;margin-right: 10px;margin-bottom: 10px"
                 @click="select(item.categoryId)">{{ item.categoryName }} ({{ item.categoryRank }})
      </el-button>
    </div>
    <el-table v-loading="loading" element-loading-text="拼命加载中" element-loading-spinner="el-icon-loading"
              element-loading-background="rgba(0, 0, 0, 0.8)" border :row-class-name="tableRowClassName" :data="myData">
      <el-table-column :resizable="false" type="index" :index="indexMethod" align="center" label="编号"
                       width="50"></el-table-column>
      <el-table-column show-overflow-tooltip align="center" label="博客标题" v-slot="scope">
        <el-tag effect="dark" style="padding: 0 5px;margin:0 -5px" type="danger">{{ scope.row.blogTitle }}</el-tag>
      </el-table-column>
      <el-table-column align="center" label="封面图片" width="275" v-slot="scope">
        <el-image style="width: 250px;height: 100px" :src="scope.row.blogCoverImage" fit="fill"></el-image>
      </el-table-column>
      <el-table-column sortable align="center" prop="category.categoryName" label="博客类别" v-slot="scope">
        <el-tag effect="dark" style="padding: 0 5px;margin:0 -5px" type="warning">
          {{ scope.row.category.categoryName }}
        </el-tag>
      </el-table-column>
      <el-table-column sortable align="center" prop="createGmt" label="创建时间" width="164" v-slot="scope">
        <el-tag effect="dark" style="padding: 0 5px;margin:0 -5px" type="success">{{ scope.row.createGmt }}</el-tag>
      </el-table-column>
      <el-table-column sortable align="center" prop="updateGmt" label="更新时间" width="164" v-slot="scope">
        <el-tag effect="dark" style="padding: 0 5px;margin:0 -5px" type="success">{{ scope.row.updateGmt }}</el-tag>
      </el-table-column>
      <el-table-column align="center">
        <template slot="header" slot-scope="scope">
          <el-autocomplete
            id="myinput1"
            :minlength="1"
            :maxlength="30"
            v-model="search"
            :fetch-suggestions="querySearchAsync"
            placeholder="请输入搜索内容"
            @select="searchBlog"
            suffix-icon=" el-icon-s-opportunity"
            prefix-icon="el-icon-search">
            <template slot-scope="{ item }">
              <span class="addr">{{ item.value }}</span>
              <i style="float: right;margin-top:10px" class="el-icon-trophy"></i>
            </template>
          </el-autocomplete>
        </template>
        <template slot-scope="scope">
          <el-button
            size="mini"
            :type="scope.row.blogId === ''?'info':'primary'"
            plain
            :disabled="scope.row.blogId === ''"
            @click="showBlog(scope.row)"
          >查看
          </el-button>
          <el-button
            style="margin-top: 10px;margin-left: 0"
            size="mini"
            :type="scope.row.blogId === ''?'info':'success'"
            plain
            :disabled="scope.row.blogId === ''"
            @click="selectBlog(scope.$index, scope.row)"
          >编辑
          </el-button>
          <el-button
            style="margin-top: 10px;margin-left: 0"
            size="mini"
            :type="scope.row.blogId === ''?'info':'danger'"
            plain
            :disabled="scope.row.blogId === ''"
            @click="showDeleteDialog(scope.$index, scope.row)"
          >删除
          </el-button>
          <el-tag
            v-if="scope.row.blogId === ''"
            type="warning"
            effect="dark">
            添加中.....
          </el-tag>
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
    <el-dialog id="eldialog" width="90%" title="修改博客"
               :visible.sync="dialogFormVisible" :close-on-click-modal="false">
      <el-form inline-message :model="formData" :rules="rules" ref="form" @submit.native.prevent>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item prop="blogTitle" class="myitem">
              <el-input v-model="formData.blogTitle" placeholder="博客标题"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item prop="category.categoryId" class="myitem">
              <el-select :loading="selectLoading" element-loading-text="拼命加载中"
                         element-loading-spinner="el-icon-loading"
                         element-loading-background="rgba(0, 0, 0, 0.8)" v-model="formData.category.categoryId"
                         placeholder="博客类别" style="width: 100%">
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
                <el-input :disabled="true" v-model="formData.blogCoverImage" placeholder="博客封面地址"
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
                <el-image style="width: 100%; height: 120px" v-if="formData.blogCoverImage"
                          :src="formData.blogCoverImage" fit="fill"></el-image>
                <i v-else class="el-icon-plus avatar-uploader-icon"></i>
                <input type="file" accept="image/png,image/jpeg" id="file"
                       style="filter:alpha(opacity=0);opacity:0;width: 0;height: 0;" @change="getFile"/>
              </div>
            </el-form-item>
          </el-col>

        </el-row>

        <el-divider></el-divider>

        <el-form-item prop="blogContent" class="myitem">
          <v-md-editor v-model="formData.blogContent" height="740px" :disabled-menus="[]"
                       @upload-image="handleUploadImage"
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
      myData: [],
      formData: {
        blogTitle: '',
        blogCoverImage: '',
        blogContent: '',
        category: {
          categoryId: '',
          categoryName: '',
        },
      },
      options: [],
      search: '',
      loading: true,
      categoriesLoading: true,
      // dialogLoading: true,
      selectLoading: false,
      dialogFormVisible: false,
      currentPage: this.$store.state.currentPage,
      pageSize: 5,
      total: null,
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
      filename: '',
      categories: [],
      categoryId: 0,
    }
  },
  methods: {
    testadd(testData) {
      this.myData.unshift(testData)
      if (this.myData.length > 5) {
        this.myData.length = 5
      }
    },
    select(id) {
      this.categoryId = id
      this.currentPage = 1
      this.$store.commit('setCurrentPage', 1)
      this.refreshDate(this.categoryId, this.currentPage, this.pageSize);
    },
    chooseFile() {
      document.getElementById("file").click()
    },
    async getFile() {
      // console.log(document.getElementById("file").files[0])
      const message = this.$message({
        message: '封面图片上传中',
        iconClass: "el-icon-loading",
        center: true,
        duration: 0,
      });
      await this.getUpLoad(document.getElementById("file").files[0], 0)
      message.close()
    },
    querySearchAsync(queryString, cb) {
      this.$http.post("/blog/common/hotkeys").then(response => {
        if (response != null) {
          this.hotkeys = response.data.data
          cb(queryString ? this.hotkeys.filter(this.createStateFilter(queryString)) : this.hotkeys);
        }
      }).catch(error => {
        console.log(error)
        this.$store.commit('errorMsg', "请求发出错误！请稍后再试")
      })
      cb(queryString ? this.hotkeys.filter(this.createStateFilter(queryString)) : this.hotkeys);
    },
    createStateFilter(queryString) {
      return (input) => {
        return (input.value.toLowerCase().indexOf(queryString.toLowerCase()) === 0);
      };
    },
    searchBlog() {
      if (this.search.length >= 1) {
        window.open("/#/bloglist/search/" + this.search)
      }
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
          this.formData.blogCoverImage = response.data.data;
        }
      }).catch(error => {
        console.log(error)
        this.$store.commit('errorMsg', "请求发出错误！请稍后再试")
      })
    },
    refreshDate(categoryId, pageNow, pageSize) {
      if (this.$route.params.test !== undefined) {
        this.testadd(this.$route.params.test)
      }
      this.$http.post("/blog/admin/selectBlogListByPageForAdmin/" + categoryId + "/" + pageSize + "/" + pageNow).then(response => {
        if (response != null) {
          this.myData = response.data.data;
          this.$http.post("/blog/admin/selectTotalBlogsForAdmin/" + categoryId).then(response => {
            if (response != null) {
              this.total = response.data.data;
              this.loading = false;
            }
          }).catch(error => {
            console.log(error)
            this.$store.commit('errorMsg', "请求发出错误！请稍后再试")
          })
        }
      }).catch(error => {
        console.log(error)
        this.$store.commit('errorMsg', "请求发出错误！请稍后再试")
      })
      this.$http.post("/category/admin/selectCategoryForAdmin/0/0").then(response => {
        if (response != null) {
          this.categories = response.data.data;
          this.categoriesLoading = false;
        }
      }).catch(error => {
        console.log(error)
        this.$store.commit('errorMsg', "请求发出错误！请稍后再试")
      })
    },
    handleCurrentChange(page) {
      this.$store.commit('setCurrentPage', page)
      this.refreshDate(this.categoryId, page, this.pageSize);
    },
    tableRowClassName({row, rowIndex}) {
      if (rowIndex % 2) {
        return 'warning-row';
      } else {
        return 'success-row';
      }
    },
    indexMethod(index) {
      return (this.currentPage - 1) * this.pageSize + index + 1;
    },
    showDeleteDialog(index, row) {
      this.$confirm('此操作将永久删除该条博客, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.deleteBlog(row.blogId, row.category.categoryId);
      }).catch(() => {
        this.$notify({
          title: '取消',
          message: '删除取消',
          type: 'warning',
          duration: 2500
        });
      });
    },
    deleteBlog(blogId, categoryId) {
      this.$http.post("/blog/admin/deleteBlogForAdmin/" + blogId + "/" + categoryId).then(response => {
        if (response != null) {
          this.$notify({
            title: '删除博客',
            message: "删除成功",
            type: 'success',
            duration: 2500
          });
          this.refreshDate(this.categoryId, this.currentPage, this.pageSize);
        }
      }).catch(error => {
        console.log(error)
        this.$store.commit('errorMsg', "请求发出错误！请稍后再试")
      })
    },
    cancel() {
      this.dialogFormVisible = false;
    },
    selectBlog(index, row) {
      this.$http.post("/blog/admin/selectBlogByIdForAdmin/" + row.blogId).then(response => {
        if (response != null) {
          this.formData = response.data.data;
          this.$http.post("/category/admin/selectCategoryForAdmin/0/0").then(response => {
            if (response != null) {
              this.options = response.data.data;
              this.dialogFormVisible = true;
              // this.dialogLoading = false;
              setTimeout(() => {
                document.getElementById("eldialog").scrollTop = 143
              }, 100)
            }
          }).catch(error => {
            console.log(error)
            this.$store.commit('errorMsg', "请求发出错误！请稍后再试")
          })
        }
      }).catch(error => {
        console.log(error)
        this.$store.commit('errorMsg', "请求发出错误！请稍后再试")
      })
    },
    submitForm(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          const loading = this.$loading({
            lock: true,
            text: 'Loading',
            spinner: 'el-icon-loading',
            background: 'rgba(0, 0, 0, 0.7)',
            body: '.el-dialog'
          });
          this.$notify({
            title: '更新博客',
            message: "更新中，请稍等！",
            type: 'warning',
            duration: 1000
          });
          this.$http.post("/blog/admin/updateBlogForAdmin", this.formData).then(response => {
            if (response != null) {
              this.$notify({
                title: '更新博客',
                message: "更新成功",
                type: 'success',
                duration: 2500
              });
              loading.close()
              this.dialogFormVisible = false
              this.refreshDate(this.categoryId, this.currentPage, this.pageSize)

            }
          }).catch(error => {
            console.log(error)
            this.$store.commit('errorMsg', "请求发出错误！请稍后再试")
          })
        } else {
          return false;
        }
      });
    },
    showBlog(row) {
      window.open("/#/bloglist/blog/" + row.blogId)
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
    async handleUploadImage(event, insertImage, files) {
      await this.getUpLoad(files[0], 1)
      insertImage({
        url: this.filename,
        desc: '博客图片',
      });
    },
    async getToken(type) {
      let now = Date.parse(new Date()) / 1000;
      if (this.$store.state.OSS.expire < now + 3 || this.$store.state.OSS.expire === 0 || this.type !== type) {
        this.type = type;
        await this.$http.post("/upload/admin/getToken/" + type).then((response) => {
          if (response != null) {
            this.$store.commit('setOSS', response.data.data)
          }
        }).catch(error => {
          console.log(error)
          this.$store.commit('errorMsg', "请求发出错误！请稍后再试")
        })
      }

    },
    async getUpLoad(file, type) {
      await this.getToken(type)
      const formData = new FormData();
      formData.append('key', this.$store.state.OSS.dir + this.randomName(file.name, 10));
      formData.append('policy', this.$store.state.OSS.policy);
      formData.append('OSSAccessKeyId', this.$store.state.OSS.accessId);
      formData.append('success_action_status', '200');
      formData.append('callback', this.$store.state.OSS.callback);
      formData.append('signature', this.$store.state.OSS.signature);
      formData.append('file', file);
      await this.$http({
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
    const that = this;
    this.refreshDate(this.categoryId, this.currentPage, this.pageSize);
    document.onkeydown = function (e) {
      // 回车提交表单
      // 兼容FF和IE和Opera
      var theEvent = window.event || e;
      var code = theEvent.keyCode || theEvent.which || theEvent.charCode;
      if (document.activeElement.id === "myinput1" && code === 13) {
        that.searchBlog()
        document.getElementById('myinput1').blur();
      }
    }
  },
  beforeRouteLeave(to, from, next) {
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
  .mobile {
    margin-top: 5px;
    padding: 2px 0;
    text-align: center
  }

  .mypagination {
    display: none;
  }
}

@media only screen and (min-width: 768px) {
  .mobile {
    display: none;
  }

  .mypagination {
    margin-top: 5px;
    margin-bottom: -5px;
    text-align: center
  }
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

.el-divider--horizontal {
  margin: 10px 0;
  background: #ef15e4;
}

.myitem {
  margin-bottom: 0;
}

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
</style>

