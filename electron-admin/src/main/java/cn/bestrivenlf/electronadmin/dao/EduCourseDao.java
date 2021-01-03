package cn.bestrivenlf.electronadmin.dao;

import cn.bestrivenlf.electronadmin.entity.CoursePublishVo;
import cn.bestrivenlf.electronadmin.entity.EduCourse;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Component;

@Component
public interface EduCourseDao extends BaseMapper<EduCourse> {
    CoursePublishVo getCoursePublishVo(String id);
}
