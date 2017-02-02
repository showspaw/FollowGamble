package com.heros.follow.web.repository;

import com.heros.follow.web.po.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by root on 2017/1/5.
 */
public interface UserRepository extends CrudRepository<User, Long> {
    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN 'true' ELSE 'false' END FROM User u WHERE u.account = ?1")
    boolean existsAccount(String account);

    User findByAccount(String account);
}
