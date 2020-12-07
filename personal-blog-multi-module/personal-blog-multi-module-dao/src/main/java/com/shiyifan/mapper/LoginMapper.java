package com.shiyifan.mapper;

import com.shiyifan.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * @author ZouCha
 * @name LoginMapper
 * @date 2020-12-07 16:05
 **/
@Mapper
@Repository
public interface LoginMapper {
    /**
     * @return com.shiyifan.pojo.User
     * @author ZouCha
     * @date 2020-12-07 16:06:00
     * @method login
     * @params [username, password]
     **/
    User login(@Param("username") String username);
}
