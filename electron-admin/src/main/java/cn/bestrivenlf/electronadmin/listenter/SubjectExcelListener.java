package cn.bestrivenlf.electronadmin.listenter;

import cn.bestrivenlf.electronadmin.dao.user.UserDao;
import cn.bestrivenlf.electronadmin.entity.User;
import cn.bestrivenlf.electronadmin.entity.UserData;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import org.springframework.beans.factory.annotation.Autowired;

public class SubjectExcelListener extends AnalysisEventListener<UserData> {

    public UserDao userDao;
    public SubjectExcelListener() {}
    //创建有参数构造，传递subjectService用于操作数据库
    public SubjectExcelListener(UserDao userDao) {
        this.userDao = userDao;
    }
    @Override
    public void invoke(UserData userData, AnalysisContext analysisContext) {
        System.out.println(userData);
        if(userData == null){
            return;
        }
//        User exist = this.isExist(userData.getUserName());
//        if(exist == null){
            User user = new User();
            user.setId(userData.getId());
            user.setUserName(userData.getUserName());
            user.setPassword(userData.getPassword());
            user.setRole(userData.getRole());
            userDao.save(user);
        //}
    }

    //判断是否存在

//    private User isExist(String userName){
//        User userByUserName = userDao.getUserByUserName(userName);
//        return userByUserName;
//    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }
}
