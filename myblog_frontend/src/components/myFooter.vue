<template>
  <div id="footer">
    <el-link href="http://chardance.cloud" target="_blank" type="danger"><el-tag type="success" effect="dark" style="padding: 0 2px">字符跳动</el-tag></el-link>
    ||
    <el-link icon="http://picture.chardance.cloud/0.png" href="http://www.beian.miit.gov.cn" target="_blank" type="danger">沪ICP备20013409号</el-link>
    <!--            |-->
    <!--            <img src="http://www.noahsark1.vip/x.png"/>-->
    <!--            <el-link href="http://www.beian.gov.cn/portal/registerSystemInfo?recordcode=31011202006825" target="_blank" type="danger">沪公网安备 31011202006825号</el-link>-->
  </div>
</template>

<script>
    export default {
        name: "myFooter"
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
