package com.lyq.bookManageSystem.mapper;

import com.lyq.bookManageSystem.model.entity.Borrow;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface BorrowMapper {

    //列表
    List<Borrow> selectList(
                          @Param("id") Long id,
                          @Param("userName") String userName,
                          @Param("bookName") String bookName,
                          @Param("borrowRange") LocalDateTime[] borrowRange, // 或 List<LocalDateTime>
                          @Param("returnRange") LocalDateTime[] returnRange
                         );

    int getTotal(
                  @Param("id") Long id,
                  @Param("userName") String userName,
                  @Param("bookName") String bookName,
                  @Param("borrowRange") LocalDateTime[] borrowRange,
                  @Param("returnRange") LocalDateTime[] returnRange);

    //新增
    int insert(Borrow borrow);

    //删除 根据id
    int deleteById(@Param("id") Long id);

    int deleteAllById(@Param("ids") List<Long> ids);

    //更新 根据id
    int update(Borrow borrow);

    //id查找
    Borrow selectById(@Param("id") Long id);






}