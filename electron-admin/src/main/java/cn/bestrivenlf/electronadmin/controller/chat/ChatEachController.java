package cn.bestrivenlf.electronadmin.controller.chat;

import cn.bestrivenlf.electronadmin.entity.Chat;
import cn.bestrivenlf.electronadmin.entity.Chateach;
import cn.bestrivenlf.electronadmin.service.content.ChatEachService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/chateach")
public class ChatEachController {
    @Autowired
    private ChatEachService chatEachService;

    @PostMapping("addOneChat")
    public void addChat(@RequestBody Chateach chateach){
        chatEachService.save(chateach);
    }

    @GetMapping("getAllChat/{ip1}/{ip2}")
    public List<Chateach> getAllChat(@PathVariable("ip1") String ip1,@PathVariable("ip2") String ip2){
        return chatEachService.getAll(ip1,ip2);
    }
}
