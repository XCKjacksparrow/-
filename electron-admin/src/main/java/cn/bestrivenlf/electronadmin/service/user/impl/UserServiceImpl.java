package cn.bestrivenlf.electronadmin.service.user.impl;

import cn.bestrivenlf.electronadmin.dao.user.UserDao;
import cn.bestrivenlf.electronadmin.entity.User;

import cn.bestrivenlf.electronadmin.entity.UserData;
import cn.bestrivenlf.electronadmin.listenter.SubjectExcelListener;

import cn.bestrivenlf.electronadmin.service.user.UserService;
import com.alibaba.excel.EasyExcel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: liufan
 * @Date: 2019/10/2 11:10
 * @Description:
 */
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDao userDao;
    @Override
    public  Map<String,Object> checkLogin(String userName, String password) {
        User user = userDao.getUserByUserName(userName);
        Map<String,Object> result = new HashMap<>();
        if(null!=user){
            if(user.getPassword().equals(password)){
                result.put("valid",true);
                result.put("msg","ok");
                result.put("object",user);
            }else{
                result.put("valid",false);
                result.put("msg","密码错误");
            }
        }else{
            result.put("valid",false);
            result.put("msg","用户不存在");
        }
        return result;
    }

    @Override
    public User getUserByUserName(String userName) {
        return userDao.getUserByUserName(userName);
    }

    @Override
    public List<String> getNameByIp(String ip) {
        return userDao.getNameByIp(ip);
    }

    @Override
    public Boolean setUserIp(String userIp,String userName) {
//        int temp = 1;
        int temp = userDao.setUserIp(userIp,userName);
        if(temp>0)
            return true;
        else
            return false;
    }
    @Override
    public Boolean deleteIp(String userIp) {
//        int temp = 1;
        int temp = userDao.deleteIp(userIp);
        if(temp>0)
            return true;
        else
            return false;
    }

    @Override
    public List<User> getuserlist() {
        List<User> userList=userDao.getuserList();
        return userList;
    }

    @Override
    public boolean importSubjectData(MultipartFile file) {
        try {

            //1 获取文件输入流
            InputStream inputStream = file.getInputStream();
            // 这里 需要指定读用哪个class去读，然后读取第一个sheet 文件流会自动关闭
            EasyExcel.read(inputStream, UserData.class, new SubjectExcelListener(userDao)).sheet().doRead();
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }

    }

    @Override
    public List<User> getonlineuserlist() {
        return userDao.getonlineuserlist();
    }

    @Override
    public List<User> getonlineteacherlist() {
        return userDao.getonlineteacherlist();
    }
}
