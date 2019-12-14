package org.skh.blog.dao;

import org.skh.blog.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
//实体属性 主键类型 接口继承
public interface UserRepository extends JpaRepository<User,Long> {
    User findByUsernameAndPassword(String username,String password) ;

}
