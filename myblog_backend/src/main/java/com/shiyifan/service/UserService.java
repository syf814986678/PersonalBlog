package com.shiyifan.service;

import com.shiyifan.pojo.Myuser;

import java.util.ArrayList;


public interface UserService{
    //用户登录
    Myuser login(String username,String password);

    //获取所有用户ID
    ArrayList<Integer> getAllId();

}
