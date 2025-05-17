package com.lyq.bookManageSystem;


import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
@EnableCaching
@MapperScan(value = "com.lyq.bookManageSystem.mapper")
public class  bookManageSystemApplication  {

    public static void main(String[] args) {
        SpringApplication.run(bookManageSystemApplication.class, args);
    }

}

