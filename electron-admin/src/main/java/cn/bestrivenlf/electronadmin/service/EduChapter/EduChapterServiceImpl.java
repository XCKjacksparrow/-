package cn.bestrivenlf.electronadmin.service.EduChapter;

import cn.bestrivenlf.electronadmin.dao.EduChapterDao;
import cn.bestrivenlf.electronadmin.entity.EduChapter;
import cn.bestrivenlf.electronadmin.entity.EduVideo;
import cn.bestrivenlf.electronadmin.entity.chapter.ChapterVo;
import cn.bestrivenlf.electronadmin.entity.chapter.VideoVo;
import cn.bestrivenlf.electronadmin.service.EduVideo.EduVideoService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class EduChapterServiceImpl extends ServiceImpl<EduChapterDao, EduChapter> implements EduChapterService{

    @Autowired

    EduVideoService eduVideoService;
    @Override
    public List<ChapterVo> getChapterVideoById(String courseId) {
        /**
         * 获取所有章节
         */
        QueryWrapper<EduChapter> wrapper = new QueryWrapper<>();

        wrapper.eq("courseId", courseId);

        List<EduChapter> chapters = baseMapper.selectList(wrapper);


        /**
         * 获取所有小结
         */

        QueryWrapper<EduVideo> videoQueryWrapper = new QueryWrapper<>();

        videoQueryWrapper.eq("courseId",courseId);

        List<EduVideo> eduVideoList = eduVideoService.list(videoQueryWrapper);

        List<ChapterVo> finalList = new ArrayList<>();

        for (int i = 0; i < chapters.size(); i++) {
            ChapterVo chapterVo = new ChapterVo();
            EduChapter chapter = chapters.get(i);
            BeanUtils.copyProperties(chapter,chapterVo);
            finalList.add(chapterVo);


            List<VideoVo> videoVoList = new ArrayList<>();
            for (int m = 0; m < eduVideoList.size(); m++) {
                EduVideo eduVideo = eduVideoList.get(m);
                if(eduVideo.getChapterId().equals(chapter.getId())){
                    VideoVo videoVo = new VideoVo();
                    BeanUtils.copyProperties(eduVideo,videoVo);
                    videoVoList.add(videoVo);
                }
            }

            chapterVo.setChildren(videoVoList);
        }


        return finalList;
    }
}
