package com.shiyifan;

import com.shiyifan.mapper.LoginMapper;
import com.shiyifan.pojo.User;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

/**
 * @author ZouCha
 * @name LoginServiceImpl
 * @date 2020-12-07 16:03
 **/
@Service
@Order
@Log4j2
public class LoginServiceImpl implements LoginService {
    @Autowired
    private LoginMapper loginMapper;

    /**
     * @return com.shiyifan.pojo.User
     * @author user
     * @date 2021-01-05 16:02:14
     * @method login
     * @params [username, password]
     **/
    @Override
    public User login(String username, String password) throws Exception {
        log.info("方法:login开始,username:" + username + ",password:" + password);
        User user = null;
        try {
            user = loginMapper.login(username);
            if (user != null && Md5Util.getMd5Str(password).equals(user.getUserPassword())) {
                return user;
            } else {
                return null;
            }
        } catch (Exception e) {
            log.error("登录出现错误" + e.toString());
            throw new Exception("登录出现错误" + e.toString());
        }
    }
}
