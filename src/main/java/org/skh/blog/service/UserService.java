package org.skh.blog.service;

import org.skh.blog.entity.User;

public interface UserService {
    User checkUser(String username,String password) ;
}
