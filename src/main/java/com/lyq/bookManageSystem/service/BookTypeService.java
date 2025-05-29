package com.lyq.bookManageSystem.service;

import com.github.pagehelper.PageInfo;
import com.lyq.bookManageSystem.model.DTO.BookTypeDTO;
import com.lyq.bookManageSystem.model.entity.BookType;


public interface BookTypeService {

    PageInfo<BookTypeDTO> getBookTypeList(int pageNum, int pageSize, Long id, String typeName);

    void addBookType(BookTypeDTO bookTypeDTO);

    int deleteBookType(String ids);

    void updateBookType(Long id, BookTypeDTO bookTypeDTO);

    BookTypeDTO getBookTypeById(Long id);




}
