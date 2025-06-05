package com.lyq.bookManageSystem.model.DTO;



import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Getter
@Setter
public class BorrowDTO {
    private Long id;
    private Long userId;

    private Long bookId;

    @NotBlank(message = "用户名不能为空")
    private String userName;
    @NotBlank(message = "书名不能为空")
    private String bookName;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime borrowTime;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime returnTime;



}
