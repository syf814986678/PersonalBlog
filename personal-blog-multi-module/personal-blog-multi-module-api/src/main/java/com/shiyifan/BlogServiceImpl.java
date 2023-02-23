package com.shiyifan;


import com.shiyifan.mapper.BlogMapper;
import com.shiyifan.pojo.Blog;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;

/**
 * @author ZouCha
 * @name BlogService
 * @date 2020-11-20 15:23:33
 **/
@Service
@Order
@Log4j2
public class BlogServiceImpl implements BlogService {

    @Autowired
    private BlogMapper blogMapper;

    @Autowired
    private BlogUtil blogUtil;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ArabicNumToChineseNumUtil arabicNumToChineseNumUtil;

    /**
     * 根据种类ID和分页数据查找博客
     * pageNow：必须≥1
     *
     * @return java.util.ArrayList<com.shiyifan.pojo.Blog>
     * @author ZouCha
     * @date 2020-12-05 13:49:46
     * @method selectBlogListByPageForCommon
     * @params [categoryId, pageNow, pageSize]
     **/
    @Override
    public ArrayList<Blog> selectBlogListByPageForCommon(int categoryId, int pageNow, int pageSize) throws Exception {
        log.info("方法:selectBlogListByPageForCommon开始,categoryId:" + categoryId + ",pageNow:" + pageNow + ",pageSize:" + pageSize);
        ArrayList<Blog> blogListForCommon = new ArrayList<>();
        try {
            for (int i = 1; i <= pageNow; i++) {
                int start = (i - 1) * pageSize;
                int end = (i * pageSize) - 1;
                ArrayList<Blog> blogListInRedis;
                if (categoryId == 0) {
                    blogListInRedis = blogUtil.getBlogListByPageForCommon(start, end);
                    if (blogListInRedis.size() == 0) {
                        blogUtil.initBlogListForCommon();
                        blogListInRedis = blogUtil.getBlogListByPageForCommon(start, end);
                    }
                } else {
                    blogListInRedis = blogUtil.getCategoryBlogListByPageForCommon(categoryId, start, end);
                    if (blogListInRedis.size() == 0) {
                        blogUtil.initCategoryBlogListForCommon();
                        blogListInRedis = blogUtil.getCategoryBlogListByPageForCommon(categoryId, start, end);
                    }
                }
                blogListForCommon.addAll((ArrayList<Blog>) (Object) blogListInRedis);
            }
        } catch (Exception e) {
            log.error("分页查找公共博客错误" + e.toString());
            throw new Exception("分页查找公共博客错误" + e.toString());
        }
        return blogListForCommon;
    }

    /**
     * @return java.lang.Integer
     * @author ZouCha
     * @date 2020-12-05 15:45:03
     * @method selectTotalBlogsForCommon
     * @params [categoryId]
     **/
    @Override
    public Integer selectTotalBlogsForCommon(int categoryId) throws Exception {
        log.info("方法:selectTotalBlogsForCommon开始,categoryId:" + categoryId);
        Integer totalBlogsForCommon;
        try {
            if (categoryId == 0) {
                totalBlogsForCommon = blogUtil.getTotalBlogsForCommon();
                if (totalBlogsForCommon == null) {
                    blogUtil.initBlogListForCommon();
                    totalBlogsForCommon = blogUtil.getTotalBlogsForCommon();
                }
            } else {
                totalBlogsForCommon = blogUtil.getCategoryTotalBlogsForCommon(categoryId);
                if (totalBlogsForCommon == null) {
                    blogUtil.initCategoryBlogListForCommon();
                    totalBlogsForCommon = blogUtil.getCategoryTotalBlogsForCommon(categoryId);
                }
            }
        } catch (Exception e) {
            log.error("分页查找公共博客数量错误" + e.toString());
            throw new Exception("分页查找公共博客数量错误" + e.toString());
        }
        return totalBlogsForCommon;
    }

