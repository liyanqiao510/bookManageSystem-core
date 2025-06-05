package com.lyq.bookManageSystem.model.entity;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class Borrow {
    private Long id;
    private Long userId;
    private Long bookId;

    private String userName;
    private String bookName;

    private LocalDateTime borrowTime;
    private LocalDateTime returnTime;

    private LocalDateTime createTime;
    private LocalDateTime updateTime;


}
