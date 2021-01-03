package cn.bestrivenlf.electronadmin.service.Video;

import cn.bestrivenlf.electronadmin.until.ConstantPropertiesUtil;


import com.aliyun.vod.upload.impl.UploadVideoImpl;
import com.aliyun.vod.upload.req.UploadStreamRequest;
import com.aliyun.vod.upload.resp.UploadStreamResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;


@Service
public class VideoAdminServiceImpl implements VideoAdminService {
    @Override
    public String addvideo(MultipartFile file) {

        try {
            InputStream inputStream = file.getInputStream();

            String originalFilename = file.getOriginalFilename();

            String title = originalFilename.substring(0,originalFilename.lastIndexOf("."));

            UploadStreamRequest request = new UploadStreamRequest(
                    ConstantPropertiesUtil.ACCESS_KEY_ID,
                    ConstantPropertiesUtil.ACCESS_KEY_SECRET,
                    title, originalFilename, inputStream);
            UploadVideoImpl uploader = new UploadVideoImpl();
            UploadStreamResponse response = uploader.uploadStream(request);

            String videoId = null;
            if (response.isSuccess()) {
                videoId= response.getVideoId();
            }else {
                videoId=response.getVideoId();
            }
            return videoId;
        } catch (IOException e) {
            return e.toString();
        }
    }


}
