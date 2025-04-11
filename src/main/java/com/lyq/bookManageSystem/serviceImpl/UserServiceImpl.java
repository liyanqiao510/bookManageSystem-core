package com.lyq.bookManageSystem.serviceImpl;

import com.github.pagehelper.PageHelper;
import com.lyq.bookManageSystem.entity.User;
import com.lyq.bookManageSystem.exception.BusinessException;
import com.lyq.bookManageSystem.mapper.UserMapper;
import com.lyq.bookManageSystem.service.UserService;


import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import javax.annotation.Resource;
import java.util.List;


@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserMapper userMapper;

    @Override
    public List<User> getUserList(int pageNum, int pageSize) {
        // 关键！这行代码会让接下来的查询自动分页
        PageHelper.startPage(pageNum, pageSize);
        return userMapper.selectList();
    }

    @Override
    public void addUser(User user) {


        int rows = userMapper.insertUser(user);
        // 执行插入
        if (rows != 1) {
            throw new BusinessException(500, "用户创建失败");
        }

    }

    @Override
    public void updateUser(User user) {
       int rows =  userMapper.updateUser(user);
        if (rows != 1) {
            throw new BusinessException(500, "用户更新失败");
        }

    }

    @Override
    public void deleteUserById(Integer id) {
        int rows = userMapper.deleteUserById(id);
        if (rows != 1) {
            throw new BusinessException(500, "用户删除失败");
        }

    }

    @Override
    public User getUserById(Integer id) {
       return  userMapper.selectUserById(id);

    }

    @Override
    public User getUserByUserName(String userName) {
       return  userMapper.selectUserByUserName(userName);

    }




}
