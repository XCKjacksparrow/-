package cn.bestrivenlf.electronadmin.service.content;

import cn.bestrivenlf.electronadmin.entity.Chateach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class ChatEachService {

    @Autowired
    private ChatEachRepository chatEachRepository;

    public void save(Chateach chateach) {
        chateach.setGmtCreate(System.currentTimeMillis());
        chateach.setGmtModified(System.currentTimeMillis());
        chatEachRepository.save(chateach);
    }


    public List<Chateach> getAll(String ip1, String ip2) {
        // 查询当天的
        List<Chateach> chatList = chatEachRepository.findAllEach(ip1,ip2);
        System.out.println(chatList);
        List<Chateach> result = new ArrayList<>();
        for (Chateach chat : chatList){
            Date date = new Date(chat.getGmtCreate());
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            String formatdate = df.format(date);
            Date dateCurrent = new Date();
            String formatdateCurrent = df.format(dateCurrent);
            if (formatdate.equals(formatdateCurrent)){
                // 说明是当天的
                result.add(chat);
            }
        }
        return result;
    }
}
