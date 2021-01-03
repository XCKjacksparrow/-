package cn.bestrivenlf.electronadmin.service.content;

import cn.bestrivenlf.electronadmin.entity.Chateach;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChatEachRepository extends JpaRepository<Chateach,Integer> {




    @Query(value="SELECT * FROM t_chateach WHERE (ip1=:ip1 AND ip2=:ip2) OR (ip1=:ip2 AND ip2=:ip1)", nativeQuery = true)
    List<Chateach> findAllEach(@Param("ip1")String ip1, @Param("ip2")String ip2);
}
