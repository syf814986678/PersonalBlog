package com.shiyifan.service;

import com.shiyifan.dao.UserMapper;
import com.shiyifan.pojo.Myuser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

@Service
@Transactional
public class UserServiceImpl implements UserService{

    @Autowired
    private UserMapper userMapper;


    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

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

    @Override
    public ArrayList<Integer> getAllId() {
        return userMapper.getAllId();
    }


}
