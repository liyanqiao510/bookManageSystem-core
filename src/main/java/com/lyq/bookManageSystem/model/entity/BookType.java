package com.lyq.bookManageSystem.model.entity;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class BookType {
    private Long id;
    private String typeName;
    private String typeDesc;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;


}
