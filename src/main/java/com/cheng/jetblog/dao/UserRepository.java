package com.cheng.jetblog.dao;

import com.cheng.jetblog.po.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author cheng
 * @since 2021/8/17 23:12
 **/
public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsernameAndPassword(String username, String password);
}
