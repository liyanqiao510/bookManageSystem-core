package com.lyq.bookManageSystem;


import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
@MapperScan(value = "com.lyq.bookManageSystem.mapper")
public class  bookManageSystemApplication  {

    public static void main(String[] args) {
        SpringApplication.run(bookManageSystemApplication.class, args);
    }

}