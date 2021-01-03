package com.shiyifan.controller.admin;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.common.utils.BinaryUtil;
import com.aliyun.oss.model.*;
import com.shiyifan.ResultUtil;
import com.shiyifan.pojo.CodeState;
import com.shiyifan.pojo.Result;
import io.jsonwebtoken.Claims;
import lombok.extern.log4j.Log4j2;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.sql.Date;
import java.util.HashMap;
import java.util.List;

/**
 * @author ZouCha
 * @name AdminUploadController
 * @date 2020-12-02 18:35
 **/
@RestController
@Log4j2
@RequestMapping("/upload/admin")
public class AdminUploadController {

    @Value("${aliyun.endpoint}")
    private String endpoint;

    @Value("${aliyun.bucket}")
    private String bucket;

    @Value("${aliyun.accessKeyId}")
    private String accessKeyId;

    @Value("${aliyun.accessKeySecret}")
    private String accessKeySecret;

    /**
     * 随机封面图片
     * 已修改(controller名称,restful风格)
     *
     * @return com.shiyifan.vo.Result
     * @author ZouCha
     * @date 2020-11-20 15:12:45
     * @method randomBlogCoverImage
     * @params [request]
     **/
    @PostMapping("/randomBlogCoverImage")
    public Result randomBlogCoverImage(HttpServletRequest request) throws Exception {
        Claims claims = null;
        List<OSSObjectSummary> sums = null;
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        try {
            claims = (Claims) request.getAttribute(CodeState.USER_CLAIMS_STR);
            ListObjectsRequest listObjectsRequest = new ListObjectsRequest();
            listObjectsRequest.setBucketName(bucket);
            listObjectsRequest.setPrefix("myblog/Random/");
            ObjectListing objectListing = ossClient.listObjects(listObjectsRequest);
            sums = objectListing.getObjectSummaries();
        } catch (Exception e) {
            log.error("randomBlogCoverImage错误" + e.toString());
            throw new Exception("randomBlogCoverImage错误" + e.toString());
        } finally {
            // 关闭OSSClient。
            ossClient.shutdown();
        }
        return ResultUtil.success("https://picture.chardance.cloud/" + sums.get((int) (1 + Math.random() * (sums.size() - 1))).getKey());
    }

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
    @PostMapping("/getToken/{type}")
    public Result getToken(HttpServletRequest request, @PathVariable("type") int type) throws Exception {
        Claims claims = null;
        HashMap<String, Object> result = null;
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        try {
            claims = (Claims) request.getAttribute(CodeState.USER_CLAIMS_STR);
            int userId = (int) claims.get("userId");
            String userName = (String) claims.get("userName");
            StringBuffer dir = new StringBuffer();
            if (type == 0) {
                dir.append("myblog/BlogCoverImage/").append(userId).append("-").append(userName).append("/");
            } else if (type == 1) {
                dir.append("myblog/BlogContentImage/").append(userId).append("-").append(userName).append("/");
            }
            // callbackUrl为 上传回调服务器的URL，请将下面的IP和Port配置为您自己的真实信息。
            String callbackUrl = "http://api.noahsark1.vip/upload/admin/callback";
            long expireTime = 30;
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

    /**
     * 上传Oss回调
     *
     * @return java.lang.String
     * @author ZouCha
     * @date 2020-11-20 15:12:59
     * @method callback
     * @params [request]
     **/
    @PostMapping ("/callback")
    public Result callback(HttpServletRequest request) throws OSSException {
        HashMap<String, Object> map = null;
        try {
            map = new HashMap<>(5);
            map.put("filename","https://picture.chardance.cloud/".concat(String.valueOf(request.getAttribute("filename"))));
            map.put("size", request.getAttribute("size"));
            map.put("mimeType", request.getAttribute("mimeType"));
            map.put("width", request.getAttribute("width"));
            map.put("height", request.getAttribute("height"));
        } catch (Exception e) {
            log.error("callback错误" + e.toString());
            throw new OSSException("callback错误" + e.toString());
        }
        return ResultUtil.success(map);
    }
}
