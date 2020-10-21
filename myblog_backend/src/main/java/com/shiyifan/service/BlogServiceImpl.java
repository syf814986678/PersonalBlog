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

/**
 * @author 81498
 */
@Service
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
    @Transactional(rollbackFor = Exception.class)
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
    }

    @Override
    @Retryable(value = IOException.class)
    public void addElasticsearchBlog(String blogId) throws IOException {
        ElasticSearchBlog blog = blogMapper.selectElasticSearchBlogByIdForCommon(blogId);
        IndexRequest indexRequest = new IndexRequest("blogindex");
        indexRequest.timeout("2s");
        Gson gson = new Gson();
        indexRequest.id(blog.getBlogId());
        indexRequest.source(gson.toJson(blog), XContentType.JSON);
        log.warn("添加ElasticsearchBlog开始");
        restHighLevelClient.index(indexRequest, RequestOptions.DEFAULT);
        log.warn("添加ElasticsearchBlog成功");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteBlogById(int userId, String blogId, int categoryId) {
        blogMapper.deleteBlogById(userId, blogId);
        categoryMapper.deleteCategoryRank(userId, categoryId);
        ArrayList<Object> myblogs = (ArrayList<Object>) redisUtil.lGet("user-"+ userId +"-myblogs", 0, -1);
        Iterator<Object> iterator = myblogs.iterator();
        while (iterator.hasNext()){
            Myblog myblog = (Myblog) iterator.next();
            if(myblog.getBlogId().equals(blogId)){
                redisUtil.lRemove("user-"+ userId +"-myblogs",1,myblog);
                redisUtil.decr("user-"+ userId +"myblogsTotal",1);
                log.info("redis删除成功");
                break;
            }
        }
        myblogs = (ArrayList<Object>) redisUtil.lGet("myblogsForCommon", 0, -1);
        iterator = myblogs.iterator();
        while (iterator.hasNext()){
            Myblog myblog = (Myblog) iterator.next();
            if(myblog.getBlogId().equals(blogId)){
                redisUtil.lRemove("myblogsForCommon",1,myblog);
                redisUtil.decr("myblogsForCommonTotal",1);
                redisUtil.lRemove("category-"+ categoryId +"-myblogsForCommon",1,myblog);
                redisUtil.decr("category-"+ categoryId +"-myblogsForCommonTotal",1);
                log.info("redis删除成功");
                break;
            }
        }
    }

    @Override
    @Retryable(value = IOException.class)
    public void deleteElasticsearchBlog(String blogId) throws IOException {
        DeleteRequest deleteRequest = new DeleteRequest("blogindex", blogId);
        deleteRequest.timeout("2s");
        log.warn("删除ElasticsearchBlog开始");
        restHighLevelClient.delete(deleteRequest, RequestOptions.DEFAULT);
        log.warn("删除ElasticsearchBlog成功");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
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
    }

    @Override
    @Retryable(value = IOException.class)
    public void updateElasticsearchBlog(String blogId)throws IOException{
        DeleteRequest deleteRequest = new DeleteRequest("blogindex", blogId);
        deleteRequest.timeout("2s");
        log.warn("更新ElasticsearchBlog(删除)开始");
        restHighLevelClient.delete(deleteRequest, RequestOptions.DEFAULT);
        log.warn("更新ElasticsearchBlog(删除)成功");
        ElasticSearchBlog blog = blogMapper.selectElasticSearchBlogByIdForCommon(blogId);
        IndexRequest indexRequest = new IndexRequest("blogindex");
        indexRequest.timeout("2s");
        Gson gson = new Gson();
        indexRequest.id(blog.getBlogId());
        indexRequest.source(gson.toJson(blog), XContentType.JSON);
        log.warn("更新ElasticsearchBlog(添加)开始");
        restHighLevelClient.index(indexRequest, RequestOptions.DEFAULT);
        log.warn("更新ElasticsearchBlog(添加)成功");
    }

    @Override
    public Myblog selectBlogById(int userId, String blogId) {
        Myblog myblog = blogMapper.selectBlogById(userId, blogId);
        return myblog;
    }

    @Override
    public ArrayList<Myblog> selectBlogByPage(int userId, int pageNow, int pageSize) {
        int start = (pageNow-1)*pageSize;
        int end = (pageNow*pageSize)-1;
        ArrayList<Object> myblogs = (ArrayList<Object>) redisUtil.lGet("user-"+ userId +"-myblogs", start, end);
        if(myblogs.size()==0){
            log.info("初始化redis-> selectBlogByPage");
            Iterator<Myblog> iterator = blogMapper.selectBlogAll(userId).iterator();
            while (iterator.hasNext()){
                redisUtil.RSet("user-"+ userId +"-myblogs", iterator.next());
            }
            myblogs= (ArrayList<Object>) redisUtil.lGet("user-"+ userId +"-myblogs", start, end);
        }
        log.info("redis中存在selectBlogByPage-> start:"+start+"<=>"+"end:"+end);
        return (ArrayList<Myblog>)(Object)myblogs;
    }

    @Override
    public int selectTotalBlogNums(int userId) {
        Object myblogsTotal = redisUtil.get("user-"+ userId +"myblogsTotal");
        if(myblogsTotal==null){
            log.info("初始化redis-> selectTotalBlogNums");
            redisUtil.set("user-"+ userId +"myblogsTotal", blogMapper.selectTotalBlogNums(userId));
            myblogsTotal=redisUtil.get("user-"+ userId +"myblogsTotal");
        }
        log.info("redis中存在selectTotalBlogNums-> nums:"+(int) myblogsTotal);
        return (int) myblogsTotal;
    }

//   todo
//   未使用
    @Override
    public ArrayList<Myblog> selectBlogByCategoryIdAndPage(int userId, int categoryId, int pageNow, int pageSize) {
        return blogMapper.selectBlogByCategoryIdAndPage(userId, categoryId,pageNow,pageSize);
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
    public Myblog getTempBlog(int userId) {
        try {
            Myblog myblog = (Myblog) redisUtil.get("user-" + userId + "-TempBlog");
            log.info("读取暂存博客成功");
            redisUtil.del("user-" + userId + "-TempBlog");
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
    public Myblog selectBlogByIdForCommon(String blogId) {
        Myblog myblog = (Myblog) redisUtil.get(blogId);
        if(myblog==null){
            log.info("初始化redis-> selectBlogByIdForCommon");
            redisUtil.set(blogId, blogMapper.selectBlogByIdForCommon(blogId));
            myblog = (Myblog) redisUtil.get(blogId);
        }
        log.info("redis中存在selectBlogByIdForCommon-> blogId:"+ blogId);
        return myblog;
    }


    @Override
    public ArrayList<Myblog> selectBlogAllByPageForCommon(int categoryId, int pageNow, int pageSize) {
        ArrayList<Object> myblogs=new ArrayList<>();
        for (int i = 1; i <= pageNow; i++) {
            int start = (i-1)*pageSize;
            int end = (i*pageSize)-1;
            ArrayList<Object> blogs=null;
            if(categoryId == 0){
                blogs = (ArrayList<Object>) redisUtil.lGet("myblogsForCommon", start, end);
            }
            else {
                blogs = (ArrayList<Object>) redisUtil.lGet("category-"+ categoryId +"-myblogsForCommon", start, end);
            }
            if(blogs.size()==0){
                log.info("初始化redis-> selectBlogAllByPageForCommon");
                Iterator<Myblog> iterator = blogMapper.selectBlogAllForCommon(0).iterator();
                while (iterator.hasNext()){
                    redisUtil.RSet("myblogsForCommon", iterator.next());
                }
                blogs= (ArrayList<Object>) redisUtil.lGet("myblogsForCommon", start, end);
            }
            myblogs.addAll(blogs);
            log.info("redis中存在selectBlogAllByPageForCommon-> categoryId:"+ categoryId +"<=>"+"start:"+start+"<=>"+"end:"+end);
        }
        return (ArrayList<Myblog>)(Object)myblogs;
    }

    @Override
    public int selectTotalBlogNumsForCommon(int categoryId) {
        Object myblogsTotal =null;
        if(categoryId ==0){
            myblogsTotal = redisUtil.get("myblogsForCommonTotal");
        }
        else {
            myblogsTotal = redisUtil.get("category-"+ categoryId +"-myblogsForCommonTotal");
        }
        if(myblogsTotal==null){
            log.info("初始化redis-> selectTotalBlogNumsForCommon");
            redisUtil.set("myblogsForCommonTotal", blogMapper.selectTotalBlogNumsForCommon(0));
            myblogsTotal=redisUtil.get("myblogsForCommonTotal");
        }
        log.info("redis中存在selectTotalBlogNumsForCommon-> categoryId:"+ categoryId +"<=>"+"nums:"+myblogsTotal);
        return (int) myblogsTotal;
    }


    @Override
    public ArrayList<Myblog> selectBlogByAuthorForCommon(int userId, int pageNow, int pageSize) {
        int start = (pageNow-1)*pageSize;
        return blogMapper.selectBlogByAuthorForCommon(userId,start,pageSize);
    }



    /*---------------------------------------------------------------------------*/

    /*------------------------------搜索操作-------------------------------*/

    @Override
    @Retryable(value = IOException.class)
    public ArrayList<Map<String, Object>> searchContentPage(String keyword, int pageNow, int pageSize) throws IOException {
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
        log.warn("搜索博客开始");
        search = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        log.warn("搜索博客成功");
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
        log.info("<--------------------初始化redis-------------------->");
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
}