    /**
     * @return com.shiyifan.pojo.Blog
     * @author ZouCha
     * @date 2020-12-07 18:33:34
     * @method selectBlogByIdForCommon
     * @params [blogId]
     **/
    @Override
    public Blog selectBlogByIdForCommon(String blogId) throws Exception {
        log.info("方法:selectBlogByIdForCommon开始,blogId:" + blogId);
        Blog blog = null;
        try {
            blog = blogUtil.getBlogForCommon(blogId);
            if (blog == null) {
                blogUtil.setBlogForCommon(blogId);
                blog = blogUtil.getBlogForCommon(blogId);
            }
        } catch (Exception e) {
            log.error("根据ID查找公共博客错误" + e.toString());
            throw new Exception("根据ID查找公共博客错误" + e.toString());
        }
        return blog;
    }

    /**
     * @return java.util.ArrayList<java.util.Map < java.lang.String, java.lang.Object>>
     * @author ZouCha
     * @date 2020-12-19 16:03:07
     * @method searchContentByPageForCommon
     * @params [keyword, pageNow, pageSize]
     **/
    @Override
    public ArrayList<Map<String, Object>> searchContentByPageForCommon(String keyword, int pageNow, int pageSize) throws IOException {
        log.info("方法:searchContentByPageForCommon开始,keyword:" + keyword + ",pageNow:" + pageNow + ",pageSize:" + pageSize);
        ArrayList<Map<String, Object>> list = null;
        try {
            list = blogUtil.searchContentByPageForCommon(keyword, pageNow, pageSize);
        } catch (IOException e) {
            log.error("searchContentByPageForCommon错误" + e.toString());
            throw new IOException("searchContentByPageForCommon错误" + e.toString());
        }
        return list;
    }

    /**
     * @return java.util.ArrayList<com.shiyifan.pojo.Blog>
     * @author ZouCha
     * @date 2020-12-19 16:19:34
     * @method selectBlogByPageForAdmin
     * @params [userId, pageNow, pageSize]
     **/
    @Override
    public ArrayList<Blog> selectBlogListByPageForAdmin(int userId, int categoryId, int pageNow, int pageSize) throws Exception {
        log.info("方法:selectBlogListByPageForAdmin开始,userId:" + userId + ",categoryId:" + categoryId + ",pageNow:" + pageNow + ",pageSize:" + pageSize);
        int start = (pageNow - 1) * pageSize;
        int end = (pageNow * pageSize) - 1;
        ArrayList<Blog> blogs = null;
        try {
            if (categoryId == 0) {
                blogs = blogUtil.getBlogListByPageForAdmin(userId, start, end);
                if (blogs.size() == 0) {
                    blogUtil.initBlogListForAdmin(userId);
                    blogs = blogUtil.getBlogListByPageForAdmin(userId, start, end);
                }
            } else {
                blogs = blogUtil.getCategoryBlogListByPageForAdmin(userId, categoryId, start, end);
                if (blogs.size() == 0) {
                    blogUtil.initCategoryBlogListForAdmin(userId);
                    blogs = blogUtil.getCategoryBlogListByPageForAdmin(userId, categoryId, start, end);
                }
            }
        } catch (Exception e) {
            log.error("selectBlogListByPageForAdmin错误" + e.toString());
            throw new Exception("selectBlogListByPageForAdmin错误" + e.toString());
        }
        return blogs;
    }

    /**
     * @return java.lang.Integer
     * @author ZouCha
     * @date 2020-12-19 16:12:32
     * @method selectTotalBlogsForAdmin
     * @params [userId]
     **/
    @Override
    public Integer selectTotalBlogsForAdmin(int userId, int categoryId) throws Exception {
        log.info("方法:selectTotalBlogsForAdmin开始,userId:" + userId + ",categoryId:" + categoryId);
        Integer totalBlogsForAdmin = null;
        try {
            if (categoryId == 0) {
                totalBlogsForAdmin = blogUtil.getTotalBlogsForAdmin(userId);
                if (totalBlogsForAdmin == null) {
                    blogUtil.initBlogListForAdmin(userId);
                    totalBlogsForAdmin = blogUtil.getTotalBlogsForAdmin(userId);
                }
            } else {
                totalBlogsForAdmin = blogUtil.getCategoryTotalBlogsForAdmin(userId, categoryId);
                if (totalBlogsForAdmin == null) {
                    blogUtil.initCategoryBlogListForAdmin(userId);
                    totalBlogsForAdmin = blogUtil.getCategoryTotalBlogsForAdmin(userId, categoryId);
                }
            }
        } catch (Exception e) {
            log.error("selectTotalBlogsForAdmin错误" + e.toString());
            throw new Exception("selectTotalBlogsForAdmin错误" + e.toString());
        }
        return totalBlogsForAdmin;
    }

