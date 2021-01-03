package cn.bestrivenlf.electronadmin.dao.resource;


import cn.bestrivenlf.electronadmin.entity.File;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author: liufan
 * @Date: 2019/7/31 14:27
 * @Description:
 */
@Component
public interface ResourceDao {
    public int saveFile(File file);
    public List<File> getClassFile();
    public void deleteFile(String id);
}
