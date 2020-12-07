package com.shiyifan;

import com.shiyifan.pojo.User;

/**
 * @author ZouCha
 * @name loginService
 * @date 2020-12-07 16:02
 **/
public interface LoginService {
    /**
     * @return com.shiyifan.pojo.User
     * @author ZouCha
     * @date 2020-12-07 16:04:36
     * @method login
     * @params [username, password]
     **/
    User login(String username, String password) throws Exception;
}
