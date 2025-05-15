package com.lyq.bookManageSystem.controller;

import com.github.pagehelper.PageInfo;
import com.lyq.bookManageSystem.model.DTO.UserDTO;
import com.lyq.bookManageSystem.model.VO.UserVO;
import com.lyq.bookManageSystem.common.response.ResponseResult;
import com.lyq.bookManageSystem.converter.UserConverter;
import com.lyq.bookManageSystem.model.entity.User;
import com.lyq.bookManageSystem.model.query.LoginQuery;
import com.lyq.bookManageSystem.service.UserService;

import com.lyq.bookManageSystem.util.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/user")
public class UserController {

    @Autowired
    UserService userService;

    @Autowired // 通过Spring自动注入
    private JwtUtils jwtUtils;

//    获取用户列表
    @GetMapping(value = "/getList")
    public ResponseResult getUserList(@RequestParam(defaultValue = "1") int pageNum,
                                      @RequestParam(defaultValue = "10") int pageSize,
                                      @RequestParam(required = false) Long id,
                                      @RequestParam(required = false) String userName,
                                      @RequestParam(required = false) String name,
                                      @RequestParam(required = false) Integer role,
                                      @RequestParam(required = false) Boolean isLocked) {
        //  特殊情况修正
        pageNum = Math.max(pageNum, 1);          // 处理页码负数
        pageSize = Math.max(pageSize, 1);        // 处理每页大小负数
        pageSize = Math.min(pageSize, 100);      // 每页最多100条
        PageInfo<UserDTO> users = userService.getUserList( pageNum,pageSize, id, userName, name, role, isLocked);



        // 转换为VO分页数据
        PageInfo<UserVO> voPage = convertPage(users);

        return ResponseResult.success("成功",voPage);
    }

    // 分页对象转换方法
    private PageInfo<UserVO> convertPage(PageInfo<UserDTO> dtoPage) {
        PageInfo<UserVO> voPage = new PageInfo<>();
        // 复制分页信息
        voPage.setPageNum(dtoPage.getPageNum());
        voPage.setPageSize(dtoPage.getPageSize());
        voPage.setTotal(dtoPage.getTotal());
        voPage.setPages(dtoPage.getPages());

        // 转换列表数据
        List<UserVO> voList = dtoPage.getList().stream()
                .map(UserConverter::toVO)
                .collect(Collectors.toList());

        voPage.setList(voList);
        return voPage;
    }

//    添加用户
    @PostMapping(value = "/add")
    public ResponseResult addUser(@RequestBody User user) {
        UserDTO exitUser = userService.getUserByUserName(user.getUserName());
        if(exitUser != null){
            return ResponseResult.error(500, "用户名已经存在");
        }
          userService.addUser(user);
          return ResponseResult.success("成功",user);
    }

//    更新用户信息
    @PutMapping(value = "/update/{id}")
    public ResponseResult updateUser(@PathVariable Long id, @RequestBody User user) {
          userService.updateUser(id, user);
          return ResponseResult.success("成功",user);
    }

    @DeleteMapping("/delete/{ids}")
    @Transactional
    public ResponseResult deleteUsers(@PathVariable String ids) {
        // 1. 参数校验
        if (ids == null || ids.trim().isEmpty()) {
            return ResponseResult.error(400, "ID列表不能为空");
        }

        // 2. 拆分并验证ID
        List<Long> idList = Arrays.stream(ids.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .map(idStr -> {
                    try {
                        Long id = Long.parseLong(idStr);
                        if (id <= 0) throw new IllegalArgumentException("ID必须为正数: " + idStr);
                        return id;
                    } catch (NumberFormatException e) {
                        throw new IllegalArgumentException("无效的ID格式: " + idStr);
                    }
                })
                .collect(Collectors.toList());

        // 3. 根据ID数量选择最优删除方式
        if (idList.isEmpty()) {
            return ResponseResult.error(400, "未提供有效ID");
        } else if (idList.size() == 1) {
            // 单个删除（走更简单的逻辑）
            userService.deleteUserById(idList.get(0));
        } else {
            // 批量删除
            userService.deleteAllById(idList);
        }

        return ResponseResult.success("成功删除 " + idList.size() + " 个用户", null);
    }



    //    根据id查找用户
    @GetMapping(value = "/getUser")
    public ResponseResult getUserById(@RequestParam Long id) {
       UserDTO userDTO = userService.getUserById(id);
       UserVO userVO = UserConverter.toVO(userDTO);
        return ResponseResult.success("成功",userVO);
    }

    @PostMapping(value = "/login")
    public ResponseResult login(@RequestBody LoginQuery loginQuery) {

        UserDTO userDTO = userService.validateUser(loginQuery);

         if(userDTO != null){
             String token = jwtUtils.generateToken(userDTO); // 生成JWT
             return ResponseResult.success("登录成功", token);

         } else{
             return ResponseResult.error(400, "用户名或密码错误");
         }

    }

}
