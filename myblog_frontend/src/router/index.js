import Vue from 'vue'
import Router from 'vue-router'
import login from "../components/login";
import Index from "../components/users/Index";
import addBlog from "../components/users/addBlog";
import blogList from "../components/users/blogList";
import page404 from "../components/page404";
import commonIndex from "../components/common/commonIndex";
import myLayout from "../components/myLayout";
import BlogDetailForCommon from "../components/common/BlogDetailForCommon";
import BlogListForCommon from "../components/common/BlogListForCommon";
import categoryList from "../components/users/categoryList";
// 引入vuex
import store from '@/store';
import BlogSearchForCommon from "../components/common/BlogSearchForCommon";
Vue.use(Router)
/*
  防止重复点击使得路由重复，报错。
 */
const originalPush = Router.prototype.push
Router.prototype.push = function push(location) {
  return originalPush.call(this, location).catch(err => err)
}
const router=new Router({
  // mode: 'history',  //去掉url中的#
  routes: [
    {
      path: '/login',
      component: login,
      meta: {
        title: '登录页',
      }
    },
    {
      path: '/',
      redirect: '/index/bloglist/all/all'
    },
    {
      path: '/index/bloglist',
      redirect: '/index/bloglist/all/all'
    },
    {
      path: '/index',
      component: commonIndex,
      redirect: '/index/bloglist/all/all',
      meta: {
        title: '首页',
      },
      children: [
        {
          path: 'search/:whatsearch',
          component: BlogSearchForCommon,
          meta: {
            title: '博客搜索',
          }
        },
        {
          path: 'bloglist/:bloglist/:bloglist2',
          component: BlogListForCommon,
          meta: {
            title: '博客列表',
          }
        },
        {
          path: 'blog/:blogid',
          component: BlogDetailForCommon,
          meta: {
            title: '博客详情',
          }
        },
      ]
    },
    {
      path: '/admin',
      component: myLayout,
      meta: { requiresAuth: true,title: '个人博客' },
      redirect: '/admin/index',
      children:[
        {
          path: 'index',
          component: Index,
          meta: {
            requiresAuth: true,
            title: '个人博客首页',
          },
        },
        {
          path: 'showBlogList',
          component: blogList,
          meta: {
            requiresAuth: true,
            title: '个人博客列表',
          },
        },
        {
          path: 'showAddBlog',
          component: addBlog,
          meta: {
            requiresAuth: true,
            title: '发布个人博客',
          },
        },
        {
          path: 'showCategoryList',
          component: categoryList,
          meta: {
            requiresAuth: true,
            title: '博客类别列表',
          },
        },
      ]
    },
    {
      path: "*",
      component: page404,
      meta: {
        title: '404，页面丢失',
      }
     }
  ]
})
router.beforeEach((to, from, next) => {
  window.document.title = to.meta.title
  if (to.meta.requiresAuth && (store.state.token==='' || store.state.myuser.userid==='' || store.state.myuser.username==='')){
    const allcookie = document.cookie.split(';')
    if (allcookie!=""){
      store.commit('setmyuserid',allcookie[0].trim().substring(9,allcookie[0].trim().length))
      store.commit('setmyusername',allcookie[1].trim().substring(11,allcookie[1].trim().length))
      store.commit('settoken',allcookie[2].trim().substring(6,allcookie[2].trim().length))
      return next()
    }
    store.commit('logout')
    return next("/login")
  }
  next()
})
export default router