    /**
     * @return void
     * @author ZouCha
     * @date 2020-12-30 09:39:17
     * @method setTempBlogForAdmin
     * @params [blog]
     **/
    @Override
    public void setTempBlogForAdmin(int userId, Blog blog) throws Exception {
        log.info("方法:setTempBlogForAdmin开始,userId:" + userId + ",blogId:" + blog.getBlogId());
        try {
            blogUtil.setTempBlogForAdmin(userId, blog);
        } catch (Exception e) {
            log.error("setTempBlogForAdmin错误" + e.toString());
            throw new Exception("setTempBlogForAdmin错误" + e.toString());
        }

    }

    /**
     * @return com.shiyifan.pojo.Blog
     * @author ZouCha
     * @date 2020-12-30 10:12:43
     * @method getTempBlogForAdmin
     * @params [userId]
     **/
    @Override
    public Blog getTempBlogForAdmin(int userId) {
        log.info("方法:getTempBlogForAdmin开始,userId:" + userId);
        return blogUtil.getTempBlogForAdmin(userId);
    }

    /**
     * @return java.lang.Boolean
     * @author ZouCha
     * @date 2020-12-30 10:12:50
     * @method addBlogForAdmin
     * @params [userId, blog]
     **/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean addBlogForAdmin(int userId, Blog blog) throws Exception {
        log.info("方法:addBlogForAdmin开始,userId:" + userId + ",blogId:" + blog.getBlogId());
        try {
            blog.setBlogId(UUID.randomUUID().toString().replaceAll("-", ""));
            Integer categoryRankForAdmin = categoryService.getCategoryRankForAdmin(userId, blog.getCategory().getCategoryId());
            blog.setBlogTitle(blog.getBlogTitle() + "(" + arabicNumToChineseNumUtil.arabicNumToChineseNum(++categoryRankForAdmin) + ")");
            blogMapper.addBlogForAdmin(userId, blog);
            categoryService.addCategoryRankForAdmin(userId, blog.getCategory().getCategoryId());
            blogUtil.setBlogToRedisAndElasticSearchForAdmin(userId, blog.getBlogId());
            blogUtil.cleanTempBlogForAdmin(userId);
        } catch (Exception e) {
            log.error("addBlogForAdmin错误" + e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            throw new Exception("addBlogForAdmin错误" + e);
        }
        return true;
    }

    /**
     * @return java.lang.Boolean
     * @author ZouCha
     * @date 2020-12-30 14:50:35
     * @method deleteBlogForAdmin
     * @params [userId, blogId, categoryId]
     **/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteBlogForAdmin(int userId, String blogId, int categoryId) throws Exception {
        log.info("方法:deleteBlogForAdmin开始,userId:" + userId + ",blogId:" + blogId + ",categoryId" + categoryId);
        try {
            blogUtil.deleteBlogInRedisAndElasticSearchForAdmin(userId, blogId);
            blogMapper.deleteBlogByIdForAdmin(userId, blogId);
            categoryService.deleteCategoryRankForAdmin(userId, categoryId);
        } catch (Exception e) {
            log.error("deleteBlogForAdmin错误" + e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            throw new Exception("deleteBlogForAdmin错误" + e);
        }
        return true;
    }

    /**
     * @return java.lang.Boolean
     * @author ZouCha
     * @date 2021-01-02 18:16:29
     * @method updateBlogForAdmin
     * @params [userId, blog]
     **/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateBlogForAdmin(int userId, Blog blog) throws Exception {
        log.info("方法:updateBlogForAdmin开始,userId:" + userId + ",blogId:" + blog.getBlogId());
        try {
            Integer categoryIdForAdminInDb = categoryService.getCategoryIdForAdmin(userId, blog.getBlogId());
            if (categoryIdForAdminInDb == null) {
                return false;
            }
            if (categoryIdForAdminInDb != blog.getCategory().getCategoryId()) {
                categoryService.deleteCategoryRankForAdmin(userId, categoryIdForAdminInDb);
                categoryService.addCategoryRankForAdmin(userId, blog.getCategory().getCategoryId());
                Integer categoryRankForAdmin = categoryService.getCategoryRankForAdmin(userId, blog.getCategory().getCategoryId());
                blog.setBlogTitle(blog.getBlogTitle().replace(blog.getBlogTitle().substring(blog.getBlogTitle().indexOf("(")), "")
                        + "(" + arabicNumToChineseNumUtil.arabicNumToChineseNum(++categoryRankForAdmin) + ")");
            }
            blogMapper.updateBlogByIdForAdmin(userId, blog);
            Blog newBlog = blogMapper.selectBlogForRedisForAdmin(userId, blog.getBlogId());
            blogUtil.updateBlogInRedisAndElasticSearchForAdmin(userId, newBlog);
        } catch (Exception e) {
            log.error("updateBlogForAdmin错误" + e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            throw new Exception("updateBlogForAdmin错误" + e);
        }
        return true;
    }

    /**
     * @return java.lang.Boolean
     * @author user
     * @date 2021-01-05 15:06:50
     * @method updateBlogForAdmin
     * @params [userId, blogId]
     **/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateBlogForAdmin(int userId, String blogId) throws Exception {
        log.info("方法:updateBlogForAdmin开始,userId:" + userId + ",blogId:" + blogId);
        try {
            Blog newBlog = blogMapper.selectBlogForRedisForAdmin(userId, blogId);
            blogUtil.updateBlogInRedisAndElasticSearchForAdmin(userId, newBlog);
        } catch (Exception e) {
            log.error("updateBlogForAdmin错误" + e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            throw new Exception("updateBlogForAdmin错误" + e);
        }
        return true;
    }

    /**
     * @return com.shiyifan.pojo.Blog
     * @author ZouCha
     * @date 2021-01-02 18:16:36
     * @method selectBlogByIdForAdmin
     * @params [userId, blogId]
     **/
    @Override
    public Blog selectBlogByIdForAdmin(int userId, String blogId) throws Exception {
        log.info("方法:selectBlogByIdForAdmin开始,userId:" + userId + ",blogId:" + blogId);
        Blog blog = null;
        try {
            blog = blogMapper.selectBlogByIdForAdmin(userId, blogId);
        } catch (Exception e) {
            log.error("selectBlogByIdForAdmin错误" + e.toString());
            throw new Exception("selectBlogByIdForAdmin错误" + e.toString());
        }
        return blog;
    }

    /**
     * @return java.util.ArrayList<java.lang.String>
     * @author user
     * @date 2021-01-05 15:15:19
     * @method selectBlogIdByCategoryIdForAdmin
     * @params [userId, categoryId]
     **/
    @Override
    public ArrayList<String> selectBlogIdByCategoryIdForAdmin(int userId, int categoryId) throws Exception {
        log.info("方法:selectBlogIdByCategoryIdForAdmin开始,userId:" + userId + ",categoryId:" + categoryId);
        ArrayList<String> blogId = null;
        try {
            blogId = blogMapper.selectBlogIdByCategoryIdForAdmin(userId, categoryId);
        } catch (Exception e) {
            log.error("selectBlogIdByCategoryIdForAdmin错误" + e.toString());
            throw new Exception("selectBlogIdByCategoryIdForAdmin错误" + e.toString());
        }
        return blogId;
    }

    /**
     * @param userId:
     * @param blogId:
     * @return java.lang.String
     * @author 走叉
     * @date 2023-02-22 14:01:10
     * @method downloadBlog2Markdown
     **/
    @Override
    public String downloadBlog2MarkdownForCommon(int userId, String blogId) throws Exception {
        try {
            String filePath = "C:\\Users\\走叉\\Documents\\Code\\JAVA\\PersonalBlog\\personal-blog-multi-module\\" + blogId + ".md";
            Blog blog = this.selectBlogByIdForCommon(blogId);
            File file = new File(filePath);
            if (file.exists()) {
                file.delete();
            }
            file.createNewFile();
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filePath), StandardCharsets.UTF_8));
            out.write(blog.toString());
            out.close();
        } catch (Exception e) {
            log.error(e);
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
        return "1";
    }

}
