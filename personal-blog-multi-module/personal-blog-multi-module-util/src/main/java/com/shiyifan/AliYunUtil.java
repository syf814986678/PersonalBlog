package com.shiyifan;

import com.aliyun.facebody20191230.Client;
import com.aliyun.facebody20191230.models.SearchFaceRequest;
import com.aliyun.facebody20191230.models.SearchFaceResponse;
import com.aliyun.facebody20191230.models.SearchFaceResponseBody;
import com.aliyun.teaopenapi.models.Config;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dcdn.model.v20180115.PreloadDcdnObjectCachesRequest;
import com.aliyuncs.dcdn.model.v20180115.PreloadDcdnObjectCachesResponse;
import com.aliyuncs.dcdn.model.v20180115.RefreshDcdnObjectCachesRequest;
import com.aliyuncs.dcdn.model.v20180115.RefreshDcdnObjectCachesResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import com.google.gson.Gson;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author ZouCha
 * @name AliYunUtil
 * @date 2020-12-01 12:06
 **/
@Component
@Log4j2
public class AliYunUtil {

    @Value("${aliyun.accessKeyId}")
    private String accessKeyId;

    @Value("${aliyun.accessKeySecret}")
    private String accessKeySecret;

    @Value("${aliyun.path}")
    private String path;

    /**
     * @return void
     * @author ZouCha
     * @date 2020-12-01 12:14:08
     * @method refreshDcdn
     * @params []
     **/
    public void refreshDcdn() throws ClientException {
        DefaultProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, accessKeySecret);
        IAcsClient client = new DefaultAcsClient(profile);
        RefreshDcdnObjectCachesRequest request = new RefreshDcdnObjectCachesRequest();
        request.setObjectPath(path);
        try {
            RefreshDcdnObjectCachesResponse response = client.getAcsResponse(request);
            log.info(new Gson().toJson(response));
        } catch (ClientException e) {
            log.error("阿里云刷新Dcdn错误" + e.toString());
            throw new ClientException("阿里云刷新Dcdn错误" + e.toString());
        }
    }

    /**
     * @return void
     * @author ZouCha
     * @date 2020-12-01 12:07:44
     * @method preLoadDcdn
     * @params []
     **/
    public void preLoadDcdn() throws ClientException {
        DefaultProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, accessKeySecret);
        IAcsClient client = new DefaultAcsClient(profile);
        PreloadDcdnObjectCachesRequest request = new PreloadDcdnObjectCachesRequest();
        request.setObjectPath(path);
        try {
            PreloadDcdnObjectCachesResponse response = client.getAcsResponse(request);
            log.info(new Gson().toJson(response));
        } catch (ClientException e) {
            log.error("阿里云预载Dcdn错误" + e.toString());
            throw new ClientException("阿里云预载Dcdn错误" + e.toString());
        }
    }

    public String faceSearch(String imageUrl) {
        try {
            Config config = new Config()
                    // 您的AccessKey ID
                    .setAccessKeyId(accessKeyId)
                    // 您的AccessKey Secret
                    .setAccessKeySecret(accessKeySecret);
            // 访问的域名
            config.endpoint = "facebody.cn-shanghai.aliyuncs.com";
            Client client = new Client(config);
            SearchFaceRequest searchFaceRequest = new SearchFaceRequest()
                    .setDbName("zoucha")
                    .setImageUrl(imageUrl)
                    .setLimit(1)
                    .setQualityScoreThreshold(80F)
                    .setMaxFaceNum(1L);
            SearchFaceResponseBody.SearchFaceResponseBodyDataMatchListFaceItems item = client.searchFace(searchFaceRequest).getBody().getData().getMatchList().get(0).getFaceItems().get(0);
            if (item.getConfidence() < 80 || item.getScore() < 0.8) {
                return null;
            }
            return item.getEntityId();
        } catch (Exception e) {
            log.error(e);
            e.printStackTrace();
            return null;
        }
    }
}
