package com.shiyifan.service;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dcdn.model.v20180115.PreloadDcdnObjectCachesRequest;
import com.aliyuncs.dcdn.model.v20180115.PreloadDcdnObjectCachesResponse;
import com.aliyuncs.dcdn.model.v20180115.RefreshDcdnObjectCachesRequest;
import com.aliyuncs.dcdn.model.v20180115.RefreshDcdnObjectCachesResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import com.google.gson.Gson;
import com.shiyifan.constant.MyConstant;
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
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author ZouCha
 * @name BlogServiceImpl
 * @date 2020-11-20 15:25:35
 *
 **/
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

    @Autowired
    private MyConstant myConstant;

    /*----------------登陆后进行的操作----------------*/

    /**
     *
     * @author ZouCha
     * @date 2020-11-20 15:25:47
     * @method addBlog
     * @params [myblog]
     * @return void
     *
     **/
    @Override
    @Transactional
    public Boolean addBlog(Myblog myblog) {
        try {
            int categoryRank = categoryMapper.getCategoryRank(myblog.getMyuser().getUserId(), myblog.getMycategory().getCategoryId());
            myblog.setBlogTitle(myblog.getBlogTitle()+"("+ numUtil.arabicNumToChineseNum(++categoryRank) +")");
            blogMapper.addBlog(myblog);
            categoryMapper.addCategoryRank(myblog.getMyuser().getUserId(), myblog.getMycategory().getCategoryId());
            Myblog mynewblog = blogMapper.selectBlogForOne(myblog.getMyuser().getUserId(), myblog.getBlogId());
            redisUtil.lSet(myConstant.getUserBlogs()+myblog.getMyuser().getUserId(),mynewblog);
            redisUtil.incr(myConstant.getUserTotalBlogs()+myblog.getMyuser().getUserId(),1);
            mynewblog=blogMapper.selectBlogForOneForCommon(myblog.getBlogId());
            redisUtil.lSet(myConstant.getBlogsForCommon(),mynewblog);
            redisUtil.incr(myConstant.getTotalBlogsForCommon(),1);
            redisUtil.lSet(myConstant.getCategoryBlogsForCommon()+myblog.getMycategory().getCategoryId(),mynewblog);
            redisUtil.incr(myConstant.getCategoryTotalBlogsForCommon()+myblog.getMycategory().getCategoryId(),1);
            log.info("博客ID:"+mynewblog.getBlogId()+"-添加成功");
            return true;
        }
        catch (Exception e){
            log.error(e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return false;
        }
    }
    /**
     *
     * @author ZouCha
     * @date 2020-11-21 20:48:13
     * @method addElasticsearchBlog
     * @params [blogId]
     * @return java.lang.Boolean
     *
     **/
    @Override
    @Retryable(value = IOException.class)
    public Boolean addElasticsearchBlog(String blogId) throws IOException {
        try {
            ElasticSearchBlog blog = blogMapper.selectElasticSearchBlogByIdForCommon(blogId);
            IndexRequest indexRequest = new IndexRequest("blogindex");
            indexRequest.timeout("2s");
            Gson gson = new Gson();
            indexRequest.id(blog.getBlogId());
            indexRequest.source(gson.toJson(blog), XContentType.JSON);
            log.warn("添加ElasticsearchBlog开始");
            restHighLevelClient.index(indexRequest, RequestOptions.DEFAULT);
            log.warn("添加ElasticsearchBlog成功");
            return true;
        }
        catch (Exception e){
            log.error(e);
            throw new IOException();
        }
    }
    /**
     *
     * @author ZouCha
     * @date 2020-11-20 15:25:58
     * @method deleteBlogById
     * @params [userId, blogId, categoryId]
     * @return void
     *
     **/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteBlogById(int userId, String blogId, int categoryId) {
        try {
            blogMapper.deleteBlogById(userId, blogId);
            categoryMapper.deleteCategoryRank(userId, categoryId);
            redisUtil.del(blogId);
            ArrayList<Object> myblogs = (ArrayList<Object>) redisUtil.lGet(myConstant.getUserBlogs()+userId, 0, -1);
            Iterator<Object> iterator = myblogs.iterator();
            while (iterator.hasNext()){
                Myblog myblog = (Myblog) iterator.next();
                if(myblog.getBlogId().equals(blogId)){
                    redisUtil.lRemove(myConstant.getUserBlogs()+userId,1,myblog);
                    redisUtil.decr(myConstant.getUserTotalBlogs()+userId,1);
                    break;
                }
            }
            myblogs = (ArrayList<Object>) redisUtil.lGet(myConstant.getBlogsForCommon(), 0, -1);
            iterator = myblogs.iterator();
            while (iterator.hasNext()){
                Myblog myblog = (Myblog) iterator.next();
                if(myblog.getBlogId().equals(blogId)){
                    redisUtil.lRemove(myConstant.getBlogsForCommon(),1,myblog);
                    redisUtil.decr(myConstant.getTotalBlogsForCommon(),1);
                    redisUtil.lRemove(myConstant.getCategoryBlogsForCommon()+categoryId,1,myblog);
                    redisUtil.decr(myConstant.getCategoryTotalBlogsForCommon()+categoryId,1);
                    break;
                }
            }
            log.info("博客ID:"+blogId+"-删除成功");
            return true;
        }
        catch (Exception e){
            log.error(e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return false;
        }
    }
    /**
     *
     * @author ZouCha
     * @date 2020-11-20 15:26:02
     * @method deleteElasticsearchBlog
     * @params [blogId]
     * @return void
     *
     **/
    @Override
    @Retryable(value = IOException.class)
    public Boolean deleteElasticsearchBlog(String blogId) throws IOException {
        try {
            DeleteRequest deleteRequest = new DeleteRequest("blogindex", blogId);
            deleteRequest.timeout("2s");
            log.warn("删除ElasticsearchBlog开始");
            restHighLevelClient.delete(deleteRequest, RequestOptions.DEFAULT);
            log.warn("删除ElasticsearchBlog成功");
            return true;
        }
        catch (Exception e){
            log.error(e);
            throw new IOException();
        }
    }
    /**
     *
     * @author ZouCha
     * @date 2020-11-20 15:26:10
     * @method updateBlog
     * @params [myblog]
     * @return void
     *
     **/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateBlog(Myblog myblog) {
        try {
            int categoryidInDB = categoryMapper.getCategoryid(myblog.getMyuser().getUserId(), myblog.getBlogId());
            if(categoryidInDB!=myblog.getMycategory().getCategoryId()){
                categoryMapper.deleteCategoryRank(myblog.getMyuser().getUserId(), categoryidInDB);
                categoryMapper.addCategoryRank(myblog.getMyuser().getUserId(), myblog.getMycategory().getCategoryId());
                myblog.setBlogTitle(myblog.getBlogTitle().replace(myblog.getBlogTitle().substring(myblog.getBlogTitle().indexOf("(")), "")
                        +"("+ numUtil.arabicNumToChineseNum(categoryMapper.getCategoryRank(myblog.getMyuser().getUserId(), myblog.getMycategory().getCategoryId())) +")");
            }
            blogMapper.updateBlog(myblog);
            Myblog mynewblog = blogMapper.selectBlogForOne(myblog.getMyuser().getUserId(), myblog.getBlogId());
            ArrayList<Object> myblogs = (ArrayList<Object>) redisUtil.lGet(myConstant.getUserBlogs()+myblog.getMyuser().getUserId(), 0, -1);
            Iterator<Object> iterator = myblogs.iterator();
            int index = 0;
            while (iterator.hasNext()){
                Myblog myredisblog = (Myblog) iterator.next();
                if(myredisblog.getBlogId().equals(myblog.getBlogId())){
                    redisUtil.lUpdateIndex(myConstant.getUserBlogs()+myblog.getMyuser().getUserId(),index,mynewblog);
                    break;
                }
                index++;
            }
            index=0;
            mynewblog=blogMapper.selectBlogForOneForCommon(myblog.getBlogId());
            myblogs = (ArrayList<Object>) redisUtil.lGet(myConstant.getBlogsForCommon(), 0, -1);
            iterator = myblogs.iterator();
            while (iterator.hasNext()){
                Myblog myredisblog = (Myblog) iterator.next();
                if(myredisblog.getBlogId().equals(myblog.getBlogId())){
                    redisUtil.lUpdateIndex(myConstant.getBlogsForCommon(),index,mynewblog);
                    if(categoryidInDB!=myblog.getMycategory().getCategoryId()){
                        redisUtil.lRemove(myConstant.getCategoryBlogsForCommon()+categoryidInDB,1,myredisblog);
                        redisUtil.decr(myConstant.getCategoryTotalBlogsForCommon()+categoryidInDB,1);
                        redisUtil.lSet(myConstant.getCategoryBlogsForCommon()+myblog.getMycategory().getCategoryId(),mynewblog);
                        redisUtil.incr(myConstant.getCategoryTotalBlogsForCommon()+myblog.getMycategory().getCategoryId(),1);
                    }
                    else {
                        redisUtil.lUpdateIndex(myConstant.getCategoryBlogsForCommon()+myblog.getMycategory().getCategoryId(),index,mynewblog);
                    }
                    break;
                }
                index++;
            }
            redisUtil.del(myblog.getBlogId());
            log.info("博客ID:"+myblog.getBlogId()+"-更新成功");
            return true;
        }
        catch (Exception e){
            log.error(e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return false;
        }

    }
    /**
     *
     * @author ZouCha
     * @date 2020-11-20 15:26:13
     * @method updateElasticsearchBlog
     * @params [blogId]
     * @return void
     *
     **/
    @Override
    @Retryable(value = IOException.class)
    public Boolean updateElasticsearchBlog(String blogId)throws IOException{
        try {
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
            return true;
        }
        catch (Exception e){
            log.error(e);
            throw new IOException();
        }
    }
    /**
     *
     * @author ZouCha
     * @date 2020-11-20 15:26:17
     * @method selectBlogById
     * @params [userId, blogId]
     * @return com.shiyifan.pojo.Myblog
     *
     **/
    @Override
    public Myblog selectBlogById(int userId, String blogId) {
        Myblog myblog =null;
        try {
            myblog = blogMapper.selectBlogById(userId, blogId);
            log.info("博客ID:"+blogId+"-查找成功");
        }
        catch (Exception e){
            log.error(e);
        }
        return myblog;
    }
    /**
     *
     * @author ZouCha
     * @date 2020-11-20 15:26:20
     * @method selectBlogByPage
     * @params [userId, pageNow, pageSize]
     * @return java.util.ArrayList<com.shiyifan.pojo.Myblog>
     *
     **/
    @Override
    public ArrayList<Myblog> selectBlogByPage(int userId, int pageNow, int pageSize) {
        int start = (pageNow-1)*pageSize;
        int end = (pageNow*pageSize)-1;
        ArrayList<Myblog> myblogs = null;
        try {
            myblogs = (ArrayList<Myblog>)(Object) redisUtil.lGet(myConstant.getUserBlogs()+userId, start, end);
            if(myblogs == null || myblogs.size()==0){
                log.info("初始化->selectBlogByPage");
                Iterator<Myblog> iterator = blogMapper.selectBlogAll(userId).iterator();
                while (iterator.hasNext()){
                    redisUtil.RSet(myConstant.getUserBlogs()+userId, iterator.next());
                }
                myblogs = (ArrayList<Myblog>)(Object) redisUtil.lGet(myConstant.getUserBlogs()+userId, start, end);
            }
            log.info("selectBlogByPage-> start:"+start+"<=>"+"end:"+end);
        }
        catch (Exception e){
            log.error(e);
        }
        return myblogs;
    }
    /**
     * todo
     * @author ZouCha
     * @date 2020-11-20 15:26:25
     * @method selectTotalBlogNums
     * @params [userId]
     * @return int
     *
     **/
    @Override
    public int selectTotalBlogNums(int userId) {
        Object myblogsTotal = null;
        try {
            myblogsTotal=redisUtil.get(myConstant.getUserTotalBlogs()+userId);
            if(myblogsTotal == null){
                log.info("初始化->selectTotalBlogNums");
                redisUtil.set(myConstant.getUserTotalBlogs()+userId, blogMapper.selectTotalBlogNums(userId));
                myblogsTotal=redisUtil.get(myConstant.getUserTotalBlogs()+userId);
            }
            log.info("selectTotalBlogNums-> nums:"+(int) myblogsTotal);
        }
        catch (Exception e){
            log.error(e);
        }
        return myblogsTotal== null ? -1 : (int)myblogsTotal;
    }


    /**
     * todo 未使用
     * @author ZouCha
     * @date 2020-11-20 15:26:32
     * @method selectBlogByCategoryIdAndPage
     * @params [userId, categoryId, pageNow, pageSize]
     * @return java.util.ArrayList<com.shiyifan.pojo.Myblog>
     *
     **/
    @Override
    public ArrayList<Myblog> selectBlogByCategoryIdAndPage(int userId, int categoryId, int pageNow, int pageSize) {
        return blogMapper.selectBlogByCategoryIdAndPage(userId, categoryId,pageNow,pageSize);
    }
    /**
     *
     * @author ZouCha
     * @date 2020-11-20 15:27:31
     * @method getTempBlog
     * @params [userId]
     * @return com.shiyifan.pojo.Myblog
     *
     **/
    @Override
    public Myblog getTempBlog(int userId) {
        Myblog myblog =null;
        try {
            myblog = (Myblog) redisUtil.get(myConstant.getUserTempBlog()+userId);
            if(myblog==null){
                myblog=new Myblog();
            }
            redisUtil.del(myConstant.getUserTempBlog()+userId);
            log.info("读取暂存博客成功");
        }
        catch (Exception e){
            log.error(e);
        }
        return myblog;
    }

    /*---------------------------------------------------------------------------*/

    /*------------------------------公共操作-------------------------------*/

    /**
     *
     * @author ZouCha
     * @date 2020-11-20 15:27:27
     * @method setTempBlog
     * @params [myblog]
     * @return java.lang.Boolean
     *
     **/
    @Override
    public Boolean setTempBlog(Myblog myblog) {
        try {
            redisUtil.set(myConstant.getUserTempBlog()+myblog.getMyuser().getUserId(), myblog);
            log.info("添加暂存博客成功");
            return true;
        }
        catch (Exception e){
            log.error(e);
            return false;
        }

    }

    /**
     *
     * @author ZouCha
     * @date 2020-11-20 15:27:39
     * @method selectBlogByIdForCommon
     * @params [blogId]
     * @return com.shiyifan.pojo.Myblog
     *
     **/
    @Override
    public Myblog selectBlogByIdForCommon(String blogId) {
        Myblog myblog =null;
        try {
            myblog = (Myblog) redisUtil.get(blogId);
            if(myblog==null){
                log.info("初始化->selectBlogByIdForCommon");
                redisUtil.set(blogId, blogMapper.selectBlogByIdForCommon(blogId));
                myblog = (Myblog) redisUtil.get(blogId);
            }
            log.info("selectBlogByIdForCommon-> blogId:"+ blogId);
        }
        catch (Exception e) {
            log.error(e);
        }
        return myblog;
    }
    /**
     *
     * @author ZouCha
     * @date 2020-11-20 15:27:45
     * @method selectBlogAllByPageForCommon
     * @params [categoryId, pageNow, pageSize]
     * @return java.util.ArrayList<com.shiyifan.pojo.Myblog>
     *
     **/
    @Override
    public ArrayList<Myblog> selectBlogAllByPageForCommon(int categoryId, int pageNow, int pageSize) {
        ArrayList<Myblog> myblogs=new ArrayList<>();
        try {
            for (int i = 1; i <= pageNow; i++) {
                int start = (i-1)*pageSize;
                int end = (i*pageSize)-1;
                ArrayList<Object> blogs=null;
                if(categoryId == 0){
                    blogs = (ArrayList<Object>) redisUtil.lGet(myConstant.getBlogsForCommon(), start, end);
                }
                else {
                    blogs = (ArrayList<Object>) redisUtil.lGet(myConstant.getCategoryBlogsForCommon()+categoryId, start, end);
                }
                if(blogs==null || blogs.size()==0){
                    log.info("初始化->selectBlogAllByPageForCommon");
                    Iterator<Myblog> iterator = blogMapper.selectBlogAllForCommon(0).iterator();
                    while (iterator.hasNext()){
                        redisUtil.RSet(myConstant.getBlogsForCommon(), iterator.next());
                    }
                    blogs= (ArrayList<Object>) redisUtil.lGet(myConstant.getBlogsForCommon(), start, end);
                }
                myblogs.addAll((ArrayList<Myblog>)(Object)blogs);
                log.info("selectBlogAllByPageForCommon-> categoryId:"+ categoryId +"<=>"+"start:"+start+"<=>"+"end:"+end);
            }
        }
        catch (Exception e){
            log.error(e);
        }
        return myblogs;
    }
    /**
     *
     * @author ZouCha
     * @date 2020-11-20 15:27:49
     * @method selectTotalBlogNumsForCommon
     * @params [categoryId]
     * @return int
     *
     **/
    @Override
    public int selectTotalBlogNumsForCommon(int categoryId) {
        Object myblogsTotal =null;
        try {
            if(categoryId == 0){
                myblogsTotal = redisUtil.get(myConstant.getTotalBlogsForCommon());
            }
            else {
                myblogsTotal = redisUtil.get(myConstant.getCategoryTotalBlogsForCommon()+categoryId);
            }
            if(myblogsTotal == null){
                log.info("初始化->selectTotalBlogNumsForCommon");
                redisUtil.set(myConstant.getTotalBlogsForCommon(), blogMapper.selectTotalBlogNumsForCommon(0));
                myblogsTotal=redisUtil.get(myConstant.getTotalBlogsForCommon());
            }
            log.info("selectTotalBlogNumsForCommon-> categoryId:"+ categoryId +"<=>"+"nums:"+myblogsTotal);
        }
        catch (Exception e){
            log.error(e);
        }
        return myblogsTotal == null ? -1 : (int)myblogsTotal;
    }
    /**
     * todo
     * @author ZouCha
     * @date 2020-11-20 15:27:52
     * @method selectBlogByAuthorForCommon
     * @params [userId, pageNow, pageSize]
     * @return java.util.ArrayList<com.shiyifan.pojo.Myblog>
     *
     **/
    @Override
    public ArrayList<Myblog> selectBlogByAuthorForCommon(int userId, int pageNow, int pageSize) {
        int start = (pageNow-1)*pageSize;
        return blogMapper.selectBlogByAuthorForCommon(userId,start,pageSize);
    }

    /*---------------------------------------------------------------------------*/

    /*------------------------------搜索操作-------------------------------*/

    /**
     *
     * @author ZouCha
     * @date 2020-11-20 15:28:01
     * @method searchContentPage
     * @params [keyword, pageNow, pageSize]
     * @return java.util.ArrayList<java.util.Map<java.lang.String,java.lang.Object>>
     *
     **/
    @Override
    @Retryable(value = IOException.class)
    public ArrayList<Map<String, Object>> searchContentPage(String keyword, int pageNow, int pageSize) throws IOException {
        int start = (pageNow-1)*pageSize;
        ArrayList<Map<String, Object>> list = new ArrayList<>();
        try {
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
        catch (Exception e){
            log.error(e);
            throw new IOException();
        }
    }

    /*---------------------------------------------------------------------------*/

    /**
     *
     * @author ZouCha
     * @date 2020-11-20 15:28:11
     * @method refreshDcdn
     * @params []
     * @return void
     *
     **/
    public void refreshDcdn(){
        DefaultProfile profile = DefaultProfile.getProfile("cn-hangzhou", myConstant.getAccessKeyId(), myConstant.getAccessKeySecret());
        IAcsClient client = new DefaultAcsClient(profile);
        RefreshDcdnObjectCachesRequest request = new RefreshDcdnObjectCachesRequest();
        request.setObjectPath("https://www.chardance.cloud/#/index/bloglist/all/all");
        try {
            RefreshDcdnObjectCachesResponse response = client.getAcsResponse(request);
            log.warn(new Gson().toJson(response));
        } catch (ClientException e) {
            log.error("ErrCode:" + e.getErrCode());
            log.error("ErrMsg:" + e.getErrMsg());
            log.error("RequestId:" + e.getRequestId());
        }
    }
    /**
     *
     * @author ZouCha
     * @date 2020-11-20 15:28:15
     * @method preLoadDcdn
     * @params []
     * @return void
     *
     **/
    public void preLoadDcdn(){
        DefaultProfile profile = DefaultProfile.getProfile("cn-hangzhou", myConstant.getAccessKeyId(), myConstant.getAccessKeySecret());
        IAcsClient client = new DefaultAcsClient(profile);
        PreloadDcdnObjectCachesRequest request = new PreloadDcdnObjectCachesRequest();
        request.setObjectPath("https://www.chardance.cloud/#/index/bloglist/all/all");

        try {
            PreloadDcdnObjectCachesResponse response = client.getAcsResponse(request);
            log.warn(new Gson().toJson(response));
        } catch (ClientException e) {
            log.error("ErrCode:" + e.getErrCode());
            log.error("ErrMsg:" + e.getErrMsg());
            log.error("RequestId:" + e.getRequestId());
        }
    }
    /**
     *
     * @author ZouCha
     * @date 2020-11-20 15:28:20
     * @method run
     * @params [args]
     * @return void
     *
     **/
    @Override
    public void run(ApplicationArguments args){
//        log.info("<--------------------初始化redis-------------------->");
//        try {
//            redisUtil.flushDb();
//            ArrayList<Mycategory> mycategories = categoryMapper.selectAllCategoryForCommon();
//            Iterator<Mycategory> iterator = mycategories.iterator();
//            while (iterator.hasNext()){
//                Mycategory mycategory = iterator.next();
//                Iterator<Myblog> iteratorMyblog = blogMapper.selectBlogAllForCommon(mycategory.getCategoryId()).iterator();
//                while (iteratorMyblog.hasNext()){
//                    redisUtil.RSet(myConstant.getCategoryBlogsForCommon()+mycategory.getCategoryId(), iteratorMyblog.next());
//                }
//                redisUtil.set(myConstant.getCategoryTotalBlogsForCommon()+mycategory.getCategoryId(), blogMapper.selectTotalBlogNumsForCommon(mycategory.getCategoryId()));            }
//            refreshDcdn();
//            preLoadDcdn();
//        }
//        catch (Exception e){
//            log.error(e);
//        }
    }
}

