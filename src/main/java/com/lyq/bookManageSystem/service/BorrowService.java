package com.lyq.bookManageSystem.service;

import com.github.pagehelper.PageInfo;
import com.lyq.bookManageSystem.model.DTO.BorrowDTO;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;


public interface BorrowService {

    PageInfo<BorrowDTO> getBorrowList(int pageNum, int pageSize,
                                     Long id,
                                     String userName,
                                     String bookName,
                                     String[] borrowRange,
                                     String[] returnRange

    );

    void addBorrow(BorrowDTO borrowDTO);

    int deleteBorrow(String ids);

    void updateBorrow(Long id, BorrowDTO borrowDTO);

    BorrowDTO getBorrowById(Long id);




}
