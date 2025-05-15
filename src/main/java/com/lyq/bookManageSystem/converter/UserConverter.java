package com.lyq.bookManageSystem.converter;


import com.lyq.bookManageSystem.model.DTO.UserDTO;
import com.lyq.bookManageSystem.model.VO.UserVO;
import com.lyq.bookManageSystem.model.entity.User;

public class UserConverter {

    // DO → DTO（屏蔽敏感字段）
    public static UserDTO toDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setUserName(user.getUserName());
        dto.setRole(user.getRole());
        dto.setName(user.getName());

        dto.setBirthday(user.getBirthday());
        dto.setPhone(user.getPhone());
        dto.setEmail(user.getEmail());
        dto.setIsLocked(user.getIsLocked());
        return dto;
    }

    // DTO → VO（适配前端）
    public static UserVO toVO(UserDTO userDTO) {
        UserVO vo = new UserVO();
        vo.setId(userDTO.getId());
        vo.setUserName(userDTO.getUserName());
        vo.setRole(userDTO.getRole());
        vo.setName(userDTO.getName());

        vo.setBirthday(userDTO.getBirthday());
        vo.setPhone(userDTO.getPhone());
        vo.setEmail(userDTO.getEmail());
        vo.setIsLocked(userDTO.getIsLocked());
        return vo;
    }

//    // 私有方法：格式化日期
//    private static String formatDate(Date date) {
//        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
//    }
}
