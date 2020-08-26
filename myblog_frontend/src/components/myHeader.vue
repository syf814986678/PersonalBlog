<template>
  <div>
    <div class="my-title">
      字符跳动
    </div>
    <div class="my-information">
      <el-avatar
        v-popover:popover
        shape="square"
        :size="40"
        fit="fill"
        :src="url">
      </el-avatar>
      <el-popover
        ref="popover"
        width="160"
        v-model="visible">
        <el-row>
          <el-col :span="24">
            用户id：<el-tag style="width: 100px;text-align: center;font-size: medium" effect="dark" type="success">{{this.$store.state.myuser.userid}}</el-tag>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            用户名：<el-tag style="width: 100px;text-align: center;margin-top: 5px;font-size: medium" effect="dark" type="success">{{this.$store.state.myuser.username}}</el-tag>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-button @click="logout" style="width: 155px;height: 25px;text-align: center;margin-top: 5px;line-height: 0px" type="danger" plain>注销</el-button>
          </el-col>
        </el-row>
      </el-popover>
    </div>
  </div>
</template>

<script>
    export default {
        name: "myHeader",
        data(){
          return{
            visible: false,
            url: 'https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1593937482731&di=bece2a0fc9049db36a1cb5324a016222&imgtype=0&src=http%3A%2F%2Fdmimg.5054399.com%2Fallimg%2Fpkm%2Fpk%2F13.jpg'
          }
        },
        methods:{
          logout(){
            this.$store.commit('logout')
          },
        },
    }
</script>

<style scoped>
  .my-title{
    /*浅色投影*/
    font-size: xx-large;
    box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.5);
    margin-top: 3px;
    padding: 5px 10px;
    float: left;
    font-family: Arial;
    color: #fff;
    letter-spacing: 1px;
  }
  .my-information{
    /*浅色投影*/
    margin-top: 10px;
    float: right;
  }
</style>
