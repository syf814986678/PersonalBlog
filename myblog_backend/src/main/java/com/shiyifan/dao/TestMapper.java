package com.shiyifan.dao;

import com.shiyifan.pojo.Myblog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
/**
 *
 * @author ZouCha
 * @name TestMapper
 * @date 2020-11-20 15:21:25
 *
 **/
@Mapper
@Repository
public interface TestMapper {

    ArrayList<Myblog> selectAllBlogTitle(@Param("categoryId")int categoryId);
    void update(@Param("blogTitle")String blogTitle,@Param("blogId")String blogId);



}
