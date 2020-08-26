package com.shiyifan.service;

import com.shiyifan.pojo.Myuser;


public interface UserService{
    //用户登录
    Myuser login(String username,String password);

}
