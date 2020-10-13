package com.shiyifan.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * @author 81498
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Myuser implements Serializable {
    private int userId;
    private String userName;
    private String userPassword;
    private String userRole;
    private String isDeleted;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createGmt;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateGmt;
}
