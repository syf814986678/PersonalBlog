package com.shiyifan.service;

import com.shiyifan.dao.BlogMapper;
import com.shiyifan.dao.CategoryMapper;
import com.shiyifan.pojo.Myblog;
import com.shiyifan.pojo.Mycategory;
import com.shiyifan.utils.ArabicNumToChineseNumUtil;
import com.shiyifan.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Iterator;
import java.util.List;
@Service
@Transactional
@Order
public class BlogServiceImpl implements BlogService, ApplicationRunner {

    @Autowired
    private BlogMapper blogMapper;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private ArabicNumToChineseNumUtil numUtil;

    /*----------------登陆后进行的操作----------------*/

    @Override
    public void addBlog(Myblog myblog) {
        int categoryRank = categoryMapper.getCategoryRank(myblog.getMyuser().getUserId(), myblog.getMycategory().getCategoryId());
        myblog.setBlogTitle(myblog.getBlogTitle()+"("+ numUtil.arabicNumToChineseNum(++categoryRank) +")");
        blogMapper.addBlog(myblog);
        categoryMapper.addCategoryRank(myblog.getMyuser().getUserId(), myblog.getMycategory().getCategoryId());
        Myblog mynewblog = blogMapper.selectBlogForOne(myblog.getMyuser().getUserId(), myblog.getBlogId());
        redisUtil.lSet("user-"+myblog.getMyuser().getUserId()+"-myblogs",mynewblog);
        redisUtil.incr("user-"+myblog.getMyuser().getUserId()+"myblogsTotal",1);
        mynewblog=blogMapper.selectBlogForOneForCommon(myblog.getBlogId());
        redisUtil.lSet("myblogsForCommon",mynewblog);
        redisUtil.incr("myblogsForCommonTotal",1);
        redisUtil.lSet("category-"+myblog.getMycategory().getCategoryId()+"-myblogsForCommon",mynewblog);
        redisUtil.incr("category-"+myblog.getMycategory().getCategoryId()+"-myblogsForCommonTotal",1);
        System.out.println("redis添加成功");
    }

    @Override
    public Myblog selectBlogById(int userid, String blogid) {
        Myblog myblog = blogMapper.selectBlogById(userid, blogid);
        if (myblog.getMycategory()==null){
            Mycategory mycategory = new Mycategory();
            mycategory.setCategoryId(0);
            mycategory.setCategoryName("无");
            myblog.setMycategory(mycategory);
        }
        return myblog;
    }

    @Override
    public void deleteBlogById(int userid, String blogid, int categoryid) {
        blogMapper.deleteBlogById(userid, blogid);
        categoryMapper.deleteCategoryRank(userid, categoryid);
        List<Object> myblogs = redisUtil.lGet("user-"+userid+"-myblogs", 0, -1);
        Iterator<Object> iterator = myblogs.iterator();
        while (iterator.hasNext()){
            Myblog myblog = (Myblog) iterator.next();
            if(myblog.getBlogId().equals(blogid)){
                redisUtil.lRemove("user-"+userid+"-myblogs",1,myblog);
                redisUtil.decr("user-"+userid+"myblogsTotal",1);
                System.out.println("myblogs-----redis删除成功");
                break;
            }
        }
        myblogs = redisUtil.lGet("myblogsForCommon", 0, -1);
        iterator = myblogs.iterator();
        while (iterator.hasNext()){
            Myblog myblog = (Myblog) iterator.next();
            if(myblog.getBlogId().equals(blogid)){
                redisUtil.lRemove("myblogsForCommon",1,myblog);
                redisUtil.decr("myblogsForCommonTotal",1);
                redisUtil.lRemove("category-"+categoryid+"-myblogsForCommon",1,myblog);
                redisUtil.decr("category-"+categoryid+"-myblogsForCommonTotal",1);
                System.out.println("myblogsForCommon---redis删除成功");
                break;
            }
        }
    }

