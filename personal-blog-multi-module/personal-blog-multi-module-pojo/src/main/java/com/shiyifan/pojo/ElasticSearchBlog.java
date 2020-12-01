package com.shiyifan.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author ZouCha
 * @name ElasticSearchBlog
 * @date 2020-11-20 15:22:29
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ElasticSearchBlog implements Serializable {
    private String blogId;
    private String blogTitle;
    private String blogCoverImage;
    private String blogContent;
    private String blogCategory;
    private String blogUser;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String createGmt;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String updateGmt;
}
