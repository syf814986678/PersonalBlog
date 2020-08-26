//package com.shiyifan;
//
//import com.aliyun.oss.OSS;
//import com.aliyun.oss.OSSClientBuilder;
//import com.aliyun.oss.model.ListObjectsRequest;
//import com.aliyun.oss.model.OSSObjectSummary;
//import com.aliyun.oss.model.ObjectListing;
//import com.shiyifan.constant.MyConstant;
//import com.shiyifan.dao.BlogMapper;
//import com.shiyifan.dao.CategoryMapper;
//import com.shiyifan.pojo.Myblog;
//import com.shiyifan.pojo.Mycategory;
//import com.shiyifan.pojo.Myuser;
//import com.shiyifan.service.BlogService;
//import com.shiyifan.service.CategoryService;
//import com.shiyifan.service.UserService;
//import com.shiyifan.utils.JwtUtil;
//import com.shiyifan.utils.RedisUtil;
//import io.jsonwebtoken.Claims;
//import org.jasypt.encryption.StringEncryptor;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import java.util.List;
//import java.util.UUID;
//
//@SpringBootTest
//class MyblogAfterendApplicationTests {
//
//    @Autowired
//    private UserService userService;
//
//    @Autowired
//    private CategoryService categoryService;
//
//    @Autowired
//    private BlogService blogService;
//
//    @Autowired
//    private CategoryMapper categoryMapper;
//
//    @Autowired
//    private BlogMapper blogMapper;
//
//    @Autowired
//    private JwtUtil jwtUtil;
//
//    @Test
//    public void testaddBlog(){
//        Mycategory mycategory = new Mycategory();
//        Myuser myuser = new Myuser();
//        myuser.setUserId(1);
//        mycategory.setCategoryId(3);
//        Myblog myblog = new Myblog(UUID.randomUUID().toString().replaceAll("-", ""), "谈情说案啊", "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1594194350583&di=124002a777bf3d3b60aa795fb95805b6&imgtype=0&src=http%3A%2F%2Fa2.att.hudong.com%2F86%2F10%2F01300000184180121920108394217.jpg", "谈情说案啊",mycategory , myuser, 0, null, null);
//        blogService.addBlog(myblog);
//    }
//
//    @Test
//    public void testselectBlogById(){
//        Myblog myblog = blogService.selectBlogById(1,"a4d4847bffaa4ed5ae57cb22f2798e43");
//        System.out.println(myblog);
//    }
//
////    @Test
////    public void testdeleteBlogById(){
////        blogService.deleteBlogById(2,"e2742b4f484d4808bbe6746b0e36e580");
////    }
//
//    @Test
//    public void testupdateBlog(){
//        Myblog myblog = new Myblog();
//        Mycategory mycategory = new Mycategory();
//        Myuser myuser = new Myuser();
//        myuser.setUserId(1);
//        mycategory.setCategoryId(2);
//        myblog.setBlogId("a7980f41adbd46c082fe194578fb4ac0");
//        myblog.setMyuser(myuser);
//        myblog.setBlogContent("更新博客测试");
//        myblog.setBlogTitle("更新博客测试");
//        myblog.setMycategory(mycategory);
//        blogService.updateBlog(myblog);
//    }
//
//    @Test
//    public void testselectBlogByPage(){
//        List<Myblog> myblogs = blogService.selectBlogByPage(1, 1, 5);
//        System.out.println(myblogs);
//    }
//
//    @Test
//    public void testselectTotalBlogNums(){
//        int i = blogService.selectTotalBlogNums(1);
//        System.out.println(i);
//    }
//
//    @Test
//    public void testselectBlogByCategoryId(){
//        List<Myblog> myblogs = blogService.selectBlogByCategoryIdAndPage(1, 2, 0, 2);
//        System.out.println(myblogs);
//    }
//
//    @Test
//    public void testselectLastestSixBlog(){
//        List<Myblog> myblogs = blogService.selectLastestSixBlog(1);
//        System.out.println(myblogs);
//    }
//
//    @Test
//    public void testselectAllCategoryForBlog(){
//        List<Mycategory> mycategories = categoryService.selectAllCategoryForBlog(1);
//        System.out.println(mycategories);
//    }
//
//    @Test
//    public void testselectBlogByIdForCommon(){
//        Myblog myblog = blogService.selectBlogByIdForCommon("447afce8ca9e4e199ef5dc26c8084256");
//        System.out.println(myblog);
//    }
//
//    @Test
//    public void testselectLastestBlogByPagForCommon(){
//        List<Myblog> myblogs =null;
//        for(int i=1;i<10;i++){
//            myblogs = blogService.selectLastestBlogByPagForCommon(i, 5);
//            System.out.println("====================================================================第"+i+"次");
//            System.out.println(myblogs);
//        }
//
//    }
//
//    @Test
//    public void testselectAllCategoryByPage(){
//        List<Mycategory> mycategories = categoryService.selectAllCategoryByPage(1, 1, 12);
//        System.out.println(mycategories);
//    }
//
//    @Test
//    public void testselectTotalCategoryNums(){
//        int i = categoryService.selectTotalCategoryNums(1);
//        System.out.println(i);
//    }
//
//    @Test
//    public void testaddCategory(){
//        categoryService.addCategory(1,"测试");
//    }
//
//    @Test
//    public void testselectCategoryById(){
//        Mycategory mycategory = categoryService.selectCategoryById(1, 1);
//        System.out.println(mycategory);
//    }
//
////    @Test
////    public void testcategoryIsUsed(){
//////        int i = categoryMapper.categoryUsedNum(1, 2);
//////        System.out.println("testcategoryIsUsed======="+i);
////        Boolean isUsed = categoryService.categoryIsUsed(1, 17);
////        System.out.println("testcategoryIsUsed======="+isUsed);
////    }
//
//
//
//    @Test
//    public void testlogin(){
//        Myuser login = userService.login("guest", "guest");
//        System.out.println(login);
//    }
//
//    @Test
//    public void testselectBlogByCategoryIdAndPageForCommon(){
//        List<Myblog> myblogs = blogService.selectBlogByCategoryIdAndPageForCommon(3, 1, 5);
//        System.out.println(myblogs);
//    }
//
//    @Test
//    public void testselectTotalBlogNumsForCommon(){
//        System.out.println(blogService.selectTotalBlogNumsForCommon());
//    }
//
//    @Test
//    public void testselectAllCategoryForCommon(){
//        System.out.println(categoryService.selectAllCategoryForCommon());
//    }
//
//    @Test
//    public void testtoken(){
//        String token = jwtUtil.createToken(1, "syf");
//        System.out.println(token);
//    }
//    /*
//eyJhbGciOiJIUzI1NiJ9.eyJ1c2VyaWQiOjEsInVzZXJuYW1lIjoic3lmIiwiaWF0IjoxNTk0ODE4OTUwLCJleHAiOjE1OTQ4MTk1NTB9.BjQgMig4njAjYK1EU8thjE7YZdHyGKVmW-orOxoSiEU
//    */
//
//    @Test
//    public void testparsetoken(){
//        //eyJhbGciOiJIUzI1NiJ9.eyJ1c2VyaWQiOjEsInVzZXJuYW1lIjoiYWRtaW4iLCJpYXQiOjE1OTY5NzcwNDYsImV4cCI6MTU5Njk4NDI0Nn0.TA_b-QY54qSqy0idDof1iFnNpsXNpeDtvDEcNG3fibM
//        //eyJhbGciOiJIUzI1NiJ9.eyJ1c2VyaWQiOjEsInVzZXJuYW1lIjoiYWRtaW4iLCJpYXQiOjE1OTY5NzcwNDYsImV4cCI6MTU5Njk4NDI0Nn0.TA_b-QY54qSqy0idDof1iFnNpsXNpeDtvDEcNG3fibM
//        Claims claims = jwtUtil.parseToken("eyJhbGciOiJIUzI1NiJ9.eyJ1c2VyaWQiOjEsInVzZXJuYW1lIjoiYWRtaW4iLCJpYXQiOjE1OTY5NzcwNDYsImV4cCI6MTU5Njk4NDI0Nn0.TA_b-QY54qSqy0idDof1iFnNpsXNpeDtvDEcNG3fibM");
//        System.out.println(claims);
//    }
//
//    @Test
//    public void testaddCategoryRank(){
//        categoryMapper.addCategoryRank(4, 26);
//    }
//
//    @Test
//    public void testdeleteCategoryRank(){
//        categoryMapper.deleteCategoryRank(4, 26);
//    }
//
//    @Autowired
//    private MyConstant myConstant;
//
//    @Test
//    public void testpicture(){
//        OSS ossClient = new OSSClientBuilder().build(myConstant.getEndpoint(), myConstant.getAccessKeyId(), myConstant.getAccessKeySecret());
//        // 列举文件。 如果不设置KeyPrefix，则列举存储空间下所有的文件。KeyPrefix，则列举包含指定前缀的文件。
//        ListObjectsRequest listObjectsRequest = new ListObjectsRequest();
//        listObjectsRequest.setBucketName(myConstant.getBucket());
//        listObjectsRequest.setPrefix("myblog/Random/");
//        ObjectListing objectListing = ossClient.listObjects(listObjectsRequest);
//        //1+Math.random()*10
//        List<OSSObjectSummary> sums = objectListing.getObjectSummaries();
//        System.out.println(sums.size());
//        for (OSSObjectSummary s : sums) {
//            System.out.println("\t" + s.getKey());
//        }
//        for (int j=0;j<10;j++){
//            int i = (int) (1 + Math.random() * (sums.size()-1));
//            System.out.println(i);
//            System.out.println(sums.get(i).getKey());
//        }
//
//        // 关闭OSSClient。
//        ossClient.shutdown();
//    }
//
//    @Test
//    public void testselectBlogByAuthor(){
////        System.out.println(blogMapper.selectBlogByAuthor(1, 0, 5));
//    }
//
//    @Autowired
//    private RedisUtil redisUtil;
//
//    @Test
//    public void testredis(){
//
////        List<Myblog> myblogList = blogService.selectBlogAll(1);
////        Iterator<Myblog> myblogIterator = myblogList.iterator();
////        while (myblogIterator.hasNext()){
////            redisUtil.RSet("myblogs", myblogIterator.next());
////        }
////        0 4         1 5
////        5 9         2 5
////        10 14       3 5
////        增删改查
////        √ √ √ √
//        List<Object> myblogs = redisUtil.lGet("myblogs", 5, 9);
//        System.out.println(myblogs.size());
////        Iterator<Object> iterator = myblogs.iterator();
////        while (iterator.hasNext()){
////            System.out.println("=============================================================");
////            Myblog myblog = (Myblog) iterator.next();
////            System.out.println(myblog.getBlogId());
////            System.out.println(myblog);
////        }
//
//
//
//
//////        HashMap<String, Object> map1 = new HashMap<>();
//////        map1.put("myblogs",myblogList);
//////        redisUtil.hmset("myblogs", map1);
//////        Map<Object, Object> map =redisUtil.hmget("myblogs");
////        List<Myblog> myblogs = (List<Myblog>) redisUtil.hmget("myblogs").get("myblogs");
////        System.out.println(myblogs.size());
////        Iterator<Myblog> iterator = myblogs.iterator();
////        while(iterator.hasNext()){
////            System.out.println("===================================================================");
////            System.out.println(iterator.next());
////        }
//    }
//
//    @Autowired
//    private StringEncryptor stringEncryptor;
//    @Test
//    public void testencoding(){
//        String yuanwen="hepIuLXr5yv3vpSsDCYjMKe36LeoMJ";
//        String miwen = stringEncryptor.encrypt(yuanwen) ;
//        System.out.println(miwen);
//        System.out.println(stringEncryptor.decrypt(miwen));
//
//    }
//}