    @Override
    public void updateBlog(Myblog myblog) {
        int categoryidInDB = categoryMapper.getCategoryid(myblog.getMyuser().getUserId(), myblog.getBlogId());
        if(categoryidInDB!=myblog.getMycategory().getCategoryId()){
            categoryMapper.deleteCategoryRank(myblog.getMyuser().getUserId(), categoryidInDB);
            categoryMapper.addCategoryRank(myblog.getMyuser().getUserId(), myblog.getMycategory().getCategoryId());
        }
        blogMapper.updateBlog(myblog);
        Myblog mynewblog = blogMapper.selectBlogForOne(myblog.getMyuser().getUserId(), myblog.getBlogId());
        List<Object> myblogs = redisUtil.lGet("user-"+myblog.getMyuser().getUserId()+"-myblogs", 0, -1);
        Iterator<Object> iterator = myblogs.iterator();
        int index = 0;
        while (iterator.hasNext()){
            Myblog myredisblog = (Myblog) iterator.next();
            if(myredisblog.getBlogId().equals(myblog.getBlogId())){
                redisUtil.lUpdateIndex("user-"+myblog.getMyuser().getUserId()+"-myblogs",index,mynewblog);
                break;
            }
            index++;
        }
        index=0;
        mynewblog=blogMapper.selectBlogForOneForCommon(myblog.getBlogId());
        myblogs = redisUtil.lGet("myblogsForCommon", 0, -1);
        iterator = myblogs.iterator();
        while (iterator.hasNext()){
            Myblog myredisblog = (Myblog) iterator.next();
            if(myredisblog.getBlogId().equals(myblog.getBlogId())){
                redisUtil.lUpdateIndex("myblogsForCommon",index,mynewblog);
                if(categoryidInDB!=myblog.getMycategory().getCategoryId()){
                    redisUtil.lRemove("category-"+categoryidInDB+"-myblogsForCommon",1,myredisblog);
                    redisUtil.decr("category-"+categoryidInDB+"-myblogsForCommonTotal",1);
                    redisUtil.lSet("category-"+myblog.getMycategory().getCategoryId()+"-myblogsForCommon",mynewblog);
                    redisUtil.incr("category-"+myblog.getMycategory().getCategoryId()+"-myblogsForCommonTotal",1);
                }
                else {
                    redisUtil.lUpdateIndex("category-"+myblog.getMycategory().getCategoryId()+"-myblogsForCommon",index,mynewblog);
                }
                break;
            }
            index++;
        }
    }

    @Override
    public List<Myblog> selectBlogByPage(int userid, int pageNow, int pageSize) {
        int start = (pageNow-1)*pageSize;
        int end = (pageNow*pageSize)-1;
        List<Object> myblogs = redisUtil.lGet("user-"+userid+"-myblogs", start, end);
        if(myblogs.size()==0){
            System.out.println("初始化redis,redis中不存在");
            Iterator<Myblog> iterator = blogMapper.selectBlogAll(userid).iterator();
            while (iterator.hasNext()){
                redisUtil.RSet("user-"+userid+"-myblogs", iterator.next());
            }
            myblogs=redisUtil.lGet("user-"+userid+"-myblogs", start, end);
        }
        System.out.println("redis中存在");
        return (List<Myblog>)(Object)myblogs;
    }

    @Override
    public int selectTotalBlogNums(int userid) {
        Object myblogsTotal = redisUtil.get("user-"+userid+"myblogsTotal");
        if(myblogsTotal==null){
            System.out.println("初始化redis,redis中不存在");
            redisUtil.set("user-"+userid+"myblogsTotal", blogMapper.selectTotalBlogNums(userid));
            myblogsTotal=redisUtil.get("user-"+userid+"myblogsTotal");
        }
        System.out.println("redis中存在");
        return (int) myblogsTotal;
    }

//   todo
//   未使用
    @Override
    public List<Myblog> selectBlogByCategoryIdAndPage(int userid, int categoryid, int pageNow, int pageSize) {
        return blogMapper.selectBlogByCategoryIdAndPage(userid,categoryid,pageNow,pageSize);
    }

