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

@RestController
@RequestMapping("/user")
@Log4j2
@CrossOrigin
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/login")
    public Result login(@RequestParam("username")String username, @RequestParam("password")String password){
        Result result = new Result();
        HashMap<String, Object> map = new HashMap<>();
        try {
            Myuser myuser = userService.login(username, password);
            if(myuser!=null){
                result.setCodeState(CodeState.success);
                map.put("myuser", myuser);
                map.put("token",jwtUtil.createToken(myuser.getUserId(), myuser.getUserName()));
            }
            else {
                result.setCodeState(CodeState.loginError);
                map.put("loginError", "账号或密码错误！");
            }
        }
        catch (Exception e){
            log.error(e);
            result.setCodeState(CodeState.exception);
            map.put("exception", "服务端处理错误！请稍后再试");
        }
        finally {
            result.setMsg(map);
        }
        return result;
    }
}
