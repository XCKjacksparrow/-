package cn.bestrivenlf.electronadmin.controller;

import cn.bestrivenlf.electronadmin.common.R;
import cn.bestrivenlf.electronadmin.service.Video.VideoAdminService;
import cn.bestrivenlf.electronadmin.until.ConstantPropertiesUtil;
import cn.bestrivenlf.electronadmin.until.VideoClient;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.vod.model.v20170321.DeleteVideoRequest;
import com.aliyuncs.vod.model.v20170321.GetVideoPlayAuthRequest;
import com.aliyuncs.vod.model.v20170321.GetVideoPlayAuthResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/eduvod/video")
@CrossOrigin
public class VideoAdminController {
    @Autowired
    VideoAdminService videoAdminService;


    @PostMapping("addvideo")
    public R addvideo(MultipartFile file){

        String id = videoAdminService.addvideo(file);
        return R.ok().data("videoId",id);
    }


    @DeleteMapping("removeVideo/{id}")
    public R removeVideo (@PathVariable String id){

        try {
            DefaultAcsClient client = VideoClient.initVodClient(ConstantPropertiesUtil.ACCESS_KEY_ID, ConstantPropertiesUtil.ACCESS_KEY_SECRET);
            DeleteVideoRequest request = new DeleteVideoRequest();

            request.setVideoIds(id);

            client.getAcsResponse(request);

            return R.ok();
        }catch (Exception e){
            e.getStackTrace();
            return R.error();
        }
    }



    @GetMapping("getVideoPlayAuth/{videoId}")
    public R getVideoPlayAuth(@PathVariable("videoId") String videoId) throws
            Exception {
             //获取阿里云存储相关常量
        String accessKeyId = ConstantPropertiesUtil.ACCESS_KEY_ID;
        String accessKeySecret = ConstantPropertiesUtil.ACCESS_KEY_SECRET;
                //初始化
        DefaultAcsClient client = VideoClient.initVodClient(accessKeyId,accessKeySecret);
           //请求
        GetVideoPlayAuthRequest request = new GetVideoPlayAuthRequest();
        request.setVideoId(videoId);
               //响应
        GetVideoPlayAuthResponse response = client.getAcsResponse(request);
              //得到播放凭证
        String playAuth = response.getPlayAuth();
           //返回结果
        return R.ok().message("获取凭证成功").data("playAuth", playAuth);
    }



}
