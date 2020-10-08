package com.shiyifan.service;

import com.google.gson.Gson;
import com.shiyifan.dao.BlogMapper;
import com.shiyifan.dao.CategoryMapper;
import com.shiyifan.pojo.ElasticSearchBlog;
import com.shiyifan.pojo.Myblog;
import com.shiyifan.pojo.Mycategory;
import com.shiyifan.utils.ArabicNumToChineseNumUtil;
import com.shiyifan.utils.RedisUtil;
import lombok.extern.log4j.Log4j2;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
@Transactional
@Order
@Log4j2
public class BlogServiceImpl implements BlogService,ApplicationRunner {

    @Autowired
    private BlogMapper blogMapper;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private ArabicNumToChineseNumUtil numUtil;

    @Autowired
    private RestHighLevelClient restHighLevelClient;

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
        log.info("redis添加成功");
        addElasticsearchBlog(myblog.getBlogId());
    }

    @Retryable(value = Exception.class)
    private void addElasticsearchBlog(String blogId){
        try {
            ElasticSearchBlog blog = blogMapper.selectElasticSearchBlogByIdForCommon(blogId);
            IndexRequest indexRequest = new IndexRequest("blogindex");
            Gson gson = new Gson();
            indexRequest.id(blog.getBlogId());
            indexRequest.source(gson.toJson(blog), XContentType.JSON);
            restHighLevelClient.index(indexRequest, RequestOptions.DEFAULT);
        } catch (Exception e) {
            log.error(e);
        }
    }

    @Override
    public Myblog selectBlogById(int userid, String blogid) {
        Myblog myblog = blogMapper.selectBlogById(userid, blogid);
        return myblog;
    }

    @Override
    public void deleteBlogById(int userid, String blogid, int categoryid) {
        blogMapper.deleteBlogById(userid, blogid);
        categoryMapper.deleteCategoryRank(userid, categoryid);
        ArrayList<Object> myblogs = (ArrayList<Object>) redisUtil.lGet("user-"+userid+"-myblogs", 0, -1);
        Iterator<Object> iterator = myblogs.iterator();
        while (iterator.hasNext()){
            Myblog myblog = (Myblog) iterator.next();
            if(myblog.getBlogId().equals(blogid)){
                redisUtil.lRemove("user-"+userid+"-myblogs",1,myblog);
                redisUtil.decr("user-"+userid+"myblogsTotal",1);
                log.info("redis删除成功");
                break;
            }
        }
        myblogs = (ArrayList<Object>) redisUtil.lGet("myblogsForCommon", 0, -1);
        iterator = myblogs.iterator();
        while (iterator.hasNext()){
            Myblog myblog = (Myblog) iterator.next();
            if(myblog.getBlogId().equals(blogid)){
                redisUtil.lRemove("myblogsForCommon",1,myblog);
                redisUtil.decr("myblogsForCommonTotal",1);
                redisUtil.lRemove("category-"+categoryid+"-myblogsForCommon",1,myblog);
                redisUtil.decr("category-"+categoryid+"-myblogsForCommonTotal",1);
                log.info("redis删除成功");
                break;
            }
        }
        try {
            DeleteRequest deleteRequest = new DeleteRequest("blogindex", blogid);
            deleteRequest.timeout("1s");
            restHighLevelClient.delete(deleteRequest, RequestOptions.DEFAULT);
        } catch (Exception e) {
            log.error(e);
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
        ArrayList<Object> myblogs = (ArrayList<Object>) redisUtil.lGet("user-"+myblog.getMyuser().getUserId()+"-myblogs", 0, -1);
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
        myblogs = (ArrayList<Object>) redisUtil.lGet("myblogsForCommon", 0, -1);
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
        redisUtil.del(myblog.getBlogId());
        try {
            DeleteRequest deleteRequest = new DeleteRequest("blogindex", myblog.getBlogId());
            deleteRequest.timeout("1s");
            restHighLevelClient.delete(deleteRequest, RequestOptions.DEFAULT);
            ElasticSearchBlog blog = blogMapper.selectElasticSearchBlogByIdForCommon(myblog.getBlogId());
            IndexRequest indexRequest = new IndexRequest("blogindex");
            Gson gson = new Gson();
            indexRequest.id(blog.getBlogId());
            indexRequest.source(gson.toJson(blog), XContentType.JSON);
            restHighLevelClient.index(indexRequest, RequestOptions.DEFAULT);
        } catch (Exception e) {
            log.error(e);
        }
    }

    @Override
    public ArrayList<Myblog> selectBlogByPage(int userid, int pageNow, int pageSize) {
        int start = (pageNow-1)*pageSize;
        int end = (pageNow*pageSize)-1;
        ArrayList<Object> myblogs = (ArrayList<Object>) redisUtil.lGet("user-"+userid+"-myblogs", start, end);
        if(myblogs.size()==0){
            log.info("初始化redis,redis中不存在");
            Iterator<Myblog> iterator = blogMapper.selectBlogAll(userid).iterator();
            while (iterator.hasNext()){
                redisUtil.RSet("user-"+userid+"-myblogs", iterator.next());
            }
            myblogs= (ArrayList<Object>) redisUtil.lGet("user-"+userid+"-myblogs", start, end);
        }
        log.info("redis中存在");
        return (ArrayList<Myblog>)(Object)myblogs;
    }

    @Override
    public int selectTotalBlogNums(int userid) {
        Object myblogsTotal = redisUtil.get("user-"+userid+"myblogsTotal");
        if(myblogsTotal==null){
            log.info("初始化redis,redis中不存在");
            redisUtil.set("user-"+userid+"myblogsTotal", blogMapper.selectTotalBlogNums(userid));
            myblogsTotal=redisUtil.get("user-"+userid+"myblogsTotal");
        }
        log.info("redis中存在");
        return (int) myblogsTotal;
    }

//   todo
//   未使用
    @Override
    public ArrayList<Myblog> selectBlogByCategoryIdAndPage(int userid, int categoryid, int pageNow, int pageSize) {
        return blogMapper.selectBlogByCategoryIdAndPage(userid,categoryid,pageNow,pageSize);
    }

    @Override
    public Boolean setTempBlog(Myblog myblog) {
        try {
            redisUtil.set("user-"+myblog.getMyuser().getUserId()+"-TempBlog", myblog);
            log.info("添加暂存博客成功");
            return true;
        }
        catch (Exception e){
            log.error(e);
            return false;
        }

    }

    @Override
    public Myblog getTempBlog(int userid) {
        try {
            Myblog myblog = (Myblog) redisUtil.get("user-" + userid + "-TempBlog");
            log.info("读取暂存博客成功");
            redisUtil.del("user-" + userid + "-TempBlog");
            return myblog;
        }
        catch (Exception e){
            log.error(e);
            return null;
        }
    }

    /*---------------------------------------------------------------------------*/

    /*------------------------------公共操作-------------------------------*/
    @Override
    public Myblog selectBlogByIdForCommon(String blogid) {
        Myblog myblog = (Myblog) redisUtil.get(blogid);
        if(myblog==null){
            log.info("初始化redis,redis中不存在");
            redisUtil.set(blogid, blogMapper.selectBlogByIdForCommon(blogid));
            myblog = (Myblog) redisUtil.get(blogid);
        }
        log.info("redis中存在");
        return myblog;
    }


    @Override
    public ArrayList<Myblog> selectBlogAllByPageForCommon(int categoryid,int pageNow,int pageSize) {
        int start = (pageNow-1)*pageSize;
        int end = (pageNow*pageSize)-1;
        ArrayList<Object> myblogs=null;
        if(categoryid==0){
            myblogs = (ArrayList<Object>) redisUtil.lGet("myblogsForCommon", start, end);
        }
        else {
            myblogs = (ArrayList<Object>) redisUtil.lGet("category-"+categoryid+"-myblogsForCommon", start, end);
        }
        if(myblogs.size()==0){
            log.info("初始化redis,redis中不存在");
            Iterator<Myblog> iterator = blogMapper.selectBlogAllForCommon(0).iterator();
            while (iterator.hasNext()){
                redisUtil.RSet("myblogsForCommon", iterator.next());
            }
            myblogs= (ArrayList<Object>) redisUtil.lGet("myblogsForCommon", start, end);
        }
        log.info("redis中存在");
        return (ArrayList<Myblog>)(Object)myblogs;
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
            log.info("初始化redis,redis中不存在");
            redisUtil.set("myblogsForCommonTotal", blogMapper.selectTotalBlogNumsForCommon(0));
            myblogsTotal=redisUtil.get("myblogsForCommonTotal");
        }
        log.info("redis中存在");
        return (int) myblogsTotal;
    }


    @Override
    public ArrayList<Myblog> selectBlogByAuthorForCommon(int userid, int pageNow, int pageSize) {
        int start = (pageNow-1)*pageSize;
        return blogMapper.selectBlogByAuthorForCommon(userid,start,pageSize);
    }



    /*---------------------------------------------------------------------------*/

    /*------------------------------搜索操作-------------------------------*/

    @Override
    @Retryable(value = Exception.class)
    public ArrayList<Map<String, Object>> searchContentPage(String keyword, int pageNow, int pageSize) {
        int start = (pageNow-1)*pageSize;
        SearchRequest searchRequest = new SearchRequest("blogindex");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.multiMatchQuery(keyword, "blogTitle","blogContent").analyzer("ik_max_word"))
                .from(start)
                .size(pageSize)
                .highlighter(new HighlightBuilder().field("*").requireFieldMatch(false).preTags("<span style=\"color:red;font-weight:bold\">").postTags("</span>"));
        searchSourceBuilder.timeout(new TimeValue(60, TimeUnit.SECONDS));
        searchRequest.source(searchSourceBuilder);
        SearchResponse search = null;
        try {
            search = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            log.error(e);
        }
        ArrayList<Map<String, Object>> list = new ArrayList<>();
        for (SearchHit hit : search.getHits().getHits()) {
            Map<String, HighlightField> highlightFields = hit.getHighlightFields();
            Map<String, Object> sourceAsMap = hit.getSourceAsMap();
            if (highlightFields.containsKey("blogContent")){
                sourceAsMap.put("blogContent",highlightFields.get("blogContent").fragments()[0].toString());
            }
            if (highlightFields.containsKey("blogTitle")){
                sourceAsMap.put("blogTitle",highlightFields.get("blogTitle").fragments()[0].toString());
            }
            list.add(sourceAsMap);
        }
        if(redisUtil.get(keyword)==null){
            redisUtil.set(keyword, 1);
        }
        else {
            redisUtil.incr(keyword, 1);
            if(Integer.parseInt(String.valueOf(redisUtil.get(keyword)))==5){
                try {
                    FileWriter fw = new FileWriter("/home/syf/myblog/remote.txt", true);
                    BufferedWriter bw = new BufferedWriter(fw);
                    // 往已有的文件上添加字符串
                    bw.write(keyword+"\n");
                    bw.close();
                    fw.close();
                }
                catch (Exception e){
                    log.error(e);
                }
            }
        }
        return list;
    }

    /*---------------------------------------------------------------------------*/

    @Override
    public void run(ApplicationArguments args){
        log.info("初始化redis,redis中不存在");
        try {
            redisUtil.flushDb();
            ArrayList<Mycategory> mycategories = categoryMapper.selectAllCategoryForCommon();
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
            log.error(e);
        }
    }

    @Recover
    public void recover(Exception e){
        log.warn("搜索重试失败;"+e);
    }
}

