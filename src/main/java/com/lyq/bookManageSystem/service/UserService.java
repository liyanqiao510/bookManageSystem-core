package com.lyq.bookManageSystem.service;

import com.lyq.bookManageSystem.entity.User;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface UserService {

    List<User> getUserList(int pageNum,int pageSize);

    void addUser(User user);

    void updateUser(User user);

    void deleteUserById(Integer id);

    User getUserById(Integer id);

    User getUserByUserName(String userName);
}
