<template>
  <div id="footer">
<!--    <el-link href="http://chardance.cloud" target="_blank" type="danger"><el-tag type="success" effect="dark" style="padding: 0 2px">字符跳动</el-tag></el-link>-->
<!--    ||-->
<!--    <el-link icon="http://picture.chardance.cloud/0.png" href="http://www.beian.miit.gov.cn" target="_blank" type="danger">沪ICP备20013409号</el-link>-->
    <el-link style="margin-top: -8px;margin-bottom: 0" href="http://chardance.cloud" target="_blank" type="danger"><el-tag type="success" effect="dark" style="padding: 0 2px">字符跳动</el-tag></el-link>
    <el-link style="margin-top: -8px;margin-bottom: 0;cursor:default;font-family: Arial;font-size: 20px; " disabled>||</el-link>
    <el-link style="margin-top: -8px;margin-bottom: 0" href="http://www.beian.miit.gov.cn" target="_blank" type="danger">沪ICP备20013409号</el-link>
    <el-link style="margin-top: -8px;margin-bottom: 0;cursor:default;font-family: Arial;font-size: 20px; " disabled>||</el-link>
    <el-link class="showTime" style="margin-top: -8px;margin-bottom: 0" href="http://time.tianqi.com/" target="_blank" type="success"></el-link>
  </div>
</template>

<script>
    export default {
        name: "myFooter"
    }
    var t = null;
    t = setTimeout(time, 1000); //開始运行
    function time() {
      clearTimeout(t); //清除定时器
      const dt = new Date();
      var y = dt.getFullYear();
      var mt = dt.getMonth() + 1;
      var day = dt.getDate();
      var h = dt.getHours(); //获取时
      var m = dt.getMinutes(); //获取分
      var s = dt.getSeconds(); //获取秒
      document.querySelector(".showTime").innerHTML =
        y +
        "年" +
        mt +
        "月" +
        day +
        "日" +
        h +
        "时" +
        m +
        "分" +
        s +
        "秒";
      t = setTimeout(time, 1000); //设定定时器，循环运行
    }
</script>

<style scoped>
  #footer {
    color: #fff;
    font-family: Arial;
    font-size: 15px;
    letter-spacing: 1px;
  }
</style>
