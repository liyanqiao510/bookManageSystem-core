package com.lyq.bookManageSystem.service;

import com.github.pagehelper.PageInfo;
import com.lyq.bookManageSystem.model.DTO.UserDTO;
import com.lyq.bookManageSystem.model.entity.User;
import com.lyq.bookManageSystem.model.query.LoginQuery;

import java.util.List;

public interface UserService {

    //用户列表
    PageInfo<UserDTO> getUserList(int pageNum, int pageSize, Long id, String userName, String name, Integer role, Boolean isLocked);

    //新增用户
    void addUser(User user);

    //更新用户
    void updateUser(Long id, User user);

    int deleteUser(String ids);

    //id查找用户
    UserDTO getUserById(Long id);

    //名称查找用户
    UserDTO getUserByUserName(String userName);

    UserDTO validateUser(LoginQuery loginQuery);
}
