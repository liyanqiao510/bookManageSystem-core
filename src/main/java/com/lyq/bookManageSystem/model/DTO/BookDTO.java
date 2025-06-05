package com.lyq.bookManageSystem.model.DTO;


import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class BookDTO {
    private Long id;
    @Pattern(regexp = "^\\S+$", message = "若填写则不能为空且不能包含空格")
    private String bookName;
    private String bookAuthor;
    @DecimalMin(value = "0.0", inclusive = false, message = "价格必须大于0")
    private BigDecimal bookPrice;
    private Long bookTypeId;
    @Size(max = 500, message = "描述长度不能超过500字符")
    private String bookDesc;
    private Boolean isBorrowed;
    private String bookImg;


}
