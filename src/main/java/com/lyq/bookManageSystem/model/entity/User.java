package com.lyq.bookManageSystem.model.entity;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
public class User {
    private Long id;
    private String userName;
    private String password;
    private Integer role;
    private String name;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    private LocalDate birthday;
    private String phone;
    private String email;
    private Boolean isLocked;



}
