<template>

  <div id="App">
    <router-view/>
  </div>

</template>

<script>
export default {
  name: 'App',
}
</script>

<style>
.vuepress-markdown-body code {
  font-weight: bold;
}

.vuepress-markdown-body blockquote {
  background-color: #000000a1;
}

.el-pagination__jump {
  color: #ffffff;
  margin-left: 0;
}

.el-pagination__total {
  color: #ffffff;
}

.el-pagination__editor {
  width: 40px !important;
}
</style>
