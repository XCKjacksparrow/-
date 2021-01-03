package cn.bestrivenlf.electronadmin.service.EduChapter;

import cn.bestrivenlf.electronadmin.entity.EduChapter;
import cn.bestrivenlf.electronadmin.entity.chapter.ChapterVo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface EduChapterService extends IService<EduChapter> {
    List<ChapterVo> getChapterVideoById(String courseId);
}
