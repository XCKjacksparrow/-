package cn.bestrivenlf.electronadmin.controller.front;

import cn.bestrivenlf.electronadmin.common.R;
import cn.bestrivenlf.electronadmin.entity.EduCourse;
import cn.bestrivenlf.electronadmin.entity.chapter.ChapterVo;
import cn.bestrivenlf.electronadmin.service.EduChapter.EduChapterService;
import cn.bestrivenlf.electronadmin.service.EduCourse.EduCourseService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RequestMapping("/eduservice/index")
@RestController
@CrossOrigin
public class IndexController {


    @Autowired
    EduCourseService eduCourseService;

    @Autowired
    EduChapterService eduChapterService;

    @GetMapping("index")
    public R index() {
                //查询前8条热门课程
        QueryWrapper<EduCourse> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("id");
        wrapper.last("limit 8");
        List<EduCourse> eduList = eduCourseService.list(wrapper);

        return R.ok().data("eduList",eduList);
    }

    @ApiOperation(value = "根据ID查询课程")
    @GetMapping("getCourseWeb/{courseId}")
    public R getById(
            @ApiParam(name = "courseId", value = "课程ID", required = true)
            @PathVariable String courseId,
            HttpServletRequest request){
        //查询课程信息和讲师信息
        EduCourse eduCourse = eduCourseService.getById(courseId);
        //查询当前课程的章节信息
        List<ChapterVo> chapterVoList = eduChapterService.getChapterVideoById(courseId);
        return R.ok().data("course", eduCourse).data("chapterVoList",chapterVoList);
    }
}
