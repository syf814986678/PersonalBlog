<template>
  <div style="margin: 0 auto">
    <el-container class="topcontainer">
      <el-header class="myheader">
        <div class="mytitle"><span class="mytitletext">字符跳动</span></div>
      </el-header>
      <el-container class="mycontainer">
        <el-main :style="{height:this.height+'px'}" class="mymain" id="myelmain"
                 v-loading="this.$store.state.mainLoading" element-loading-text="拼命加载中"
                 element-loading-spinner="el-icon-loading" element-loading-background="rgba(0, 0, 0, 0.8)">
          <transition mode="out-in"
                      enter-active-class="animate__animated animate__bounceInDown animate__faster"
                      leave-active-class="animate__animated animate__bounceOutDown animate__faster">
            <router-view/>
          </transition>
        </el-main>
        <el-aside
          class="myaside"
          :style="{height:this.height+'px'}"
          style="max-width: 235px"
          v-loading="loading" element-loading-text="拼命加载中" element-loading-spinner="el-icon-loading"
          element-loading-background="rgba(0, 0, 0, 0.8)">
          <div class="myautocomplete">
            <el-autocomplete
              id="myinput1"
              :minlength="1"
              :maxlength="30"
              v-model="input"
              :fetch-suggestions="querySearchAsync"
              placeholder="请输入搜索内容"
              @select="handleSelect"
              suffix-icon=" el-icon-s-opportunity"
              prefix-icon="el-icon-search">
              <template slot-scope="{ item }">
                <span>{{ item.value }}</span>
                <i style="float: right;margin-top:10px" class="el-icon-trophy"></i>
              </template>
            </el-autocomplete>
          </div>
          <el-divider></el-divider>
          <div class="mycategory">
            <el-row>
              <el-tag effect="dark" type="danger" style="letter-spacing: 1px;font-size: 14px;margin-top: 10px">
                TOP10类型
              </el-tag>
            </el-row>
            <el-row style="margin-top: 10px">
              <el-button class="mybutton" round v-for="item in categories" :key="item.categoryId" type="success" plain
                         size="mini" style="font-size: 12px;font-weight: bold" @click="select(item.categoryId)">
                {{ item.categoryName }}({{ item.categoryRank }})
              </el-button>
            </el-row>
          </div>
          <el-divider></el-divider>
          <div class="mycategory infodiv">
            <span style="font-size: 18px;font-weight: bold;color: #032c88;float: left">
              查看全部类别
            </span>
            <el-switch
              @change="openDraw"
              v-model="drawerVisible"
              active-color="#13ce66"
              inactive-color="#ff4949">
            </el-switch>
          </div>

          <el-divider></el-divider>
          <div class="mycategory infodiv">
            <span style="display: block" class="info motto-shake"></span>
          </div>
        </el-aside>
      </el-container>
      <div class="mobile" v-loading="loading" element-loading-text="拼命加载中"
           element-loading-spinner="el-icon-loading" element-loading-background="rgba(0, 0, 0, 0.8)">
        <div class="mycategory">
          <el-autocomplete
            id="myinput2"
            style="margin-top: 15px;width: 90%"
            :minlength="1"
            :maxlength="30"
            v-model="input"
            :fetch-suggestions="querySearchAsync"
            placeholder="请输入搜索内容"
            @select="handleSelect"
            suffix-icon=" el-icon-s-opportunity"
            prefix-icon="el-icon-search">
            <template slot-scope="{ item }">
              <span>{{ item.value }}</span>
              <i style="float: right;margin-top:10px" class="el-icon-trophy"></i>
            </template>
          </el-autocomplete>
          <el-row>
            <el-tag effect="dark" type="danger" style="letter-spacing: 1px;font-size: 14px;margin-top: 10px">TOP10类型
            </el-tag>
          </el-row>
          <el-row style="margin-top: 5px;padding-bottom: 10px">
            <el-button class="mybutton" round v-for="item in categories" :key="item.categoryId" type="success" plain
                       size="mini" style="font-size: 12px;font-weight: bold"
                       @click="select(item.categoryId,item.categoryName)">
              {{ item.categoryName }}({{ item.categoryRank }})
            </el-button>
          </el-row>
        </div>
      </div>
      <el-footer class="myfooter">
        <div class="myfooterinfo1">
          <el-link style="margin-top: -16px;margin-bottom: 0" href="https://www.chardance.cloud" target="_blank"
                   type="danger">
            <el-tag type="success" effect="dark" style="padding: 0 2px">字符跳动</el-tag>
          </el-link>
          <el-link style="margin-top: -18px;margin-bottom: 0;cursor:default;font-size: 20px; " disabled>||</el-link>
          <el-link style="margin-top: -16px;margin-bottom: 0" href="https://beian.miit.gov.cn/" target="_blank"
                   type="danger">沪ICP备20013409号-1
          </el-link>
          <el-link style="margin-top: -18px;margin-bottom: 0;cursor:default;font-size: 20px; " disabled>||</el-link>
          <el-link style="margin-top: -16px;margin-bottom: 0;position: relative;margin-left: 20px;"
                   href="http://www.beian.gov.cn/portal/registerSystemInfo?recordcode=31011202013505" target="_blank"
                   type="danger"><img src="https://picture.chardance.cloud/myblog/frontResource/images/beian.png"
                                      style="top: 33%;position: absolute;left: -25px;">沪公网安备 31011202013505号
          </el-link>
          <el-link style="margin-top: -18px;margin-bottom: 0;cursor:default;font-size: 20px; " disabled>||</el-link>
          <el-link class="showTime1" style="margin-top: -16px;margin-bottom: 0" href="http://time.tianqi.com/"
                   target="_blank" type="success"></el-link>
        </div>
        <div class="myfooterinfo2">
          <div>
            <el-link style="margin-top: -16px;margin-bottom: 0" href="https://www.chardance.cloud" target="_blank"
                     type="danger">
              <el-tag type="success" effect="dark" style="padding: 0 2px">字符跳动</el-tag>
            </el-link>
            <el-link style="margin-top: -18px;margin-bottom: 0;cursor:default;font-size: 20px; " disabled>||</el-link>
            <el-link style="margin-top: -16px;margin-bottom: 0" href="https://beian.miit.gov.cn/" target="_blank"
                     type="danger">沪ICP备20013409号-1
            </el-link>
          </div>
          <div style="margin-top: -25px">
            <el-link style="margin-top: -16px;margin-bottom: 0;position: relative;margin-left: 20px;"
                     href="http://www.beian.gov.cn/portal/registerSystemInfo?recordcode=31011202013505" target="_blank"
                     type="danger"><img src="https://picture.chardance.cloud/myblog/frontResource/images/beian.png"
                                        style="top: 33%;position: absolute;left: -25px;">沪公网安备 31011202013505号
            </el-link>
          </div>
          <div style="margin-top: -30px">
            <el-link class="showTime2" style="margin-top: -16px;margin-bottom: 0" href="http://time.tianqi.com/"
                     target="_blank" type="success"></el-link>
          </div>
        </div>
      </el-footer>
    </el-container>
    <el-drawer
      size='14%'
      title="全部类别："
      :visible.sync="drawerVisible"
      direction="rtl">
      <div style="margin-top: 15px;margin-bottom: 15px;margin-left: 20px" v-for="category in allCategories"
           :key="category.categoryId">
        <el-button type="primary" style="font-size: 12px;font-weight: bold" @click="select(category.categoryId)">
          {{ category.categoryName }}({{ category.categoryRank }})
        </el-button>
      </div>
    </el-drawer>
  </div>
