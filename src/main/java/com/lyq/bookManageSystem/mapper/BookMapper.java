package com.lyq.bookManageSystem.mapper;

import com.lyq.bookManageSystem.model.entity.Book;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BookMapper {

    //列表
    List<Book> selectList(
                          @Param("id") Long id,
                          @Param("bookName") String bookName,
                          @Param("bookAuthor") String bookAuthor,
                          @Param("bookTypeId") Long bookTypeId,
                          @Param("isBorrowed") Boolean isBorrowed  );

    int getTotal(@Param("id") Long id,
                 @Param("bookName") String bookName,
                 @Param("bookAuthor") String bookAuthor,
                 @Param("bookTypeId") Long bookTypeId,
                 @Param("isBorrowed") Boolean isBorrowed);

    //新增
    int insert(Book book);

    //删除 根据id
    int deleteById(@Param("id") Long id);

    int deleteAllById(@Param("ids") List<Long> ids);

    //更新 根据id
    int update(Book book);

    //id查找
    Book selectById(@Param("id") Long id);

    Book selectByName(@Param("bookName") String bookName);






}