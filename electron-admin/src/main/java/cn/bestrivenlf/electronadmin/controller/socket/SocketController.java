package cn.bestrivenlf.electronadmin.controller.socket;

import cn.bestrivenlf.electronadmin.entity.AjaxResult;
import cn.bestrivenlf.electronadmin.service.user.UserService;
import cn.bestrivenlf.electronadmin.webSocket.BrokerServer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: liufan
 * @Date: 2019/10/2 12:45
 * @Description:
 */
@RestController
@RequestMapping("/socket")
public class SocketController {

    @Autowired
    private UserService userService;

    @RequestMapping("/getPeoples")
    public AjaxResult getPeoples(){
        List<Map<String,String>> teachers = new ArrayList<>();
        List<Map<String,String>> students = new ArrayList<>();
        BrokerServer.socketMap.keySet().forEach(key->{
            Map<String,String> temp = new HashMap();
            temp.put("ip",key.split("-")[0]);
            temp.put("identity",key.split("-")[1]);
            String username = userService.getNameByIp(key.split("-")[0]).get(0);
            temp.put("username",username);
            if(key.split("-")[1].equals("teacher")){
                teachers.add(temp);
            }else if(key.split("-")[1].equals("student")){
                students.add(temp);
            }
        });
        JSONObject json = new JSONObject();
        json.put("teachers",teachers);
        json.put("students",students);
        return new AjaxResult(200,json);
    }
}
