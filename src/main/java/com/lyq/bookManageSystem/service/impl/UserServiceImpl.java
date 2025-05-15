package com.lyq.bookManageSystem.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lyq.bookManageSystem.model.DTO.UserDTO;
import com.lyq.bookManageSystem.converter.UserConverter;
import com.lyq.bookManageSystem.model.entity.User;
import com.lyq.bookManageSystem.common.exception.BusinessException;
import com.lyq.bookManageSystem.model.query.LoginQuery;
import com.lyq.bookManageSystem.mapper.UserMapper;
import com.lyq.bookManageSystem.service.UserService;


import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserMapper userMapper;

    @Override
    public PageInfo<UserDTO> getUserList(int pageNum, int pageSize, Long id, String userName, String name, Integer role, Boolean isLocked) {

        Integer totalCount = userMapper.getTotal(id, userName, name, role, isLocked);

        Integer totalPages = (int) Math.ceil((double) totalCount / pageSize);

        // 修正 页码超出时, 使用最大页码
        if (totalPages > 0 && pageNum > totalPages) {
            pageNum = totalPages;
            System.out.println("执行页码修正");
        }

        // 1. 启动分页
        PageHelper.startPage(pageNum, pageSize);

        // 2. 执行查询（返回的是 UserDO 列表）
        List<User> userList = userMapper.selectList(id, userName, name, role, isLocked);

        // 3. 将 List<UserDO> 转换为 List<UserDTO>
        List<UserDTO> userDTOList = userList.stream()
                .map(UserConverter::toDTO)  // 使用转换器
                .collect(Collectors.toList());

        // 4. 获取分页信息（PageHelper 自动生成）
        PageInfo<User> pageInfo = new PageInfo<>(userList);

        // 5. 构造新的 PageInfo<UserDTO>（保留分页数据）
        PageInfo<UserDTO> dtoPageInfo = new PageInfo<>();
        BeanUtils.copyProperties(pageInfo, dtoPageInfo); // 拷贝分页元数据
        dtoPageInfo.setList(userDTOList);                // 替换为 DTO 列表

        return dtoPageInfo;
    }

    @Override
    @Transactional
    public void addUser(User user) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String hashedPassword = encoder.encode("Cs123456");
        user.setPassword(hashedPassword);
        int rows = userMapper.insertUser(user);
        // 执行插入
        if (rows != 1) {
            throw new BusinessException(500, "用户创建失败");
        }

    }

    @Override
    @Transactional
    public void updateUser(Long id, User user) {
        user.setId(id);
       int rows =  userMapper.updateUser(user);
        if (rows != 1) {
            throw new BusinessException(500, "用户更新失败");
        }

    }

    @Override
    @Transactional
    public void deleteUserById(Long id) {
        int rows = userMapper.deleteUserById(id);
        if (rows != 1) {
            throw new BusinessException(500, "用户删除失败");
        }

    }

    @Override
    @Transactional
    // 批量删除（根据ORM选择最优方案）
    public void deleteAllById(List<Long> ids) {
        if (ids.size() == 1) {
            deleteUserById(ids.get(0)); // 兜底逻辑
            return;
        }
        // 方案1: MyBatis批量删除
        userMapper.deleteAllById(ids);

    }

    @Override
    public UserDTO getUserById(Long id) {

       return UserConverter.toDTO(userMapper.selectUserById(id)) ;

    }


    @Override
    public UserDTO getUserByUserName(String userName) {
        User user = userMapper.selectUserByUserName(userName);
        return user == null ? null : UserConverter.toDTO(user);
    }

    @Override
    public UserDTO validateUser(LoginQuery loginQuery) {
        User user = userMapper.selectUserByUserName(loginQuery.getUserName());
        if(user == null){
            return null; // 用户名不存在
        }else{
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            if (encoder.matches(loginQuery.getPassword(), user.getPassword())) {
                return UserConverter.toDTO(user); // 验证成功
            }else{
                return null;   // 验证失败
            }
        }


    }


}
