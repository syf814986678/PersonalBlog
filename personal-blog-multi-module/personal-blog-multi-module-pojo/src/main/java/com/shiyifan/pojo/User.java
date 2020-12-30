package com.shiyifan.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * @author ZouCha
 * @name Myuser
 * @date 2020-11-20 15:23:04
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User implements Serializable {
    private int userId;
    private String userName;
    private String userPassword;
    private String userRole;
    private int isDeleted;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createGmt;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateGmt;
}
