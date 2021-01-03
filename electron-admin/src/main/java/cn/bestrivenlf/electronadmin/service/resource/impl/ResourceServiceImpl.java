package cn.bestrivenlf.electronadmin.service.resource.impl;

import cn.bestrivenlf.electronadmin.dao.resource.ResourceDao;
import cn.bestrivenlf.electronadmin.entity.File;
import cn.bestrivenlf.electronadmin.service.resource.ResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: liufan
 * @Date: 2019/7/31 14:27
 * @Description:
 */
@Service
public class ResourceServiceImpl implements ResourceService {
    @Autowired
    private ResourceDao resourceDao;
    @Override
    public boolean saveFile(File file) {
        return resourceDao.saveFile(file)>0?true:false;
    }

    @Override
    public List<File> getClassFile() {
        return resourceDao.getClassFile();
    }

    @Override
    public void deleteFile(String id) {
        resourceDao.deleteFile(id);
    }
}
