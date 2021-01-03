package cn.bestrivenlf.electronadmin.service.siginin;

import cn.bestrivenlf.electronadmin.controller.SignVo;
import cn.bestrivenlf.electronadmin.entity.SignIn;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.*;

@Component
public class SignInService {

    @Autowired
    private SignInRepository signInRepository;

    public void SignInOrUpdate(String userName, String ip) {
        SignIn signIn = signInRepository.getByUserName(userName);
        if (null == signIn) {
            // 首次登录
            signIn = new SignIn();
            signIn.setIp(ip);
            signIn.setUserName(userName);
            signIn.setGmtCreate(System.currentTimeMillis());
            signIn.setGmtModified(signIn.getGmtCreate());
            signInRepository.save(signIn);
        } else {
            // 判断今天是否登录
            Date date = new Date(signIn.getGmtModified());
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            String formatdate = df.format(date);

            Date dateCurrent = new Date();
            String formatdateCurrent = df.format(dateCurrent);

            // 刷新登录时间
            signIn.setGmtModified(System.currentTimeMillis());
            signInRepository.save(signIn);
        }
    }

    public List<SignVo> getAllSignInList(){
        List<SignIn> all = signInRepository.findAll();
        List<SignVo> result = new ArrayList<>();
        for (SignIn item : all){
            SignVo signVo = new SignVo();
            signVo.setName(item.getUserName());

            // 与今天的时间进行对比 判断是否签到
            Date date = new Date(item.getGmtModified());
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            String formatdate = df.format(date);
            Date dateCurrent = new Date();
            String formatdateCurrent = df.format(dateCurrent);
            if (formatdate.equals(formatdateCurrent)){
                // 说明已经签到
                signVo.setIsSign("是");
                SimpleDateFormat dfNew = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                signVo.setSignInTime(dfNew.format(date));
            }else{
                signVo.setIsSign("否");
                signVo.setSignInTime("");
            }
            result.add(signVo);
        }
        return result;
    }
}
