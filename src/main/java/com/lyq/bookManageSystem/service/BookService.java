package com.lyq.bookManageSystem.service;

import com.github.pagehelper.PageInfo;
import com.lyq.bookManageSystem.model.DTO.BookDTO;

import java.time.LocalDateTime;


public interface BookService {

    PageInfo<BookDTO> getBookList(int pageNum, int pageSize,
                                  Long id, String bookName,
                                  String bookAuthor,
                                  Long bookTypeId,
                                  Boolean isBorrowed
                                  );

    void addBook(BookDTO bookDTO);

    int deleteBook(String ids);

    void updateBook(Long id, BookDTO bookDTO);

    BookDTO getBookById(Long id);

    BookDTO getBookByName(String bookName);

    void readBook(Long userId, Long bookId, LocalDateTime borrowTime, LocalDateTime returnTime);


}
