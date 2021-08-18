package com.cheng.jetblog.service.impl;

import com.cheng.jetblog.dao.UserRepository;
import com.cheng.jetblog.po.User;
import com.cheng.jetblog.service.UserService;
import com.cheng.jetblog.utils.EncryptStr;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author cheng
 * @since 2021/8/17 23:11
 **/
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public User checkUser(String username, String password) {
        return userRepository.findByUsernameAndPassword(username, EncryptStr.encryptShortSHE256(password));
    }
}
