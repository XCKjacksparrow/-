package cn.bestrivenlf.electronadmin.service.Comment;

import cn.bestrivenlf.electronadmin.dao.CommentDao;
import cn.bestrivenlf.electronadmin.entity.EduComment;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class CommentServieImpl extends ServiceImpl<CommentDao, EduComment> implements CommentService {

}