</template>
<script>
import {time} from "../../assets/js/showTime";
import {getHeight} from "../../assets/js/calc";
import {getWidth} from "../../assets/js/calc";

export default {
  name: "commonIndex",
  data() {
    return {
      drawerVisible: false,
      categories: [],
      allCategories: [],
      loading: true,
      input: '',
      hotkeys: [],
      height: 0,
      info: ["经汝之手，晓后世之荣耀,吾等世界终将拨乱反正，永垂不朽", "我一个人来到这个世界，给我勇气一个人离开这个世界"]
    }
  },
  methods: {
    openDraw() {
      if (this.drawerVisible) {
        this.$http.post("/category/common/selectAllCategoryForCommon").then(response => {
          if (response != null) {
            this.allCategories = response.data.data
            for (let i = 0; i < this.categories.length; i++) {
              this.$store.commit('setCategory', [this.categories[i].categoryId, this.categories[i].categoryName])
            }
            this.drawerVisible = true
          }
        }).catch(error => {
          console.log(error)
          this.$store.commit('errorMsg', "请求发出错误！请稍后再试")
        })
      }
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
    handleSelect() {
      this.search()
    },
    search() {
      if (this.input.length >= 1 && (this.$route.params.whatsearch !== this.input)) {
        this.$router.push("/bloglist/search/" + this.input)
        this.$store.commit('setMainLoading', true)
        this.$store.commit('setSearch', this.input)
        document.documentElement.scrollTop = 0
        document.body.scrollTop = 0
      }
    },
    select(id) {
      this.drawerVisible = false
      this.$store.commit('setHeight', 0)
      this.$store.commit('setCommonCurrentPage', 1)
      this.$router.push("/bloglist/category/" + id)
      document.documentElement.scrollTop = 0
      document.body.scrollTop = 0
    },
  },
  created() {
    const that = this;
    if (this.$route.params.whatsearch != null) {
      this.input = this.$route.params.whatsearch
    }
    this.$http.post("/category/common/selectTopTenCategoryForCommon").then(response => {
      if (response != null) {
        this.categories = response.data.data
        this.loading = false
        for (let i = 0; i < this.categories.length; i++) {
          this.$store.commit('setCategory', [this.categories[i].categoryId, this.categories[i].categoryName])
        }
        document.onkeydown = function (e) {
          // 回车提交表单
          // 兼容FF和IE和Opera
          var theEvent = window.event || e;
          var code = theEvent.keyCode || theEvent.which || theEvent.charCode;
          if ((document.activeElement.id === "myinput1" || document.activeElement.id === "myinput2") && code === 13) {
            that.search()
            document.getElementById('myinput1').blur();
            document.getElementById('myinput2').blur();
          }
        }
      }
    }).catch(error => {
      console.log(error)
      this.$store.commit('errorMsg', "请求发出错误！请稍后再试")
    })
    if (getWidth() > 1000) {
      this.height = getHeight() - 125
    } else {
      this.height = getHeight() - 60
    }
    window.addEventListener("resize", function () {
      if (getWidth() > 1000) {
        that.height = getHeight() - 125
      } else {
        that.height = getHeight() - 60
      }
    })
    time()
    setTimeout(function () {
      var i = 0;
      new Motto('.info', {
        lyric: that.info[i],
        showUpSpeed: 150,
        callback: function () {
          setTimeout(function () {
            document.getElementsByClassName("info")[0].classList.remove('motto-shake')
          }, 5000)
        }
      })
    }, 1000)


    // window.onload=function(){
    //   var i=0;
    //   new Motto('.info', {
    //     lyric: that.info[i],
    //     showUpSpeed: 150,
    //     callback: function () {
    //       setTimeout(function (){
    //         document.getElementsByClassName("info")[0].classList.remove('motto-shake')
    //       },5000)
    //     }
    //   })
    // };
  },
  beforeRouteLeave(to, from, next) {
    document.onkeydown = undefined
    next()
  },
}
</script>
<style scoped>
.myheader {
  background-color: #ffffff;
  margin-top: -6px;
  text-align: center;
}

.mytitle {
  background-color: #11c69c;
  border-radius: 20px;
  box-shadow: 0 0 30px 0 rgba(0, 0, 0, 0.5);
  margin: 3px -16px;
  padding: 3px 10px;
  text-align: center;
}

.mytitletext {
  font-size: 35px;
  color: #fff;
  display: block;
  text-align: center;
}

.myfooter {
  background-color: #fff;
  text-align: center;
  margin-top: 3px;
}

.myfooterinfo1 {
  background-color: #333;
  border-radius: 20px;
  display: block;
  box-shadow: 0 0 30px 0 rgba(0, 0, 0, 0.8);
  color: #ffffff;
  font-size: 15px;
  letter-spacing: 1px;
  height: 48px;
  text-align: center;
  margin: 3px -20px;
}

.myfooterinfo2 {
  background-color: #333;
  border-radius: 20px;
  display: block;
  box-shadow: 0 0 30px 0 rgba(0, 0, 0, 0.8);
  color: #ffffff;
  font-size: 15px;
  letter-spacing: 1px;
  height: 110px;
  text-align: center;
  margin: 3px -20px;
}

.mymain {
  padding: 0 3px;
}

.myautocomplete {
  box-shadow: 0 0 30px 5px rgba(106, 245, 152, 0.7);
  margin: 10px 10px 0;
  text-align: center;
  background-color: rgba(255, 234, 216, 0.63);
  border-radius: 10px
}

.mycategory {
  box-shadow: 0 0 30px 5px rgba(106, 245, 152, 0.7);
  margin: 0 10px 10px;
  text-align: center;
  background-color: rgba(255, 234, 216, 0.63);
  border-radius: 10px;
}


@media only screen and (max-width: 767px) {
  .topcontainer {
    background-image: url("https://picture.chardance.cloud/myblog/bg/bg4.png");
    background-size: cover;
  }

  .myaside {
    display: none;
  }

  .mobile {
    text-align: center;
    padding-top: 15px;
    border-top: cyan solid 1px;
  }

  .myfooter {
    height: 110px !important;
    line-height: 60px;
  }

  .myfooterinfo1 {
    display: none;
  }

  .showTime1 {
    display: none;
  }
}

@media only screen and (min-width: 768px) {
  .mycontainer {
    background-image: url("https://picture.chardance.cloud/myblog/bg/bg.jpg");
    background-size: cover;
  }

  .myaside {
    text-align: center;
    margin: 0 auto;
    padding: 5px 0;
  }

  .mobile {
    display: none;
  }

  .info {
    font-weight: bold;
    font-size: 20px;
    color: #5d07fc;
  }

  .infodiv {
    padding: 15px;
  }

  .myfooter {
    line-height: 60px;
    height: 48px !important;
  }

  .myfooterinfo2 {
    display: none;
  }

  .showTime2 {
    display: none;
  }
}

.mybutton {
  margin: 4px;
}

.el-divider--horizontal {
  background: cyan;
}

</style>
<style>
.el-drawer {
  background-image: url("https://picture.chardance.cloud/myblog/bg/bg4.png");
  background-size: cover;
  overflow: auto;
}

.el-drawer__header {
  color: #6fffa2;
  font-weight: bold;
  font-size: 25px;
}
</style>
