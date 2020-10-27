// let t = setTimeout(getHeight, 1000);
// function getHeight(){
//   clearTimeout(t); //清除定时器
//   var winHeight=null
// // 获取窗口高度
//   if (window.innerHeight){
//     winHeight = window.innerHeight;
//   }
//   else if ((document.body) && (document.body.clientHeight)){
//     winHeight = document.body.clientHeight;
//   }
// // 通过深入 Document 内部对 body 进行检测，获取窗口大小
//   if (document.documentElement && document.documentElement.clientHeight)
//   {
//     winHeight = document.documentElement.clientHeight;
//   }
//   that.height=winHeight-135
//   t = setTimeout(getHeight, 1000); //设定定时器，循环运行
// }
// function getWidth(){
//   var winWidth=null
// // 获取窗口宽度
//   if (window.innerWidth){
//     winWidth = window.innerWidth;
//   }
//   else if ((document.body) && (document.body.clientWidth)){
//     winWidth = document.body.clientWidth;
//   }
// // 通过深入 Document 内部对 body 进行检测，获取窗口大小
//   if (document.documentElement && document.documentElement.clientWidth)
//   {
//     winWidth = document.documentElement.clientWidth;
//   }
//   return winWidth
// }
// export {
//   getHeight,
//   getWidth
// }
//
