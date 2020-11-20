package com.shiyifan.dao;

import com.shiyifan.pojo.Myuser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
/**
 *
 * @author ZouCha
 * @name UserMapper
 * @date 2020-11-20 15:21:39
 *
 **/
@Mapper
@Repository
public interface UserMapper {
    /**
     * 用户登录
     * @author ZouCha
     * @date 2020-11-20 15:21:45
     * @method login
     * @params [username]
     * @return com.shiyifan.pojo.Myuser
     *
     **/
    Myuser login(@Param("username")String username);

    /**
     * 获取所有用户ID
     * @author ZouCha
     * @date 2020-11-20 15:21:51
     * @method getAllId
     * @params []
     * @return java.util.ArrayList<java.lang.Integer>
     *
     **/
    ArrayList<Integer> getAllId();
}
