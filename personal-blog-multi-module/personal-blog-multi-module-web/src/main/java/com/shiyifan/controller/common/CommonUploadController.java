package com.shiyifan.controller.common;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.common.utils.BinaryUtil;
import com.aliyun.oss.model.MatchMode;
import com.aliyun.oss.model.PolicyConditions;
import com.shiyifan.*;
import com.shiyifan.pojo.Result;
import com.shiyifan.pojo.User;
import lombok.extern.log4j.Log4j2;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.sql.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author ZouCha
 * @name CommonUploadController
 * @date 2020-12-02 18:35
 **/
@RestController
@Log4j2
@RequestMapping("/upload/common")
public class CommonUploadController {

    @Autowired
    private VisitorUtil visitorUtil;

    @Autowired
    private AliYunUtil aliYunUtil;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private LoginService loginService;

    @Value("${aliyun.endpoint}")
    private String endpoint;

    @Value("${aliyun.bucket}")
    private String bucket;

    @Value("${aliyun.accessKeyId}")
    private String accessKeyId;

    @Value("${aliyun.accessKeySecret}")
    private String accessKeySecret;

    @Value("${aliyun.commonCallbackProtocol}")
    private String commonCallbackProtocol;

    @Value("${aliyun.commonCallbackHost}")
    private String commonCallbackHost;

    @Value("${aliyun.commonCallbackPath}")
    private String commonCallbackPath;

    /**
     * 获取Token
     * 已修改(controller名称,restful风格)
     *
     * @return com.shiyifan.vo.Result
     * @author ZouCha
     * @date 2020-11-20 15:12:45
     * @method randomBlogCoverImage
     * @params [request]
     **/
    @PostMapping("/getToken")
    public Result getToken(HttpServletRequest request) throws Exception {
        if (!visitorUtil.isLimited(request)) {
            HashMap<String, Object> result = null;
            OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
            String dir = "myblog/loginFace/";
            try {
                // callbackUrl为 上传回调服务器的URL，请将下面的IP和Port配置为您自己的真实信息。
                String callbackUrl = commonCallbackProtocol + commonCallbackHost + commonCallbackPath;
                long expireTime = 300;
                long expireEndTime = System.currentTimeMillis() + expireTime * 1000;
                Date expiration = new Date(expireEndTime);
                PolicyConditions policyConditions = new PolicyConditions();
                policyConditions.addConditionItem(PolicyConditions.COND_CONTENT_LENGTH_RANGE, 0, 1048576000);
                policyConditions.addConditionItem(MatchMode.StartWith, PolicyConditions.COND_KEY, dir.toString());

                String postPolicy = ossClient.generatePostPolicy(expiration, policyConditions);
                byte[] binaryData = postPolicy.getBytes(StandardCharsets.UTF_8);
                String encodedPolicy = BinaryUtil.toBase64String(binaryData);
                String postSignature = ossClient.calculatePostSignature(postPolicy);

                // host的格式为 bucketname.endpoint
                result = new HashMap<>(7);
                result.put("accessId", accessKeyId);
                result.put("policy", encodedPolicy);
                result.put("signature", postSignature);
                result.put("dir", dir);
                result.put("host", "https://" + bucket + "." + endpoint);
                result.put("expire", String.valueOf(expireEndTime / 1000));

                JSONObject jasonCallback = new JSONObject();
                jasonCallback.put("callbackUrl", callbackUrl);
                jasonCallback.put("callbackHost", commonCallbackHost);
                jasonCallback.put("callbackBody",
                        "filename=${object}&size=${size}&mimeType=${mimeType}&height=${imageInfo.height}&width=${imageInfo.width}");
                jasonCallback.put("callbackBodyType", "application/x-www-form-urlencoded");
                String base64CallbackBody = BinaryUtil.toBase64String(jasonCallback.toString().getBytes());
                result.put("callback", base64CallbackBody);
            } catch (Exception e) {
                log.error("getToken错误" + e.toString());
                throw new Exception("getToken错误" + e.toString());
            } finally {
                // 关闭OSSClient。
                ossClient.shutdown();
            }
            return ResultUtil.success(result);
        }
        return ResultUtil.operationError(null);

    }

    /**
     * 上传Oss回调
     *
     * @return java.lang.String
     * @author ZouCha
     * @date 2020-11-20 15:12:59
     * @method callback
     * @params [request]
     **/
    @PostMapping("/callback")
    public Result callback(HttpServletRequest request) throws OSSException {
        try {
            String name = aliYunUtil.faceSearch("https://chardance-picture.oss-cn-shanghai.aliyuncs.com/".concat(String.valueOf(request.getParameter("filename"))));
            if (name == null) {
                return ResultUtil.loginError("用户不存在或置信度低!", null);
            }
            User user = loginService.login(name);
            if (user != null) {
                return ResultUtil.success("登录成功", new String[]{String.valueOf(user.getUserId()), jwtUtil.createToken(user.getUserId(), user.getUserName())});
            }
            else {
                return ResultUtil.loginError("账号不存在或密码错误", null);
            }
        } catch (Exception e) {
            log.error("callback错误" + e.toString());
            throw new OSSException("callback错误" + e.toString());
        }
    }
}
