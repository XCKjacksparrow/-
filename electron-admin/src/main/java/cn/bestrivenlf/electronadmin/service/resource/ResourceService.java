package cn.bestrivenlf.electronadmin.service.resource;


import cn.bestrivenlf.electronadmin.entity.File;

import java.util.List;

/**
 * @Author: liufan
 * @Date: 2019/7/31 14:26
 * @Description:
 */
public interface ResourceService {
    public boolean saveFile(File file);
    public List<File> getClassFile();
    public void deleteFile(String id);
}
