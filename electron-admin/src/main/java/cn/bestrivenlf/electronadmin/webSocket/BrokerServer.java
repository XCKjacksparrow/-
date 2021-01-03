package cn.bestrivenlf.electronadmin.webSocket;

import cn.bestrivenlf.electronadmin.config.MySpringConfigurator;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author: liufan
 * @Date: 2019/9/25 10:50
 * @Description:
 */
@Component
@ServerEndpoint(value = "/{localIp}/{identity}",configurator = MySpringConfigurator.class)
@Data
@Slf4j
public class BrokerServer {
    public static ConcurrentHashMap<String,Object> socketMap = new ConcurrentHashMap<>();
    public static int count = 0;
    @OnOpen
    public void onOpen(@PathParam("localIp") String localIp, @PathParam("identity") String identity,Session session){
        log.info("------Ip:"+localIp+",identity:"+identity+"建立了连接-----");
        String key = localIp+"-"+identity;
        this.socketMap.put(key,session);
        this.count += 1;
        //上线提醒
        online(localIp,identity,"online");
        //如果是老师，则开始进行遍历学生
    }
    @OnClose
    public void onClose(@PathParam("localIp") String localIp, @PathParam("identity") String identity,Session session){
        log.info("------Ip:"+localIp+",identity:"+identity+"关闭了连接-----");
        String key = localIp+"-"+identity;
        this.socketMap.remove(key);
        this.count -= 1;
        //下线提醒
        online(localIp,identity,"offline");
    }

    /**
     * 收到消息
     * @param message
     * @param session
     * @param identity
     */
    @OnMessage
    public synchronized void onMessage(String message,Session session, @PathParam("localIp") String localIp, @PathParam("identity") String identity){

        JSONObject json = JSON.parseObject(message);
        processCommand(json);
    }

    /**
     * 处理消息
     * @param json JSON对象的消息
     */
    public synchronized void processCommand(JSONObject json){

        log.info("---------------------------webSocket收到消息-----------------------");
        //首先拿到基础信息
        String fromIp = json.getString("fromIp");
        String fromIdentity = json.getString("fromIdentity");
        String toIp = json.getString("toIp");
        String toIdentity = json.getString("toIdentity");
        String type = json.getString("type");
        JSONObject data = json.getJSONObject("data");
        if(!type.equals("snapShot")){
            log.info("fromIp:"+fromIp+"\n");
            log.info("fromIdentity:"+fromIdentity+"\n");
            log.info("toIp:"+toIp+"\n");
            log.info("toIdentity:"+toIdentity+"\n");
            log.info("数据类型:"+type+"\n");
            log.info("数据体:"+data.toJSONString());
            log.info("-------------------------------------------------------------------");
        }
        Session session = null;
        //进行回应
        /**如果指定了toIP和toIdentity**/
        if((!toIp.equals("null"))&&(!toIdentity.equals("null"))){
            //发送给指定的人
            session = (Session) this.socketMap.get(toIp+"-"+toIdentity);
            sendMessage(session,json);
        }else{
            //没有指定发送者按照身份分发
            if(fromIdentity.equals("teacher")){
                    //发送给学生
                    sendMessageByIdentity(json, "student");
            }else if(fromIdentity.equals("student")){
                sendMessageByIdentity(json,"teacher");
            }else if(fromIdentity.equals("exp")){
                //实验环境，则发给同iP的socket
                sendMessageByIp(json,fromIp,fromIdentity);
                sendMessageByIp(json,fromIp,"student");

                sendMessageByIdentity(json,"teacher");

            }
        }
    }

    /**
     * 发送消息
     * @param session  socket
     * @param json 消息
     */
    public void sendMessage(Session session,JSONObject json){

            if(null == session){
                log.error("要发送的人还没上线,toIp="+json.getString("toIp")+",identity="+json.getString("toIdentity"));
            }else{

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                session.getBasicRemote().sendText(json.toJSONString());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();


            }


    }

    /**
     * 消息按身份发送  学生=》老师  老师=》学生
     * @param message
     * @param toIdentity
     */
    public void sendMessageByIdentity(JSONObject message,String toIdentity){
        for(String key:this.socketMap.keySet()){
            if(key.split("-")[1].equals(toIdentity)){
                Session session = (Session) this.socketMap.get(key);
                sendMessage(session,message);
            }
        }
    }

    /**
     * 消息转发给同IP不同身份的客户端
     * @param message
     * @param ip
     * @param excludeIdentity 排除的身份
     */
    public void sendMessageByIp(JSONObject message,String ip,String excludeIdentity){
        for(String key:this.socketMap.keySet()){
            if(key.split("-")[0].equals(ip)&&(!key.split("-")[1].equals(excludeIdentity))){
                Session session = (Session) this.socketMap.get(key);
                sendMessage(session,message);
            }
        }
    }

    /**
     * 上线\下线提醒方法
     * @param ip
     * @param identity
     */
    public void online(String ip,String identity,String type){
        if(identity.equals("teacher")){
            //是老师，则提醒给学生
            for(String key:this.socketMap.keySet()){
                if(key.split("-")[1].equals("student")){
                    //是学生
                    Session session =(Session) this.socketMap.get(key);
                    sendMessage(session,
                            getBaseData(
                                    ip,
                                    identity,
                                    key.split("-")[0],
                                    key.split("-")[1],
                                    type,
                                    new JSONObject()
                            )
                    );
                }
            }
        }else if (identity.equals("student")){
            //是学生，则提醒给老师
            for(String key:this.socketMap.keySet()){
                if(key.split("-")[1].equals("teacher")){
                    //是学生
                    Session session =(Session) this.socketMap.get(key);
                    sendMessage(session,
                            getBaseData(
                                    ip,
                                    identity,
                                    key.split("-")[0],
                                    key.split("-")[1],
                                    type,
                                    new JSONObject()
                            )
                    );
                }
            }
        }else if (identity.equals("exp")){
            //是实验环境

        }
    }

    /**
     * 封装基本的数据格式
     * @param fromIp
     * @param fromIdentity
     * @param toIp
     * @param toIdentity
     * @param type
     * @param data
     * @return
     */
    public JSONObject getBaseData(String fromIp,String fromIdentity,String toIp,String toIdentity,String type,
                                  JSONObject data){
        JSONObject message = new JSONObject();
        message.put("fromIp",fromIp);
        message.put("fromIdentity",fromIdentity);
        message.put("toIp",toIp);
        message.put("toIdentity",toIdentity);
        message.put("type",type);
        message.put("data",data);
        return message;
    }
}
