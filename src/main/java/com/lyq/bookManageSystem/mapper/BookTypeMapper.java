package com.lyq.bookManageSystem.mapper;

import com.lyq.bookManageSystem.model.entity.BookType;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BookTypeMapper {

    //列表
    List<BookType> selectList(@Param("id") Long id,
                          @Param("typeName") String typeName);

    int getTotal(@Param("id") Long id,
                 @Param("typeName") String typeName);

    //新增
    int insert(BookType bookType);

    //删除 根据id
    int deleteById(@Param("id") Long id);

    int deleteAllById(@Param("ids") List<Long> ids);

    //更新 根据id
    int update(BookType bookType);

    //id查找
    BookType selectById(@Param("id") Long id);

    List<BookType> selectAll();




}