    @Override
    public Boolean setTempBlog(Myblog myblog) {
        try {
            redisUtil.set("user-"+myblog.getMyuser().getUserId()+"-TempBlog", myblog);
            System.out.println("添加暂存博客成功");
            return true;
        }
        catch (Exception e){
            e.printStackTrace();
            return false;
        }

    }

    @Override
    public Myblog getTempBlog(int userid) {
        try {
            Myblog myblog = (Myblog) redisUtil.get("user-" + userid + "-TempBlog");
            System.out.println("读取暂存博客成功");
            redisUtil.del("user-" + userid + "-TempBlog");
            return myblog;
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    /*---------------------------------------------------------------------------*/

    /*------------------------------公共操作-------------------------------*/
    @Override
    public Myblog selectBlogByIdForCommon(String blogid) {
        return blogMapper.selectBlogByIdForCommon(blogid);
    }

    @Override
    public List<Myblog> selectBlogAllByPageForCommon(int categoryid,int pageNow,int pageSize) {
        int start = (pageNow-1)*pageSize;
        int end = (pageNow*pageSize)-1;
        List<Object> myblogs=null;
        if(categoryid==0){
            myblogs = redisUtil.lGet("myblogsForCommon", start, end);
        }
        else {
            myblogs = redisUtil.lGet("category-"+categoryid+"-myblogsForCommon", start, end);
        }
        if(myblogs.size()==0){
            System.out.println("初始化redis,redis中不存在");
            Iterator<Myblog> iterator = blogMapper.selectBlogAllForCommon(0).iterator();
            while (iterator.hasNext()){
                redisUtil.RSet("myblogsForCommon", iterator.next());
            }
            myblogs=redisUtil.lGet("myblogsForCommon", start, end);
        }
        System.out.println("redis中存在");
        return (List<Myblog>)(Object)myblogs;
    }

    @Override
    public int selectTotalBlogNumsForCommon(int categoryid) {
        Object myblogsTotal =null;
        if(categoryid==0){
            myblogsTotal = redisUtil.get("myblogsForCommonTotal");
        }
        else {
            myblogsTotal = redisUtil.get("category-"+categoryid+"-myblogsForCommonTotal");
        }
        if(myblogsTotal==null){
            System.out.println("初始化redis,redis中不存在");
            redisUtil.set("myblogsForCommonTotal", blogMapper.selectTotalBlogNumsForCommon(0));
            myblogsTotal=redisUtil.get("myblogsForCommonTotal");
        }
        System.out.println("redis中存在");
        return (int) myblogsTotal;
    }


    @Override
    public List<Myblog> selectBlogByAuthorForCommon(int userid, int pageNow, int pageSize) {
        int start = (pageNow-1)*pageSize;
        return blogMapper.selectBlogByAuthorForCommon(userid,start,pageSize);
    }


    @Override
    public void run(ApplicationArguments args) throws Exception {
        System.out.println("初始化redis,redis中不存在");
        try {
            redisUtil.flushDb();
            List<Mycategory> mycategories = categoryMapper.selectAllCategoryForCommon();
            Iterator<Mycategory> iterator = mycategories.iterator();
            while (iterator.hasNext()){
                Mycategory mycategory = iterator.next();
                Iterator<Myblog> iteratorMyblog = blogMapper.selectBlogAllForCommon(mycategory.getCategoryId()).iterator();
                while (iteratorMyblog.hasNext()){
                    redisUtil.RSet("category-"+mycategory.getCategoryId()+"-myblogsForCommon", iteratorMyblog.next());
                }
                redisUtil.set("category-"+mycategory.getCategoryId()+"-myblogsForCommonTotal", blogMapper.selectTotalBlogNumsForCommon(mycategory.getCategoryId()));            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }


}

