package com.lyq.bookManageSystem.mapper;

import com.lyq.bookManageSystem.entity.User;

import java.util.List;

public interface UserMapper {

    //查找用户列表
    List<User> selectList();

    //添加用户
    int insertUser(User user);

    //更新用户 根据id
    int updateUser(User user);

    //删除用户 根据id
    int deleteUserById(Integer id);

    //id查找用户
    User selectUserById(Integer id);

    //用户名查找用户
    User selectUserByUserName(String userName);




}