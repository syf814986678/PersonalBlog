package com.shiyifan.controller;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.common.utils.BinaryUtil;
import com.aliyun.oss.model.*;
import com.google.gson.Gson;
import com.shiyifan.constant.CodeState;
import com.shiyifan.constant.MyConstant;
import com.shiyifan.vo.Result;
import io.jsonwebtoken.Claims;
import lombok.extern.log4j.Log4j2;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.sql.Date;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author ZouCha
 * @name UploadController
 * @date 2020-11-20 15:12:36
 *
 **/
@RestController
@RequestMapping("/upload")
@Log4j2
public class UploadController {

    @Autowired
    private MyConstant myConstant;

    private String dir;

//    @PostMapping("/uploadBlogCoverImage")
//    public Result uploadBlogCoverImage(HttpServletRequest request,@RequestParam("file") MultipartFile file){
//        Result result = new Result();
//        HashMap<String, Object> map = new HashMap<>();
//        OSS ossClient = new OSSClientBuilder().build(myConstant.getEndpoint(),myConstant.getAccessKeyId(),myConstant.getAccessKeySecret());
//        try {
//            Claims claims = (Claims)request.getAttribute("user_claims");
//            if(claims!=null){
//                int userId = (int)claims.get("userId");
//                String userName=(String)claims.get("userName");
//                String key="myblog/BlogCoverImage/"+ userId+"-"+userName+"/"+ UUID.randomUUID().toString().replaceAll("-", "")+".jpg";
//                PutObjectRequest putObjectRequest = new PutObjectRequest(myConstant.getBucket(), key, new ByteArrayInputStream(file.getBytes()));
//                ossClient.putObject(putObjectRequest);
//                result.setCodeState(CodeState.SUCCESS_CODE);
//                map.put("blogCoverImage","https://picture.chardance.cloud/"+key);
//            }
//            else {
//                result.setCodeState(CodeState.TOKEN_ERROR_CODE);
//                map.put("TOKEN_ERROR_CODE", request.getAttribute("TOKEN_ERROR_CODE"));
//            }
//        }
//        catch (Exception e){
//            log.error(e);
//            result.setCodeState(CodeState.EXCEPTION_CODE);
//            map.put("EXCEPTION_CODE", "服务端处理错误！请稍后再试");
//        }
//        finally {
//            // 关闭OSSClient。
//            ossClient.shutdown();
//            result.setMsg(map);
//        }
//        return result;
//    }

    /**
     * 随机封面图片
     * 已修改(controller名称,restful风格)
     * @author ZouCha
     * @date 2020-11-20 15:12:45
     * @method randomBlogCoverImage
     * @params [request]
     * @return com.shiyifan.vo.Result
     *
     **/
    @PostMapping("/randomBlogCoverImage")
    public Result randomBlogCoverImage(HttpServletRequest request){
        Result result = new Result();
        HashMap<String, Object> map = new HashMap<>();
        OSS ossClient = new OSSClientBuilder().build(myConstant.getEndpoint(),myConstant.getAccessKeyId(),myConstant.getAccessKeySecret());
        try {
            Claims claims = (Claims)request.getAttribute(CodeState.USER_CLAIMS_STR);
            if(claims!=null){
                ListObjectsRequest listObjectsRequest = new ListObjectsRequest();
                listObjectsRequest.setBucketName(myConstant.getBucket());
                listObjectsRequest.setPrefix("myblog/Random/");
                ObjectListing objectListing = ossClient.listObjects(listObjectsRequest);
                List<OSSObjectSummary> sums = objectListing.getObjectSummaries();
                result.setCodeState(CodeState.SUCCESS_CODE);
                map.put("blogCoverImage","https://picture.chardance.cloud/"+sums.get((int) (1 + Math.random() * (sums.size()-1))).getKey());
            }
            else {
                if(request.getAttribute(CodeState.TOKEN_ERROR_STR)!=null){
                    result.setCodeState(CodeState.TOKEN_ERROR_CODE);
                    map.put(CodeState.TOKEN_ERROR_STR, request.getAttribute(CodeState.TOKEN_ERROR_STR));
                }
                if(request.getAttribute(CodeState.TOKEN_TIME_LIMIT_STR)!=null) {
                    result.setCodeState(CodeState.TOKEN_TIME_LIMIT_CODE);
                    map.put(CodeState.TOKEN_TIME_LIMIT_STR, request.getAttribute(CodeState.TOKEN_TIME_LIMIT_STR));
                }
            }
        }
        catch (Exception e){
            log.error(e);
            result.setCodeState(CodeState.EXCEPTION_CODE);
            map.put(CodeState.EXCEPTION_STR, "获取随机封面图片失败！");
        }
        finally {
            // 关闭OSSClient。
            ossClient.shutdown();
            result.setMsg(map);
        }
        return result;
    }

