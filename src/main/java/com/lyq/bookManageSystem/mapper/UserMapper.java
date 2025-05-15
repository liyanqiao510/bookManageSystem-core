package com.lyq.bookManageSystem.mapper;

import com.lyq.bookManageSystem.model.entity.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserMapper {

    //用户列表
    List<User> selectList(@Param("id") Long id,
                          @Param("userName") String userName,
                          @Param("name") String name,
                          @Param("role") Integer role,
                          @Param("isLocked") Boolean isLocked);

    int getTotal(@Param("id") Long id,
                 @Param("userName") String userName,
                 @Param("name") String name,
                 @Param("role") Integer role,
                 @Param("isLocked") Boolean isLocked);

    //新增用户
    int insertUser(User user);

    //更新用户 根据id
    int updateUser(User user);

    //删除用户 根据id
    int deleteUserById(Long id);

    int deleteAllById(@Param("ids") List<Long> ids);

    //id查找用户
    User selectUserById(Long id);

    //用户名查找用户
    User selectUserByUserName(String userName);




}