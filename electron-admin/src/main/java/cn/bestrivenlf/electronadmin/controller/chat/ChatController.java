package cn.bestrivenlf.electronadmin.controller.chat;

import cn.bestrivenlf.electronadmin.entity.Chat;
import cn.bestrivenlf.electronadmin.service.content.ChatService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author: kazemi
 * @Date: 2020/09/06 15:44
 * @Description: to show all chats in a day
 */
@RestController
@Slf4j
@RequestMapping("/chat")
public class ChatController {

    @Autowired
    private ChatService chatService;

    @PostMapping("addOneChat")
    public void addChat(@RequestBody Chat chat){
        chatService.save(chat);
    }

    @GetMapping("getAllChat")
    public List<Chat> getAllChat(){
        return chatService.getAll();
    }
}
