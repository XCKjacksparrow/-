package cn.bestrivenlf.electronadmin.service.content;

import cn.bestrivenlf.electronadmin.entity.Chat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class ChatService {
    @Autowired
    private ChatRepository chatRepository;

    public List<Chat> getAll() {
        // 查询当天的
        List<Chat> chatList = chatRepository.findAll();
        List<Chat> result = new ArrayList<>();
        for (Chat chat : chatList){
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

    public void save(Chat chat) {
        chat.setGmtCreate(System.currentTimeMillis());
        chat.setGmtModified(System.currentTimeMillis());
        chatRepository.save(chat);
    }
}
