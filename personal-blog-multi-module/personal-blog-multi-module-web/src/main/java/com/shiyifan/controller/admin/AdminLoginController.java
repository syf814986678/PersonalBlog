package com.shiyifan.controller.admin;

import com.shiyifan.JwtUtil;
import com.shiyifan.LoginService;
import com.shiyifan.ResultUtil;
import com.shiyifan.pojo.Category;
import com.shiyifan.pojo.CodeState;
import com.shiyifan.pojo.Result;
import com.shiyifan.pojo.User;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author ZouCha
 * @name AdminLoginController
 * @date 2020-12-02 18:36
 **/
@RestController
@CrossOrigin
@Log4j2
@RequestMapping("/admin")
public class AdminLoginController {
    @Autowired
    private LoginService loginService;

    @Autowired
    private JwtUtil jwtUtil;
    @PostMapping("/login")
    public Result login(@RequestParam("username") String username, @RequestParam("password") String password) throws Exception {

        try {
            User user =loginService.login(username, password);
            if (user!=null) {
                return ResultUtil.success("登录成功",jwtUtil.createToken(user.getUserId(), user.getUserName()));
            } else {
                return ResultUtil.loginError("账号不存在或密码错误",null);
            }
        } catch (Exception e) {
            log.error("登录出现错误" + e.toString());
            throw new Exception("登录出现错误" + e.toString());
        }
    }
}
