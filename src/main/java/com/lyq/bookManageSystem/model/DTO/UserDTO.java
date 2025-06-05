package com.lyq.bookManageSystem.model.DTO;


import com.lyq.bookManageSystem.model.valid.ValidPhone;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Getter
@Setter
public class UserDTO {
    private Long id;

    @NotBlank(message = "用户名不能为空")
    private String userName;

    @NotNull(message = "角色不能为空")
    private Integer role;
    private String name;

    @Past(message = "出生日期必须早于当前日期")
    private LocalDate birthday;

    @ValidPhone   //允许null和空字符串 , 其他情况视为有填写, 有填写时:不允许空格, 不允许错误的手机号格式
    private String phone;

    @Email(message = "邮箱格式不正确")
    @Size(max = 50, message = "邮箱长度不能超过50个字符")
    private String email;

    private Boolean isLocked;

}
