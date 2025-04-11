package com.lyq.bookManageSystem.controller;

import com.lyq.bookManageSystem.common.ResponseResult;
import com.lyq.bookManageSystem.entity.User;
import com.lyq.bookManageSystem.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/user")
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping(value = "/getList")
    public ResponseResult getUserList(@RequestParam int pageNum,int pageSize) {
        List<User> users = userService.getUserList(pageNum,pageSize);

        return ResponseResult.success("成功",users);
    }

    @PostMapping(value = "/add")
    public ResponseResult addUser(@RequestBody User user) {
        User exitUser = userService.getUserByUserName(user.getUserName());
        if(exitUser != null){
            return ResponseResult.fail("用户名已经存在");
        }
          userService.addUser(user);
          return ResponseResult.success("成功",user);
    }

    @PostMapping(value = "/update")
    public ResponseResult updateUser(@RequestBody User user) {
          userService.updateUser(user);
          return ResponseResult.success("成功",user);
    }

    @DeleteMapping (value = "/delete")
    public ResponseResult deleteUserById(@RequestParam Integer id) {
        userService.deleteUserById(id);
        return ResponseResult.success("成功",null);
    }

    @GetMapping(value = "/getUser")
    public ResponseResult getUserById(@RequestParam Integer id) {
       User user = userService.getUserById(id);
        return ResponseResult.success("成功",user);
    }

}
