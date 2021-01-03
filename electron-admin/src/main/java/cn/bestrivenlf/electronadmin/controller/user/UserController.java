package cn.bestrivenlf.electronadmin.controller.user;

import cn.bestrivenlf.electronadmin.common.R;
import cn.bestrivenlf.electronadmin.controller.SignVo;
import cn.bestrivenlf.electronadmin.entity.AjaxResult;
import cn.bestrivenlf.electronadmin.entity.SignIn;
import cn.bestrivenlf.electronadmin.entity.User;
import cn.bestrivenlf.electronadmin.service.siginin.SignInService;
import cn.bestrivenlf.electronadmin.service.user.UserService;
import cn.bestrivenlf.electronadmin.service.user.uploadUserService;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * @Author: liufan
 * @Date: 2019/9/24 10:38
 * @Description:
 */
@RestController
@Slf4j
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private SignInService signInService;

    @RequestMapping("/login")
    public Object login(){
        return new AjaxResult(200);
    }

    @RequestMapping("/userLogin")
    public Object userLogin(@RequestBody JSONObject json){
        //首先拿到用户密码
        String userName = json.getString("username");
        String password = json.getString("password");
        String userIp = json.getString("userip");
        //核对数据库是否存在该用户
        Map<String,Object> result = userService.checkLogin(userName,password);
        if((boolean)result.get("valid")){
            Boolean userState = userService.setUserIp(userIp,userName);
            // 签到
            signInService.SignInOrUpdate(userName,userIp);
            log.info("-------userIp:"+userIp+"   "+userState+"--------\n");
            return new AjaxResult(200,(User)result.get("object"));
        }else{
            return new AjaxResult(4001,(String)result.get("msg"));
        }
    }

    @RequestMapping("/info")
    public AjaxResult getUserInfo(String userName){
        return new AjaxResult(200,userService.getUserByUserName(userName));
    }
    @RequestMapping("/logout")
    public Object userLogout(@RequestBody JSONObject json){
        String userIp = json.getString("userip");
        Boolean userState = userService.deleteIp(userIp);
        log.info("-------删除Ip:"+userIp+"   "+userState+"--------\n");
        return new AjaxResult(200,null);
    }

    public Object userModel(@RequestBody JSONObject json){
        return null;
    }


    @GetMapping("getSignList")
    public List<SignVo> getSignList(){
        List<SignVo> allSignInList = signInService.getAllSignInList();
        return allSignInList;
    }


    @GetMapping("getuserlist")
    public List<User> getuserlist(){
        List<User> userList= userService.getuserlist();
        
        return userList;
    }

    @GetMapping("getonlineuserlist")
    public List<User> getonlineuserlist(){
        List<User> userList= userService.getonlineuserlist();

        return userList;
    }

    @GetMapping("getonlineteacherlist")
    public List<User> getonlineteacherlist(){
        List<User> userList= userService.getonlineteacherlist();

        return userList;
    }


    @PostMapping("addSubject")
    public boolean addSubject(MultipartFile file){
        //1 获取上传的excel文件 MultipartFile
//返回错误提示信息
        boolean b= userService.importSubjectData(file);
//判断返回集合是否为空
        return b;
    }


    @GetMapping("getuserlistById/{name}")
    public int getuserlistById(@PathVariable("name") String name){
        User uesr = userService.getUserByUserName(name);
        int id = uesr.getId();
        return id;
    }

    @GetMapping("getuserByIp/{ip}")
    public String getuserByIp(@PathVariable("ip") String ip){
        return userService.getNameByIp(ip).get(0);
    }


}
