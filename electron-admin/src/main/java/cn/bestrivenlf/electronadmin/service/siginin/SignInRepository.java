package cn.bestrivenlf.electronadmin.service.siginin;

import cn.bestrivenlf.electronadmin.entity.SignIn;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface SignInRepository extends JpaRepository<SignIn,Integer> {

    @Query(value = "SELECT * FROM t_signin WHERE user_name = ?1",nativeQuery = true)
    SignIn getByUserName(@Param("userName") String userName);
}
