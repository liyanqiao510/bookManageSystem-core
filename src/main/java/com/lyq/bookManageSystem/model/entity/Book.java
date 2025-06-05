package com.lyq.bookManageSystem.model.entity;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class Book {
    private Long id;
    private String bookName;
    private String bookAuthor;
    private BigDecimal bookPrice;
    private Long bookTypeId;
    private String bookDesc;
    private Boolean isBorrowed;
    private String bookImg;

    private LocalDateTime createTime;
    private LocalDateTime updateTime;


}
