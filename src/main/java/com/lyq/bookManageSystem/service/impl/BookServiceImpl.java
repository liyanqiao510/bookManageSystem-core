package com.lyq.bookManageSystem.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lyq.bookManageSystem.common.enums.BusinessErrorCode;
import com.lyq.bookManageSystem.common.exception.BusinessException;
import com.lyq.bookManageSystem.mapper.BookMapper;
import com.lyq.bookManageSystem.mapper.BorrowMapper;
import com.lyq.bookManageSystem.model.DTO.BookDTO;
import com.lyq.bookManageSystem.model.entity.Book;
import com.lyq.bookManageSystem.model.entity.Borrow;
import com.lyq.bookManageSystem.service.BookService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class BookServiceImpl implements BookService {

    @Resource
    private BookMapper bookMapper;

    @Resource
    private BorrowMapper borrowMapper;

    @Override
    public PageInfo<BookDTO> getBookList(int pageNum, int pageSize,
                                         Long id, String bookName,
                                         String bookAuthor,
                                         Long bookTypeId,
                                         Boolean isBorrowed ) {
        //  特殊情况修正
        pageNum = Math.max(pageNum, 1);          // 处理页码负数
        pageSize = Math.max(pageSize, 1);        // 处理每页大小负数
        pageSize = Math.min(pageSize, 100);      // 每页最多100条


        Integer totalCount = bookMapper.getTotal( id,  bookName,
                 bookAuthor,
                 bookTypeId,
                 isBorrowed  );

        Integer totalPages = (int) Math.ceil((double) totalCount / pageSize);

        // 修正 页码超出时, 使用最大页码
        if (totalPages > 0 && pageNum > totalPages) {
            pageNum = totalPages;
            System.out.println("执行页码修正");
        }

        // 1. 启动分页
        PageHelper.startPage(pageNum, pageSize);

        List<Book> bookList = bookMapper.selectList(id,  bookName,
                bookAuthor,
                bookTypeId,
                isBorrowed  );

        List<BookDTO> dtoList = new ArrayList<>();
        for (Book book : bookList) {
            BookDTO dto = new BookDTO();
            BeanUtils.copyProperties(book, dto);
            dtoList.add(dto);
        }

        PageInfo<Book> pageInfo = new PageInfo<>(bookList);

        PageInfo<BookDTO> dtoPageInfo = new PageInfo<>();
        BeanUtils.copyProperties(pageInfo, dtoPageInfo); // 拷贝分页元数据
        dtoPageInfo.setList(dtoList);                // 替换为 DTO 列表

        return dtoPageInfo;
    }

    @Override
    @Transactional
    public void addBook(BookDTO bookDTO) {
        Book book = new Book();
        BeanUtils.copyProperties(bookDTO, book);

        int rows = bookMapper.insert(book);
        // 执行插入
        if (rows != 1) {
            throw new BusinessException(BusinessErrorCode.USER_CREATE_FAILED);
        }

    }

    @Override
    @Transactional
    public void updateBook(Long id, BookDTO bookDTO) {

        bookDTO.setId(id);
        Book book = new Book();
        BeanUtils.copyProperties(bookDTO, book);

       int rows =  bookMapper.update(book);
        if (rows != 1) {
            throw new BusinessException(BusinessErrorCode.USER_UPDATE_FAILED);
        }

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteBook(String ids) {

        int rows = 0;
        // 1. 参数校验
        if (ids == null || ids.trim().isEmpty()) {
            throw new BusinessException(BusinessErrorCode.ID_LIST_EMPTY);
        }

        // 2. 拆分并验证ID
        List<Long> idList = Arrays.stream(ids.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .map(idStr -> {
                    try {
                        Long id = Long.parseLong(idStr);
                        if (id <= 0) {
                            throw new BusinessException(BusinessErrorCode.ID_MUST_BE_POSITIVE);
                        }
                        return id;
                    } catch (NumberFormatException e) {
                        throw new BusinessException(BusinessErrorCode.INVALID_ID_FORMAT);
                    }
                })
                .collect(Collectors.toList());

        // 3. 根据ID数量选择最优删除方式
        if (idList.isEmpty()) {
            throw new BusinessException(BusinessErrorCode.INVALID_ID);
        } else if (idList.size() > 100) {
            throw new BusinessException(BusinessErrorCode.INVALID_ID);

        }  else if (idList.size() == 1) {
            // 单个删除（走更简单的逻辑）
            rows = bookMapper.deleteById(idList.get(0));
            if (rows != 1) {
                throw new BusinessException(BusinessErrorCode.USER_DELETE_FAILED);
            }

        } else {
            // 批量删除
            rows = bookMapper.deleteAllById(idList);
            if (rows != idList.size()) {
                throw new BusinessException(BusinessErrorCode.USER_DELETE_FAILED);
            }
        }

        return rows;
    }

    @Override
    public BookDTO getBookById(Long id) {
        Book book = bookMapper.selectById(id);
        BookDTO dto = new BookDTO();
        BeanUtils.copyProperties(book, dto);

       return dto  ;

    }

    @Override
    public  BookDTO getBookByName(String bookName) {
        Book book = bookMapper.selectByName(bookName);
        BookDTO dto = new BookDTO();
        BeanUtils.copyProperties(book, dto);

       return dto  ;

    }

    @Override
    public  void readBook(Long userId, Long bookId, LocalDateTime borrowTime, LocalDateTime returnTime) {
        Book book = bookMapper.selectById(bookId);
        if(book.getIsBorrowed() == true){
            throw new BusinessException(40060,"图书已被借阅",400);
        }

        Borrow borrow = new Borrow();
        borrow.setBookId(bookId);
        borrow.setUserId(userId);
        borrow.setBorrowTime(borrowTime);
        borrow.setReturnTime(returnTime);
        borrowMapper.insert(borrow);

        book.setIsBorrowed(true);
        bookMapper.update(book);

    }



}
