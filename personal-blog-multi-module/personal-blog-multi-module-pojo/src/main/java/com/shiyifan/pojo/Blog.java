package com.shiyifan.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * @author ZouCha
 * @name Blog
 * @date 2020-12-01 15:25:20
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Blog implements Serializable {
    private String blogId;
    private String blogTitle;
    private String blogCoverImage;
    private String blogContent;
    private Category category;
    private User user;
    private int isDeleted;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createGmt;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateGmt;
}
