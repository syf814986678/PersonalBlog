function getHeight(){
  var winHeight=null

// 获取窗口高度
  if (window.innerHeight)
    winHeight = window.innerHeight;
  else if ((document.body) && (document.body.clientHeight))
    winHeight = document.body.clientHeight;
// 通过深入 Document 内部对 body 进行检测，获取窗口大小
  if (document.documentElement && document.documentElement.clientHeight)
  {
    winHeight = document.documentElement.clientHeight;
  }
  return winHeight
}
t = setTimeout(getWidth, 1000); //開始运行

function getWidth(){
  var winWidth=null
// 获取窗口宽度
  if (window.innerWidth)
    winWidth = window.innerWidth;
  else if ((document.body) && (document.body.clientWidth))
    winWidth = document.body.clientWidth;

// 通过深入 Document 内部对 body 进行检测，获取窗口大小
  if (document.documentElement && document.documentElement.clientWidth)
  {
    winWidth = document.documentElement.clientWidth;
  }
  t = setTimeout(getWidth, 1000); //開始运行
  return winWidth
}
export {
  getHeight,
  getWidth
}

