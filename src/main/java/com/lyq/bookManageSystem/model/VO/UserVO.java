package com.lyq.bookManageSystem.model.VO;


import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class UserVO {
    private Long id;
    private String userName;
    private Integer role;
    private String name;

    private LocalDate birthday;
    private String phone;
    private String email;
    private Boolean isLocked;

}
