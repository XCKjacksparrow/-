package cn.bestrivenlf.electronadmin.controller.resource;

import cn.bestrivenlf.electronadmin.common.Const;
import cn.bestrivenlf.electronadmin.entity.AjaxResult;
import cn.bestrivenlf.electronadmin.service.resource.ResourceService;
import cn.bestrivenlf.electronadmin.util.MyUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static cn.bestrivenlf.electronadmin.common.Const.NORMALERROR_CODE;
import static cn.bestrivenlf.electronadmin.common.Const.SUCCESS_CODE;

/**
 * @Author: liufan
 * @Date: 2019/5/20 14:30
 * @Description:
 */
@RestController
@RequestMapping("/upload")
@Slf4j
public class UploadController {
    @Value("${file.uploadAbsolutePath}")
    String uploadAbsolutePath;
    @Value("${file.uploadAccessPath}")
    String uploadAccessPath;

    @Autowired
    private ResourceService resourceService;
    /**
     * 文件上传接口
     * @param
     * @param session
     * @return
     */
    @PostMapping("/uploadFile")
    public Object uploadImg(HttpServletRequest request, HttpSession session, String classId){
        MultipartFile multipartFile = null;
        //设置编码
        try {
            request.setCharacterEncoding("UTF-8");
            //初始化返回map
            Map<String, Object> json = new HashMap<String, Object>();
            //获取request
            MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
            multipartFile = multipartRequest.getFileMap().get("file");
            //获得上传的文件名
            String fileName = multipartFile.getOriginalFilename();
            InputStream inputStream;
            //获取文件的后缀
            String type = fileName.substring(fileName.lastIndexOf("."));
            //生成新的随机名称（完整）
            String newFileName = MyUtil.getUuid() + type;
            //虚拟路径
            String address = uploadAccessPath + newFileName;
            //磁盘路径
            String diskAddress = uploadAbsolutePath + newFileName;
            cn.bestrivenlf.electronadmin.entity.File fileEntity = new cn.bestrivenlf.electronadmin.entity.File();
            Map<String, Object> resourceMap = null;
            //获得文件的流
            inputStream = multipartFile.getInputStream();
            fileEntity.setInitialName(fileName);
            fileEntity.setType(type);
            fileEntity.setDiskAddress(diskAddress);
            fileEntity.setAddress(address);
            fileEntity.setUuidName(newFileName);
            fileEntity.setInputStream(inputStream);
            //生成一个临时的复制对象用来存放文件流扔进session
            List<cn.bestrivenlf.electronadmin.entity.File> fileList = new ArrayList<>();
            fileList.add(fileEntity);
            MyUtil.saveFile(fileList);
            fileEntity.setInputStream(null);
            resourceService.saveFile(fileEntity);
            return new AjaxResult(SUCCESS_CODE,fileEntity);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return new AjaxResult(NORMALERROR_CODE,"不支持的编码:"+e.getMessage());
        } catch (NullPointerException e){
            e.printStackTrace();
            return new AjaxResult(NORMALERROR_CODE,"上传失败(NullPoint):"+e.getMessage());
        } catch (Exception e){
            e.printStackTrace();
            return new AjaxResult(NORMALERROR_CODE,"文件上传失败:"+e.getMessage());
        }

    }

    /**
     * 下载文件
     * @param request
     * @param response
     * @param
     * @return
     * @throws UnsupportedEncodingException
     */
    //实现Spring Boot 的文件下载功能，映射网址为/download
    @RequestMapping("/download")
    public String downloadFile(HttpServletRequest request,
                               HttpServletResponse response, String folder, String fileName) throws
            UnsupportedEncodingException {
//        String folder = params.getString("folder");
//        String fileName = params.getString("fileName");
        // 获取指定目录下的第一个文件
        folder = uploadAbsolutePath;
        File scFileDir = new File(folder);
        File TrxFiles[] = scFileDir.listFiles();
        log.info("下载目录:"+folder+",下载文件:"+fileName+",该目录下共有"+TrxFiles.length+"个文件");
        File target = null;
        for(int i = 0;i<TrxFiles.length;i++){
            if(TrxFiles[i].getName().equals(fileName)){
                target = TrxFiles[i];
            }
        }
//        String fileName = TrxFiles[0].getName(); //下载的文件名
        // 如果文件名不为空，则进行下载
        if (target != null) {
            //设置文件路径
            String realPath = folder;
            File file = new File(realPath, fileName);
            // 如果文件名存在，则进行下载
            if (file.exists()) {
                // 配置文件下载
                response.setHeader("content-type", "application/octet-stream;charset=UTF-8");
                response.setContentType("application/octet-stream;charset=UTF-8");
                // 下载文件能正常显示中文
                response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
                // 实现文件下载
                byte[] buffer = new byte[1024];
                FileInputStream fis = null;
                BufferedInputStream bis = null;
                try {
                    fis = new FileInputStream(file);
                    bis = new BufferedInputStream(fis);
                    OutputStream os = response.getOutputStream();
                    int i = bis.read(buffer);
                    while (i != -1) {
                        os.write(buffer, 0, i);
                        i = bis.read(buffer);
                    }
                    log.info("下载成功");
                }
                catch (Exception e) {
                    log.error("下载失败:"+e.getMessage());
                }
                finally {
                    if (bis != null) {
                        try {
                            bis.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (fis != null) {
                        try {
                            fis.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }else{

        }
        return null;
    }

    /**
     * 获取课堂文件
     * @param lessonId
     * @return
     */
    @RequestMapping("/getResourceList")
    public Object getResourceListByClassId(String lessonId){
        try{
            return new AjaxResult(SUCCESS_CODE,resourceService.getClassFile());
        }catch(Exception e){
            e.printStackTrace();
            return new AjaxResult(NORMALERROR_CODE,e.getMessage());
        }
    }

    /**
     * 删除文件
     * @param id
     * @return
     */
    @RequestMapping("/deleteResource")
    public Object deleteResourceById(String id){
        try{
            resourceService.deleteFile(id);
            return new AjaxResult(SUCCESS_CODE);
        }catch(Exception e){
            e.printStackTrace();
            return new AjaxResult(NORMALERROR_CODE,e.getMessage());
        }
    }
}
