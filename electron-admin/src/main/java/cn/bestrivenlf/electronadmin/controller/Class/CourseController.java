package cn.bestrivenlf.electronadmin.controller.Class;

import cn.bestrivenlf.electronadmin.common.R;
import cn.bestrivenlf.electronadmin.entity.CoursePublishVo;
import cn.bestrivenlf.electronadmin.entity.EduChapter;
import cn.bestrivenlf.electronadmin.entity.EduCourse;
import cn.bestrivenlf.electronadmin.entity.EduVideo;
import cn.bestrivenlf.electronadmin.entity.chapter.ChapterVo;
import cn.bestrivenlf.electronadmin.service.EduChapter.EduChapterService;
import cn.bestrivenlf.electronadmin.service.EduCourse.EduCourseService;
import cn.bestrivenlf.electronadmin.service.EduVideo.EduVideoService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("couse")
public class CourseController {


    @Autowired
    EduCourseService eduCourseService;

    @Autowired
    EduVideoService eduVideoService;

    @Autowired
    EduChapterService eduChapterService;

    //添加课程基本信息的方法
    @PostMapping("addCourseInfo")
    public Boolean addCourseInfo(@RequestBody EduCourse eduCourse) {

        //返回添加之后课程id，为了后面添加大纲使用
        boolean save = eduCourseService.save(eduCourse);
        if (save)
        {
            return true;
        }
        return false;

    }


    @PostMapping("savechapter")
    public boolean savechapter(@RequestBody EduChapter eduChapter){

        boolean save = eduChapterService.save(eduChapter);

        return save;

    }

    //根据章节id查询
    @GetMapping("getChapterInfo/{chapterId}")
    public EduChapter getChapterInfo(@PathVariable String chapterId) {
        EduChapter eduChapter = eduChapterService.getById(chapterId);
        return eduChapter;
    }

    //修改章节
    @PostMapping("updateChapter")
    public R updateChapter(@RequestBody EduChapter eduChapter) {
        eduChapterService.updateById(eduChapter);
        return R.ok();
    }
    @PostMapping("addVideo")
    public R addVideo(@RequestBody EduVideo  eduVideo){
        eduVideoService.save(eduVideo);
        return R.ok();
    }

    @GetMapping("getChapterVideoById/{courseId}")
    public R getChapterVideoById(@PathVariable String courseId){
        List<ChapterVo> list = eduChapterService.getChapterVideoById(courseId);
        return R.ok().data("items",list);
    }

    @ApiOperation(value = "根据ID获取课程发布信息")
    @GetMapping("getCoursePublishVoById/{id}")
    public R getCoursePublishVoById(
            @ApiParam(name = "id", value = "课程ID", required = true)
            @PathVariable String id){
        EduCourse course = eduCourseService.getById(id);
        return R.ok().data("item", course);
    }



    @PostMapping("publishCourseInfo/{id}")
    public R publishCourseInfo(@PathVariable Integer id){
        EduCourse eduCourse = new EduCourse();
        eduCourse.setId(id);
        eduCourse.setStatus("Normal");
        eduCourseService.updateById(eduCourse);
        return R.ok();
    }






}
