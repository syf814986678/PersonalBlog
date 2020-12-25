package com.shiyifan;

import com.google.gson.Gson;
import com.shiyifan.mapper.BlogMapper;
import com.shiyifan.pojo.ElasticSearchBlog;
import lombok.extern.log4j.Log4j2;
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
    public ArrayList<Map<String, Object>> searchContentByPageForCommon(String keyword, int pageNow, int pageSize) throws IOException {
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
            log.error(e);
            throw new IOException();
        }
        return list;
    }

    /**
     * @return java.lang.Boolean
     * @author ZouCha
     * @date 2020-12-13 11:55:27
     * @method addElasticsearchBlog
     * @params [blogId]
     **/
    @Retryable(value = IOException.class)
    public Boolean addElasticsearchBlog(ElasticSearchBlog elasticSearchBlog) throws IOException {
        try {
            IndexRequest indexRequest = new IndexRequest("blogindex");
            indexRequest.timeout("2s");
            Gson gson = new Gson();
            indexRequest.id(elasticSearchBlog.getBlogId());
            indexRequest.source(gson.toJson(elasticSearchBlog), XContentType.JSON);
            restHighLevelClient.index(indexRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            log.error(e);
            throw new IOException();
        }
        return true;
    }
}
