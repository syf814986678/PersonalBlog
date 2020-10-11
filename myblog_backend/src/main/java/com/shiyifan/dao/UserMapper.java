package com.shiyifan.dao;

import com.shiyifan.pojo.Myuser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Mapper
@Repository
public interface UserMapper {
    //用户登录
    Myuser login(@Param("username")String username);

    //获取所有用户ID
    ArrayList<Integer> getAllId();
}
