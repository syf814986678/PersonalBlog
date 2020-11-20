package com.shiyifan.service;

import com.shiyifan.dao.UserMapper;
import com.shiyifan.pojo.Myuser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

/**
 *
 * @author ZouCha
 * @name UserServiceImpl
 * @date 2020-11-20 15:31:01
 *
 **/
@Service
@Transactional
public class UserServiceImpl implements UserService{

    @Autowired
    private UserMapper userMapper;


    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    /**
     *
     * @author ZouCha
     * @date 2020-11-20 15:31:09
     * @method login
     * @params [username, password]
     * @return com.shiyifan.pojo.Myuser
     *
     **/
    @Override
    public Myuser login(String username, String password) {
        Myuser myuser = userMapper.login(username);
        if(myuser!=null){
            if(passwordEncoder.matches(password, myuser.getUserPassword())){
                return myuser;
            }
        }
        return null;
    }
    /**
     *
     * @author ZouCha
     * @date 2020-11-20 15:31:19
     * @method getAllId
     * @params []
     * @return java.util.ArrayList<java.lang.Integer>
     *
     **/
    @Override
    public ArrayList<Integer> getAllId() {
        return userMapper.getAllId();
    }


}
