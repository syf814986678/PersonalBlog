package com.shiyifan.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * @author ZouCha
 * @name Category
 * @date 2020-12-01 15:25:28
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Category implements Serializable {
    private int categoryId;
    private String categoryName;
    private int categoryRank;
    private int categoryUserId;
    private int isDeleted;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createGmt;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateGmt;
}
