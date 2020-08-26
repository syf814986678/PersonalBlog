package com.shiyifan.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Myblog implements Serializable {
    private String blogId;
    private String blogTitle;
    private String blogCoverImage;
    private String blogContent;
    private Mycategory mycategory;
    private Myuser myuser;
    private int isDeleted;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createGmt;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateGmt;
}
