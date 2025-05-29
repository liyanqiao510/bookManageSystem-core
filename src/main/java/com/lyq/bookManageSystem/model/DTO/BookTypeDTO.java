package com.lyq.bookManageSystem.model.DTO;


import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;

@Getter
@Setter
public class BookTypeDTO {
    private Long id;

    @Pattern(regexp = "^\\S+$", message = "若填写则不能为空且不能包含空格")
    private String typeName;
    private String typeDesc;

    private LocalDateTime createTime;
    private LocalDateTime updateTime;

}
