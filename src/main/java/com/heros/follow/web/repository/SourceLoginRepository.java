package com.heros.follow.web.repository;

import com.heros.follow.web.po.SourceLogin;
import com.heros.follow.web.po.User;
import org.hibernate.annotations.NamedNativeQueries;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.Repository;

import java.util.List;

/**
 * Created by root on 2017/1/5.
 */
public interface SourceLoginRepository extends CrudRepository<SourceLogin, Long> {
    //    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN 'true' ELSE 'false' END FROM SourceLogin u WHERE u.name = ?1")
//    boolean existsName(String name);
    @Query(value = "ALTER TABLE Source_Login AUTO_INCREMENT =0",nativeQuery = true)
    void resetId();
//    List<SourceLogin> findByWhere(String where);
}
