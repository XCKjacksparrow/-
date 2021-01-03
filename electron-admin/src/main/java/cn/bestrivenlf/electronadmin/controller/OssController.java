package cn.bestrivenlf.electronadmin.controller;



import cn.bestrivenlf.electronadmin.common.R;
import cn.bestrivenlf.electronadmin.service.oss.OssService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/eduoss/fileoss")
@CrossOrigin
public class OssController {

    @Autowired
    OssService ossService;


    @PostMapping
    public R uploadFile(MultipartFile file){

        String url =ossService.uploadFile(file);
        return R.ok().data("url",url);
    }

}
