package cn.bestrivenlf.electronadmin.service.oss;

import org.springframework.web.multipart.MultipartFile;

public interface OssService {
    String uploadFile(MultipartFile file);
}
