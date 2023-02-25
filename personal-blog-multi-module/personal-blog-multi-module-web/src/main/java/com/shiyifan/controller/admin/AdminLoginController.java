package com.shiyifan.controller.admin;

import com.aliyun.oss.OSSException;
import com.google.gson.Gson;
import com.shiyifan.*;
import com.shiyifan.pojo.Result;
import com.shiyifan.pojo.User;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

/**
 * @author ZouCha
 * @name AdminLoginController
 * @date 2020-12-02 18:36
 **/
@RestController
@Log4j2
@RequestMapping("/admin")
public class AdminLoginController {
    @Autowired
    private LoginService loginService;

    @Autowired
    private JwtUtil jwtUtil;

    /**
     * @return com.shiyifan.pojo.Result
     * @author ZouCha
     * @date 2020-12-17 13:56:59
     * @method login
     * @params [username, password]
     **/
    @PostMapping("/login")
    public Result login(@RequestParam("username") String username, @RequestParam("password") String password) throws Exception {
        try {
            User user = loginService.login(username, password);
            if (user != null) {
                return ResultUtil.success("登录成功", new String[]{String.valueOf(user.getUserId()), jwtUtil.createToken(user.getUserId(), user.getUserName())});
            } else {
                return ResultUtil.loginError("账号不存在或密码错误", null);
            }
        } catch (Exception e) {
            log.error("登录出现错误" + e.toString());
            throw new Exception("登录出现错误" + e.toString());
        }
    }

    /**
     * @param :
     * @return com.shiyifan.pojo.Result
     * @author 走叉
     * @date 2023-02-25 19:33:36
     * @method getWxCode
     **/
    @PostMapping("/getWxCode")
    public Result getWxCode() throws Exception {
        try {
            //微信开放平台授权baseUrl
            String baseUrl = "https://open.weixin.qq.com/connect/qrconnect" +
                    "?appid=%s" +
                    "&redirect_uri=%s" +
                    "&response_type=code" +
                    "&scope=snsapi_login" +
                    "&state=%s" +
                    "#wechat_redirect";
            //对redirect_url进行URLEncoder编码
            String redirectUrl = ConstantWxUtil.WX_OPEN_REDIRECT_URL;
            redirectUrl = URLEncoder.encode(redirectUrl, StandardCharsets.UTF_8);
            //设置%s的值
            String url = String.format(
                    baseUrl,
                    ConstantWxUtil.WX_OPEN_APP_ID,
                    redirectUrl,
                    "blog"
            );
            //重定向到请求微信地址
            return ResultUtil.success(url);
        } catch (Exception e) {
            log.error("登录出现错误" + e.toString());
            throw new Exception("登录出现错误" + e.toString());
        }
    }

    @GetMapping("/callback")
    public Result callback(String code, String state) {
        try {
            //获取code值，临时票据类似于验证码
            //拿着code请求微信固定的地址，得到两个值
            //1.向认证服务器发送请求换取access_token
            String baseAccessTokenUrl =
                    "https://api.weixin.qq.com/sns/oauth2/access_token" +
                            "?appid=%s" +
                            "&secret=%s" +
                            "&code=%s" +
                            "&grant_type=authorization_code";
            //拼接三个参数：id 密钥 和 code值
            String accessTokenUrl = String.format(
                    baseAccessTokenUrl,
                    ConstantWxUtil.WX_OPEN_APP_ID,
                    ConstantWxUtil.WX_OPEN_APP_SECRET,
                    code
            );
            //2.请求拼接好的地址，得到返回的两个值access_token和openid
            //使用httpclient发送请求，得到返回结果(json形式的字符串)
            String accessTokenInfo = OkHttpUtil.get(accessTokenUrl);
            //从accessTokenInfo字符串获取两个值access_token、openid
            //把accessTokenInfo字符串转换为map集合，根据map里面的key获取值
            //这里使用json转换工具Gson
            Gson gson = new Gson();
            HashMap accessTokenMap = gson.fromJson(accessTokenInfo, HashMap.class);
            String access_token = (String) accessTokenMap.get("access_token");
            String openid = (String) accessTokenMap.get("openid");

            //3.判断该用户是不是第一次扫码登录
            //通过openid判断
            User user = loginService.login(openid);
            //4.只有第一次登录才获取信息
            if (user == null) {
                //根据access_token和openid再去访问微信的资源服务器，获取用户信息
                String baseUserInfoUrl = "https://api.weixin.qq.com/sns/userinfo" +
                        "?access_token=%s" +
                        "&openid=%s";
                String userInfoUrl = String.format(
                        baseUserInfoUrl,
                        access_token,
                        openid
                );
                //发送请求，得到用户信息
                String userInfo = OkHttpUtil.get(userInfoUrl);
                System.out.println(userInfo);
                //将用户信息存入数据库
                //把json转换为map
                HashMap userInfoMap = gson.fromJson(userInfo, HashMap.class);
                //得到nickname
                String nickname = (String) userInfoMap.get("nickname");
                //得到微信头像avatar
                String headimgurl = (String) userInfoMap.get("headimgurl");
                System.out.println(nickname);
                System.out.println(headimgurl);
                //todo 新用户注册并登录

//                member = new UcenterMember();
//                member.setOpenid(openid);
//                member.setNickname(nickname);
//                member.setAvatar(headimgurl);
//                memberService.save(member);
            }
            return ResultUtil.success("登录成功", new String[]{String.valueOf(user.getUserId()), jwtUtil.createToken(user.getUserId(), user.getUserName())});
        } catch (Exception e) {
            log.error("callback错误" + e.toString());
            throw new OSSException("callback错误" + e.toString());
        }
    }
}
