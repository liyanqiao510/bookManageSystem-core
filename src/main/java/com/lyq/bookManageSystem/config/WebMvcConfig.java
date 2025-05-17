package com.lyq.bookManageSystem.config;

import com.lyq.bookManageSystem.common.interceptor.LoginInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Autowired
    private LoginInterceptor loginInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginInterceptor)
                .addPathPatterns("/**")  // 拦截所有路径
                .excludePathPatterns(    // 排除路径
                        "/api/login",
                        "/swagger**",        // 排除Swagger文档
                        "/webjars/**",       // 排除静态资源
                        "/error"             // 排除错误页面
                );
    }
}
