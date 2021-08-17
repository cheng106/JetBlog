package com.cheng.jetblog.service;

import com.cheng.jetblog.po.User;

/**
 * @author cheng
 * @since 2021/8/17 23:10
 **/
public interface UserService {
    User checkUser(String username, String password);
}
