package com.lyq.bookManageSystem.model.VO;


import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class BookVO {
    private Long id;
    private String bookName;
    private String bookAuthor;
    private BigDecimal bookPrice;
    private Long bookTypeId;
    private String bookDesc;
    private Boolean isBorrowed;
    private String bookImg;
    private String tableBookImg;

}
