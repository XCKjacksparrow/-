package cn.bestrivenlf.electronadmin.dao.user;

import cn.bestrivenlf.electronadmin.entity.User;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author: liufan
 * @Date: 2019/10/2 11:10
 * @Description:
 */
@Component
public interface UserDao {
    public User getUserByUserName(String userName);
    public List<String> getNameByIp(String ip);

    public int setUserIp(@Param("userIp") String userIp,@Param("userName") String userName);
    public int deleteIp(@Param("userIp") String userIp);

    public List<User> getuserList();

    public void save(@Param("user") User user);

    List<User> getonlineuserlist();

    List<User> getonlineteacherlist();
}
