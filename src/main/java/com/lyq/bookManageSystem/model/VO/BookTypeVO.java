package com.lyq.bookManageSystem.model.VO;


import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
public class BookTypeVO {
    private Long id;
    private String typeName;
    private String typeDesc;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

}
