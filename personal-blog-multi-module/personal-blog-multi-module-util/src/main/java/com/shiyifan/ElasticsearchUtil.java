package com.shiyifan;

import com.google.gson.Gson;
import com.shiyifan.pojo.ElasticSearchBlog;
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
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author ZouCha
 * @name ElasticsearchUtil
 * @date 2020-12-13 11:51
 **/
@Component
@Log4j2
public class ElasticsearchUtil {
    @Autowired
    private RestHighLevelClient restHighLevelClient;

    /**
     * @return java.util.ArrayList<java.util.Map < java.lang.String, java.lang.Object>>
     * @author ZouCha
     * @date 2020-12-19 16:03:36
     * @method searchContentByPageForCommon
     * @params [keyword, pageNow, pageSize]
     **/
    @Retryable(value = IOException.class)
    public ArrayList<Map<String, Object>> searchContentByPageForCommon(String keyword, int pageNow, int pageSize) throws IOException {
        log.info("方法:searchContentByPageForCommon开始,keyword:" + keyword + ",pageNow:" + pageNow + ",pageSize:" + pageSize);
        int start = (pageNow - 1) * pageSize;
        ArrayList<Map<String, Object>> list = new ArrayList<>();
        try {
            SearchRequest searchRequest = new SearchRequest("blogindex");
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            searchSourceBuilder.query(QueryBuilders.multiMatchQuery(keyword, "blogTitle", "blogContent").analyzer("ik_max_word"))
                    .from(start)
                    .size(pageSize)
                    .highlighter(new HighlightBuilder().field("*").requireFieldMatch(false).preTags("<span style=\"color:red;font-weight:bold\">").postTags("</span>"));
            searchSourceBuilder.timeout(new TimeValue(60, TimeUnit.SECONDS));
            searchRequest.source(searchSourceBuilder);
            SearchResponse search = null;
            search = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
            for (SearchHit hit : search.getHits().getHits()) {
                Map<String, HighlightField> highlightFields = hit.getHighlightFields();
                Map<String, Object> sourceAsMap = hit.getSourceAsMap();
                if (highlightFields.containsKey("blogContent")) {
                    sourceAsMap.put("blogContent", highlightFields.get("blogContent").fragments()[0].toString());
                }
                if (highlightFields.containsKey("blogTitle")) {
                    sourceAsMap.put("blogTitle", highlightFields.get("blogTitle").fragments()[0].toString());
                }
                list.add(sourceAsMap);
            }
        } catch (IOException e) {
            log.error("searchContentByPageForCommon错误" + e);
            throw new IOException("searchContentByPageForCommon错误" + e);
        }
        return list;
    }

    /**
     * @return void
     * @author ZouCha
     * @date 2020-12-30 13:46:56
     * @method addElasticsearchBlogForAdmin
     * @params [elasticSearchBlog]
     **/
    @Retryable(value = IOException.class)
    public void addElasticsearchBlogForAdmin(ElasticSearchBlog elasticSearchBlog) throws IOException {
        log.info("方法:addElasticsearchBlogForAdmin开始,user:" + elasticSearchBlog.getBlogUser()+",blogId:" + elasticSearchBlog.getBlogId());
        try {
            IndexRequest indexRequest = new IndexRequest("blogindex");
            indexRequest.timeout("2s");
            Gson gson = new Gson();
            indexRequest.id(elasticSearchBlog.getBlogId());
            indexRequest.source(gson.toJson(elasticSearchBlog), XContentType.JSON);
            restHighLevelClient.index(indexRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            log.error("addElasticsearchBlogForAdmin错误" + e);
            throw new IOException("addElasticsearchBlogForAdmin错误" + e);
        }
    }

    /**
     * @return void
     * @author ZouCha
     * @date 2020-12-30 15:57:39
     * @method deleteElasticsearchBlogForAdmin
     * @params [blogId]
     **/
    @Retryable(value = IOException.class)
    public void deleteElasticsearchBlogForAdmin(String blogId) throws IOException {
        log.info("方法:deleteElasticsearchBlogForAdmin开始,blogId:" + blogId);
        try {
            DeleteRequest deleteRequest = new DeleteRequest("blogindex", blogId);
            deleteRequest.timeout("2s");
            restHighLevelClient.delete(deleteRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            log.error("deleteElasticsearchBlogForAdmin错误" + e);
            throw new IOException("deleteElasticsearchBlogForAdmin错误" + e);
        }
    }

    /**
     * @return void
     * @author ZouCha
     * @date 2021-01-02 18:16:51
     * @method updateElasticsearchBlogForAdmin
     * @params [elasticSearchBlog]
     **/
    @Retryable(value = Exception.class)
    public void updateElasticsearchBlogForAdmin(ElasticSearchBlog elasticSearchBlog) throws IOException {
        log.info("方法:updateElasticsearchBlogForAdmin开始,user:" + elasticSearchBlog.getBlogUser()+",blogId:" + elasticSearchBlog.getBlogId());
        try {
            deleteElasticsearchBlogForAdmin(elasticSearchBlog.getBlogId());
            addElasticsearchBlogForAdmin(elasticSearchBlog);
        } catch (IOException e) {
            log.error("updateElasticsearchBlogForAdmin错误" + e);
            throw new IOException("updateElasticsearchBlogForAdmin错误" + e);
        }
    }
}
