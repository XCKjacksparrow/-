package cn.bestrivenlf.electronadmin.service.user;

import cn.bestrivenlf.electronadmin.entity.User;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * @Author: liufan
 * @Date: 2019/10/2 11:09
 * @Description:
 */
public interface UserService {
    public Map<String,Object> checkLogin(String username, String password);

    public User getUserByUserName(String userName);

    public List<String> getNameByIp(String ip);

    public Boolean setUserIp(String userIp,String userName);
    public Boolean deleteIp(String userIp);

    List<User> getuserlist();


    boolean importSubjectData(MultipartFile file);

    List<User> getonlineuserlist();

    List<User> getonlineteacherlist();
}
