package com.shiyifan.service;

import com.shiyifan.pojo.Myuser;

import java.util.ArrayList;

/**
 *
 * @author ZouCha
 * @name UserService
 * @date 2020-11-20 15:30:38
 *
 **/
public interface UserService{
    /**
     * 用户登录
     * @author ZouCha
     * @date 2020-11-20 15:30:42
     * @method login
     * @params [username, password]
     * @return com.shiyifan.pojo.Myuser
     *
     **/
    Myuser login(String username,String password);

    /**
     * 获取所有用户ID
     * @author ZouCha
     * @date 2020-11-20 15:30:47
     * @method getAllId
     * @params []
     * @return java.util.ArrayList<java.lang.Integer>
     *
     **/
    ArrayList<Integer> getAllId();

}
