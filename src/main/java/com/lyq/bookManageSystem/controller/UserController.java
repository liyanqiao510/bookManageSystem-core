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
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/user")
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    //    获取用户列表
    @GetMapping(value = "/getList")
    public ResponseResult getUserList(@RequestParam(defaultValue = "1") int pageNum,
                                      @RequestParam(defaultValue = "10") int pageSize,
                                      @RequestParam(required = false) Long id,
                                      @RequestParam(required = false) String userName,
                                      @RequestParam(required = false) String name,
                                      @RequestParam(required = false) Integer role,
                                      @RequestParam(required = false) Boolean isLocked) {

        PageInfo<UserDTO> users = userService.getUserList( pageNum,pageSize, id, userName, name, role, isLocked);

        // 转换为VO类 分页数据
        PageInfo<UserVO> voPage = convertPage(users);

        return ResponseResult.success("获取用户列表成功",voPage);
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

//  新增用户
    @PostMapping(value = "/add")
    public ResponseResult addUser(@RequestBody User user) {
        userService.addUser(user);

          return ResponseResult.success("新增用户成功",user);
    }

//  更新用户信息
    @PutMapping(value = "/update/{id}")
    public ResponseResult updateUser(@PathVariable Long id, @RequestBody User user) {
          userService.updateUser(id, user);
          return ResponseResult.success("修改用户信息成功",user);
    }

  //单个或批量删除
    @DeleteMapping("/delete/{ids}")
    @Transactional
    public ResponseResult deleteUsers(@PathVariable String ids) {


        int deleteCount = userService.deleteUser(ids);

        return ResponseResult.success("成功删除 " + deleteCount + " 个用户", null);
    }

    //  根据id查找用户
//    @GetMapping(value = "/getUser")
//    public ResponseResult getUserById(@RequestParam Long id) {
//       UserDTO userDTO = userService.getUserById(id);
//       UserVO userVO = UserConverter.toVO(userDTO);
//        return ResponseResult.success("成功",userVO);
//    }


    //登录
    @PostMapping("/login")
    public ResponseEntity<ResponseResult<?>> login(@RequestBody LoginQuery loginQuery) {
        UserDTO userDTO = userService.validateUser(loginQuery);
        if (userDTO != null) {
            //用于验证的token
            String token = jwtUtils.generateToken(userDTO);
            HashMap<String, Object> data = new HashMap<>();
            data.put("token", token);  // 假设 token 是 String 类型
            data.put("userName", userDTO.getUserName() );
            data.put("role", userDTO.getRole() );

            //用于刷新登录的token
            String refreshToken = jwtUtils.generateRefreshToken(userDTO);
            // 存储RefreshToken到Redis（关联用户ID）
            redisTemplate.opsForValue().set(
                    "refresh:" + userDTO.getUserName(),
                    refreshToken,
                    jwtUtils.getRefreshExpiration(),
                    TimeUnit.SECONDS
            );


            // 设置HttpOnly Cookie
            ResponseCookie cookie = ResponseCookie.from("refreshToken", refreshToken)
                    .httpOnly(true)
                    .secure(true)  // 仅HTTPS
                    .sameSite("Strict")  // 防御CSRF
                    .maxAge(jwtUtils.getRefreshExpiration())
                    .path("/")
                    .build();

                ResponseResult<?> result = ResponseResult.success("登录成功", data);
                return ResponseEntity.ok()
                        .header(HttpHeaders.SET_COOKIE, cookie.toString())
                        .body(result);
        } else {
            ResponseResult<?> error = ResponseResult.error( "用户名或密码错误",1001);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error); //  认证失败
        }
    }

    //退出登录
    @PostMapping("/logout")
    public ResponseEntity<ResponseResult<?>> logout(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7); // 去除Bearer前缀

        //加入JWT到Redis黑名单
        jwtUtils.invalidateToken(token);
        ResponseResult<?> result = ResponseResult.success("退出登录成功");
        return ResponseEntity.ok(result);

    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(
            @CookieValue("refreshToken") String refreshToken) {

        // 1. 验证RefreshToken有效性
        if (!jwtUtils.validateRefreshToken(refreshToken)) {
            return ResponseEntity.status(401).body("RefreshToken无效");
        }

        // 2. 检查Redis中的Token匹配
        Claims claims = jwtUtils.parseToken(refreshToken);
        System.out.println("claims的值是:"+claims);
        String redisKey = "refresh:" + claims.get("sub");
        if (!redisTemplate.hasKey(redisKey)) {
            return ResponseEntity.status(403).body("RefreshToken已失效或未存储");
        }

        String redisToken = (String) redisTemplate.opsForValue().get("refresh:" + claims.get("sub"));
        System.out.println("redisToken的值是:"+redisToken);
        System.out.println("请求refreshToken的值是:"+refreshToken);
        if (!refreshToken.equals(redisToken)) {
            return ResponseEntity.status(403).body("RefreshToken不匹配");
        }

        // 3. 生成新Token并更新Cookie
        UserDTO userDTO = userService.getUserByUserName((String) claims.get("sub"));
//        UserDTO userDTO = jwtUtils.extractUserFromClaims(claims);
        String newAccessToken = jwtUtils.generateToken(userDTO);

//        String newRefreshToken = jwtUtils.generateRefreshToken(userDTO);
//
//        // 更新Redis和Cookie
//        redisTemplate.opsForValue().set(
//                "refresh:" + userDTO.getId(),
//                newRefreshToken,
//                jwtUtils.getRefreshExpiration(),
//                TimeUnit.SECONDS
//        );
//
//
//        ResponseCookie cookie = ResponseCookie.from("refreshToken", newRefreshToken)
//                .httpOnly(true)
//                .secure(true)
//                .build();

        HashMap<String, Object> data = new HashMap<>();
        data.put("token", newAccessToken);

        ResponseResult<?> result = ResponseResult.success("登录成功", data);
        return ResponseEntity.ok()
                .body(result);

    }




}