    /**
     * 获取Token
     * 已修改(controller名称,restful风格)
     * @author ZouCha
     * @date 2020-11-20 15:12:53
     * @method getOssToken
     * @params [request, type]
     * @return com.shiyifan.vo.Result
     *
     **/
    @RequestMapping("/getOssToken/{type}")
    public Result getOssToken(HttpServletRequest request,@PathVariable("type") int type){
        Result result = new Result();
        HashMap<String, Object> map = new HashMap<>();
        OSS ossClient = new OSSClientBuilder().build(myConstant.getEndpoint(),myConstant.getAccessKeyId(),myConstant.getAccessKeySecret());
        try {
            Claims claims = (Claims)request.getAttribute(CodeState.USER_CLAIMS_STR);
            if(claims!=null){
                int userId = (int)claims.get("userId");
                String userName=(String)claims.get("userName");
                if(type==0){
                    dir="myblog/BlogCoverImage/"+ userId+"-"+userName+"/";
                }
                else if(type==1){
                    dir="myblog/BlogContentImage/"+ userId+"-"+userName+"/";
                }
                // host的格式为 bucketname.endpoint
                String host = "https://" + myConstant.getBucket() + "." + myConstant.getEndpoint();
                // callbackUrl为 上传回调服务器的URL，请将下面的IP和Port配置为您自己的真实信息。
                String callbackUrl = "https://47.103.1.235:8989/upload/callback";
                long expireTime = 30;
                long expireEndTime = System.currentTimeMillis() + expireTime * 1000;
                Date expiration = new Date(expireEndTime);
                PolicyConditions policyConds = new PolicyConditions();
                policyConds.addConditionItem(PolicyConditions.COND_CONTENT_LENGTH_RANGE, 0, 1048576000);
                policyConds.addConditionItem(MatchMode.StartWith, PolicyConditions.COND_KEY, dir);

                String postPolicy = ossClient.generatePostPolicy(expiration, policyConds);
                byte[] binaryData = postPolicy.getBytes(StandardCharsets.UTF_8);
                String encodedPolicy = BinaryUtil.toBase64String(binaryData);
                String postSignature = ossClient.calculatePostSignature(postPolicy);

                map.put("accessid", myConstant.getAccessKeyId());
                map.put("policy", encodedPolicy);
                map.put("signature", postSignature);
                map.put("dir", dir);
                map.put("host", host);
                map.put("expire", String.valueOf(expireEndTime / 1000));

                JSONObject jasonCallback = new JSONObject();
                jasonCallback.put("callbackUrl", callbackUrl);
                jasonCallback.put("callbackBody",
                        "filename=${object}&size=${size}&mimeType=${mimeType}&height=${imageInfo.height}&width=${imageInfo.width}");
                jasonCallback.put("callbackBodyType", "application/x-www-form-urlencoded");
                String base64CallbackBody = BinaryUtil.toBase64String(jasonCallback.toString().getBytes());
                map.put("callback", base64CallbackBody);
                result.setCodeState(CodeState.SUCCESS_CODE);
            }
            else {
                if(request.getAttribute(CodeState.TOKEN_ERROR_STR)!=null){
                    result.setCodeState(CodeState.TOKEN_ERROR_CODE);
                    map.put(CodeState.TOKEN_ERROR_STR, request.getAttribute(CodeState.TOKEN_ERROR_STR));
                }
                if(request.getAttribute(CodeState.TOKEN_TIME_LIMIT_STR)!=null) {
                    result.setCodeState(CodeState.TOKEN_TIME_LIMIT_CODE);
                    map.put(CodeState.TOKEN_TIME_LIMIT_STR, request.getAttribute(CodeState.TOKEN_TIME_LIMIT_STR));
                }
            }
        }
        catch (Exception e){
            log.error(e);
            result.setCodeState(CodeState.EXCEPTION_CODE);
            map.put(CodeState.EXCEPTION_STR, "获取Token失败！");
        }
        finally {
            // 关闭OSSClient。
            ossClient.shutdown();
            result.setMsg(map);
        }
        return result;
    }

    /**
     * 上传Oss回调
     * @author ZouCha
     * @date 2020-11-20 15:12:59
     * @method callback
     * @params [request]
     * @return java.lang.String
     *
     **/
    @RequestMapping("/callback")
    public String callback(HttpServletRequest request) {
        Gson gson = new Gson();
        HashMap<String, Object> map = new HashMap<>();
        try {
            String filename = request.getParameter("filename");
            filename = "https://picture.chardance.cloud/".concat(filename);
            map.put("filename", filename);
            map.put("Size", request.getParameter("size"));
            map.put("MimeType", request.getParameter("mimeType"));
            map.put("Width", request.getParameter("Width"));
            map.put("Height", request.getParameter("Height"));
            map.put("codeState", CodeState.SUCCESS_CODE);
        }

        catch (Exception e){
            log.error(e);
            map.put("codeState", CodeState.OSS_EXCEPTION_CODE);
            map.put(CodeState.OSS_EXCEPTION_STR, "上传Oss回调失败！");
        }
        return gson.toJson(map);
    }
}
