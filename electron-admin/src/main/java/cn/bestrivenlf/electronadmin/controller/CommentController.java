package cn.bestrivenlf.electronadmin.controller;


import cn.bestrivenlf.electronadmin.common.R;
import cn.bestrivenlf.electronadmin.entity.EduComment;
import cn.bestrivenlf.electronadmin.service.Comment.CommentService;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/educomment/comment")
@CrossOrigin
public class CommentController {

    @Autowired
    private CommentService commentService;


    /**
     * 分页查询评论
     */
    @GetMapping("pagecommentlist/{page}/{limit}")
    public R pagecommentlist(@PathVariable Long page, @PathVariable Long limit){
        System.out.println(666666);
        Page<EduComment> pageParam = new Page<>(page, limit);
        commentService.page(pageParam,null);
        List<EduComment> commentList = pageParam.getRecords();
        Map<String, Object> map = new HashMap<>();
        map.put("items", commentList);
        map.put("current", pageParam.getCurrent());
        map.put("pages", pageParam.getPages());
        map.put("size", pageParam.getSize());
        map.put("total", pageParam.getTotal());
        map.put("hasNext", pageParam.hasNext());
        map.put("hasPrevious", pageParam.hasPrevious());
        return R.ok().data(map);
    }

    @ApiOperation(value = "添加评论")
    @PostMapping("auth/save")
    public R save(@RequestBody EduComment comment) {
        List<EduComment> list = commentService.list(null);
        int size = list.size();
        comment.setId(size++);

        commentService.save(comment);
        return R.ok();
    }






}
