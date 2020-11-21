package com.shiyifan.controller;

import com.shiyifan.constant.CodeState;
import com.shiyifan.pojo.Myuser;
import com.shiyifan.service.UserService;
import com.shiyifan.utils.JwtUtil;
import com.shiyifan.vo.Result;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

/**
 *
 * @author ZouCha
 * @name UserController
 * @date 2020-11-21 12:23:39
 *
 **/
@RestController
@RequestMapping("/user")
@Log4j2
@CrossOrigin
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    /**
     * 登录
     * @author ZouCha
     * @date 2020-11-21 12:23:15
     * @method login
     * @params [username, password]
     * @return com.shiyifan.vo.Result
     *
     **/
    @PostMapping("/login")
    public Result login(@RequestParam("username")String username, @RequestParam("password")String password){
        Result result = new Result();
        HashMap<String, Object> map = new HashMap<>();
        try {
            Myuser myuser = userService.login(username, password);
            if(myuser!=null){
                result.setCodeState(CodeState.SUCCESS_CODE);
                map.put("myuser", myuser);
                map.put("token",jwtUtil.createToken(myuser.getUserId(), myuser.getUserName()));
            }
            else {
                result.setCodeState(CodeState.LOGIN_ERROR_CODE);
                map.put(CodeState.LOGIN_ERROR_STR, "账号或密码错误！");
            }
        }
        catch (Exception e){
            log.error(e);
            result.setCodeState(CodeState.EXCEPTION_CODE);
            map.put(CodeState.EXCEPTION_STR, "登录失败！");
        }
        result.setMsg(map);
        return result;
    }
}
