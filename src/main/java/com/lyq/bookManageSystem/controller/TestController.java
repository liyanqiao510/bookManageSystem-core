package com.lyq.bookManageSystem.controller;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @Value("${spring.datasource.url}")
    private String value;

    @GetMapping("/config-check")
    public String checkConfig() {
        return "Value: " + value;
    }
}
