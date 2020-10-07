package com.shiyifan;

import com.shiyifan.dao.BlogMapper;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.jasypt.encryption.StringEncryptor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@SpringBootTest
class MyblogAfterendApplicationTests {

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    @Autowired
    private BlogMapper blogMapper;
    @Test
    public void test() throws IOException {
        SearchRequest searchRequest = new SearchRequest("blogindex");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.multiMatchQuery("leetcode题解", "blogTitle","blogContent,blogCategory").analyzer("ik_max_word"))
                .highlighter(new HighlightBuilder().field("*").requireFieldMatch(false).preTags("<span style=\"color:red;font-weight:bold\">").postTags("</span>"));
        searchSourceBuilder.timeout(new TimeValue(60, TimeUnit.SECONDS));
        searchRequest.source(searchSourceBuilder);
        SearchResponse search = null;
        try {
            search = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
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
        System.out.println(list.toString());
    }

//    @Test
//    public void testindex() throws IOException {
//        CreateIndexRequest testindex = new CreateIndexRequest("testindex");
//        CreateIndexResponse createIndexResponse = restHighLevelClient.indices().create(testindex, RequestOptions.DEFAULT);
//        System.out.println(createIndexResponse);

//        GetIndexRequest test_index = new GetIndexRequest("test_index");
//        boolean exists = restHighLevelClient.indices().exists(test_index, RequestOptions.DEFAULT);
//        System.out.println(exists);

//        DeleteIndexRequest test_index = new DeleteIndexRequest("test_index");
//        AcknowledgedResponse delete = restHighLevelClient.indices().delete(test_index, RequestOptions.DEFAULT);
//        System.out.println(delete.isAcknowledged());
//    }

//    @Test
//    public void testdocument() throws IOException {
//        SearchRequest searchRequest = new SearchRequest("blogindex");
//        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
//        searchSourceBuilder.query(QueryBuilders.multiMatchQuery("自动装配原理", "blogTitle","blogContent").analyzer("ik_max_word"))
//                .from(0)
//                .size(20)
//                .highlighter(new HighlightBuilder().field("*").requireFieldMatch(false).preTags("<test>").postTags("</test>"));
//       searchSourceBuilder.timeout(new TimeValue(60, TimeUnit.SECONDS));
//        searchRequest.source(searchSourceBuilder);
//        SearchResponse search = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
//        List<Map<String, Object>> list = new ArrayList<>();
//        for (SearchHit hit : search.getHits().getHits()) {
//            Map<String, HighlightField> highlightFields = hit.getHighlightFields();
//            Map<String, Object> sourceAsMap = hit.getSourceAsMap();
//            if (highlightFields.containsKey("blogContent")){
//                sourceAsMap.put("blogContent",highlightFields.get("blogContent").fragments()[0].toString());
//            }
//            if (highlightFields.containsKey("blogTitle")){
//                sourceAsMap.put("blogTitle",highlightFields.get("blogTitle").fragments()[0].toString());
//            }
//            list.add(sourceAsMap);
//        }
//
//        System.out.println("==================================================================");
//        System.out.println("size   "+list.size());
//        Iterator<Map<String, Object>> iterator = list.iterator();
//        while (iterator.hasNext()) {
//            Map<String, Object> objectMap = iterator.next();
//            System.out.println("==================================================================");
//            System.out.println("blogContent    "+objectMap.get("blogContent"));
//            System.out.println("==================================================================");
//            System.out.println("blogTitle    "+objectMap.get("blogTitle"));
//            System.out.println("==================================================================");
//        }


//        SearchHit[] hits = search.getHits().getHits();
//        for (SearchHit hit : hits) {
//            System.out.println("=============================================================");
//            System.out.println(hit.getSourceAsMap().get("title"));
//            System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
//            System.out.println(hit.getSourceAsString());
//            System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
//            Map<String, HighlightField> highlightFields = hit.getHighlightFields();
//            if (highlightFields.containsKey("content")){
//                System.out.println(highlightFields.get("content").fragments()[0].toString());
//                System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
//            }
//            if (highlightFields.containsKey("title")){
//                System.out.println(highlightFields.get("title").fragments()[0].toString());
//                System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
//            }
//            System.out.println("=============================================================");
//        }

//        UpdateRequest updateRequest = new UpdateRequest("blogindex", "zRtxQnQBlQ5HJN97rJJ0");
//        updateRequest.doc("{\"title\":\"今天我要碰瓷\"}", XContentType.JSON);
//        UpdateResponse update = restHighLevelClient.update(updateRequest, RequestOptions.DEFAULT);
//        System.out.println(update.status());


//        ElasticSearchBlog blog = blogMapper.selectElasticSearchBlogByIdForCommon("09c6a23a220a41ee896784355a7777b9");
//        IndexRequest indexRequest = new IndexRequest("blogindex");
//        Gson gson = new Gson();
//        indexRequest.id(blog.getBlogId());
//        indexRequest.source(gson.toJson(blog), XContentType.JSON);
//        IndexResponse index = restHighLevelClient.index(indexRequest, RequestOptions.DEFAULT);
//        System.out.println(index.status());


//        ArrayList<ElasticSearchBlog> elasticSearchBlogs = blogMapper.selectElasticSearchAllBlogForCommon();
//        BulkRequest bulkRequest = new BulkRequest();
//        Gson gson = new Gson();
//        bulkRequest.timeout("2m");
//        for (ElasticSearchBlog elasticSearchBlog : elasticSearchBlogs) {
//            bulkRequest.add(new IndexRequest("blogindex").id(elasticSearchBlog.getBlogId()).source(gson.toJson(elasticSearchBlog), XContentType.JSON));
//        }
//        BulkResponse bulk = restHighLevelClient.bulk(bulkRequest, RequestOptions.DEFAULT);
//        System.out.println(bulk.hasFailures());
//    }


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
//        Myblog myblog = blogService.selectBlogByIdForCommon("09c6a23a220a41ee896784355a7777b9");
//        System.out.println("===============================================");
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
    @Autowired
    private StringEncryptor stringEncryptor;
    @Test
    public void testencoding(){
        String yuanwen1="47.103.1.235";
        String miwen1 = stringEncryptor.encrypt(yuanwen1) ;
        System.out.println(miwen1);
        System.out.println(stringEncryptor.decrypt(miwen1));

    }


//    @Test
//    public void testwrite(){
//        try {
//            FileWriter fw = new FileWriter("src\\main\\resources\\static\\remote_ext.txt", true);
//            BufferedWriter bw = new BufferedWriter(fw);
//            bw.write("写入测试\n");// 往已有的文件上添加字符串
//            bw.close();
//            fw.close();
//        }
//        catch (Exception e){
//            e.printStackTrace();
//        }
//
//    }
}
