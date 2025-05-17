package com.lyq.bookManageSystem.common.interceptor;

import com.lyq.bookManageSystem.util.JwtUtils;
import io.jsonwebtoken.JwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

// 包路径：com.yourproject.interceptor
@Component
public class LoginInterceptor implements HandlerInterceptor {
    @Autowired
    JwtUtils jwtUtils;

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {
        // 0. 放行OPTIONS预检请求
        // 避免拦截预检请求
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            response.setStatus(HttpStatus.OK.value());
            return true;
        }

        // 1. 获取请求路径并处理白名单
        String uri = request.getRequestURI();
        if (uri.contains("/login") || uri.contains("/static/")) {
            return true; // 推荐使用精确路径匹配
        }

        // 2. 提取并验证JWT令牌
        String tokenHeader = request.getHeader("Authorization");
        System.out.println("tokenHeader:=="+tokenHeader);
        if (tokenHeader == null || !tokenHeader.startsWith("Bearer ")) {
            sendJsonError(response, 401, "Authorization头缺失或格式错误");
            return false;
        }

        String token = tokenHeader.substring(7).trim();
        try {
            if (!jwtUtils.validateToken(token)) { // 需包含签名+有效期验证
                sendJsonError(response, 401, "令牌无效或已过期");
                return false;
            }
        }
//        catch (ExpiredJwtException e) {
//            sendJsonError(response, 401, "令牌已过期");
//            return false;
//        }
        catch (JwtException | IllegalArgumentException e) {
            sendJsonError(response, 401, "令牌解析失败");
            return false;
        }

        return true;
    }


    // 辅助方法：返回JSON格式错误
    private void sendJsonError(HttpServletResponse response, int code, String msg) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        try {
            response.getWriter().write(String.format("{\"code\":%d, \"msg\":\"%s\"}", code, msg));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}



