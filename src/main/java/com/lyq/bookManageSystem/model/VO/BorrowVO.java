package com.lyq.bookManageSystem.model.VO;


import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class BorrowVO {
    private Long id;
    private Long userId;
    private String userName;

    private Long bookId;
    private String bookName;

    private LocalDateTime borrowTime;
    private LocalDateTime returnTime;




}