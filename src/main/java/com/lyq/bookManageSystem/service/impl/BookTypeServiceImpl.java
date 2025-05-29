package com.lyq.bookManageSystem.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lyq.bookManageSystem.common.enums.BusinessErrorCode;
import com.lyq.bookManageSystem.common.exception.BusinessException;

import com.lyq.bookManageSystem.mapper.BookTypeMapper;
import com.lyq.bookManageSystem.model.DTO.BookTypeDTO;
import com.lyq.bookManageSystem.model.entity.BookType;
import com.lyq.bookManageSystem.service.BookTypeService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class BookTypeServiceImpl implements BookTypeService {

    @Resource
    private BookTypeMapper bookTypeMapper;

    @Override
    public PageInfo<BookTypeDTO> getBookTypeList(int pageNum, int pageSize, Long id, String typeName ) {
        //  特殊情况修正
        pageNum = Math.max(pageNum, 1);          // 处理页码负数
        pageSize = Math.max(pageSize, 1);        // 处理每页大小负数
        pageSize = Math.min(pageSize, 100);      // 每页最多100条

        Integer totalCount = bookTypeMapper.getTotal(id, typeName );

        Integer totalPages = (int) Math.ceil((double) totalCount / pageSize);

        // 修正 页码超出时, 使用最大页码
        if (totalPages > 0 && pageNum > totalPages) {
            pageNum = totalPages;
            System.out.println("执行页码修正");
        }

        // 1. 启动分页
        PageHelper.startPage(pageNum, pageSize);

        List<BookType> bookTypeList = bookTypeMapper.selectList(id, typeName  );

        List<BookTypeDTO> dtoList = new ArrayList<>();
        for (BookType bookType : bookTypeList) {
            BookTypeDTO dto = new BookTypeDTO();
            BeanUtils.copyProperties(bookType, dto);
            dtoList.add(dto);
        }

        PageInfo<BookType> pageInfo = new PageInfo<>(bookTypeList);

        PageInfo<BookTypeDTO> dtoPageInfo = new PageInfo<>();
        BeanUtils.copyProperties(pageInfo, dtoPageInfo); // 拷贝分页元数据
        dtoPageInfo.setList(dtoList);                // 替换为 DTO 列表

        return dtoPageInfo;
    }

    @Override
    @Transactional
    public void addBookType(BookTypeDTO bookTypeDTO) {
        BookType bookType = new BookType();
        BeanUtils.copyProperties(bookTypeDTO, bookType);

        int rows = bookTypeMapper.insert(bookType);
        // 执行插入
        if (rows != 1) {
            throw new BusinessException(BusinessErrorCode.USER_CREATE_FAILED);
        }

    }

    @Override
    @Transactional
    public void updateBookType(Long id, BookTypeDTO bookTypeDTO) {

        bookTypeDTO.setId(id);
        BookType bookType = new BookType();
        BeanUtils.copyProperties(bookTypeDTO, bookType);

       int rows =  bookTypeMapper.update(bookType);
        if (rows != 1) {
            throw new BusinessException(BusinessErrorCode.USER_UPDATE_FAILED);
        }

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteBookType(String ids) {

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
            rows = bookTypeMapper.deleteById(idList.get(0));
            if (rows != 1) {
                throw new BusinessException(BusinessErrorCode.USER_DELETE_FAILED);
            }

        } else {
            // 批量删除
            rows = bookTypeMapper.deleteAllById(idList);
            if (rows != idList.size()) {
                throw new BusinessException(BusinessErrorCode.USER_DELETE_FAILED);
            }
        }

        return rows;
    }

    @Override
    public BookTypeDTO getBookTypeById(Long id) {
        BookType bookType = bookTypeMapper.selectById(id);
        BookTypeDTO dto = new BookTypeDTO();
        BeanUtils.copyProperties(bookType, dto);

       return dto  ;

    }



}
