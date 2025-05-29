package com.lyq.bookManageSystem.mapper;

import com.lyq.bookManageSystem.model.entity.BookType;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BookTypeMapper {

    //用户列表
    List<BookType> selectList(@Param("id") Long id,
                          @Param("typeName") String typeName);

    int getTotal(@Param("id") Long id,
                 @Param("typeName") String typeName);

    //新增用户
    int insert(BookType booktype);

    //删除用户 根据id
    int deleteById(Long id);

    int deleteAllById(@Param("ids") List<Long> ids);

    //更新用户 根据id
    int update(BookType booktype);

    //id查找用户
    BookType selectById(Long id);






}