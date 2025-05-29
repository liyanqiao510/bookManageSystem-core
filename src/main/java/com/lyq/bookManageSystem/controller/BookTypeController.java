package com.lyq.bookManageSystem.controller;

import com.github.pagehelper.PageInfo;
import com.lyq.bookManageSystem.common.response.ResponseResult;
import com.lyq.bookManageSystem.model.DTO.BookTypeDTO;
import com.lyq.bookManageSystem.model.VO.BookTypeVO;
import com.lyq.bookManageSystem.service.BookTypeService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/bookType")
public class BookTypeController {

    @Autowired
    BookTypeService bookTypeService;


//获取列表
    @GetMapping(value = "/getList")
    public ResponseEntity<ResponseResult<?>> getList(@RequestParam(defaultValue = "1") @Min(1) int pageNum,
                                      @RequestParam(defaultValue = "10") @Min(1) int pageSize,
                                      @RequestParam(required = false) @Positive Long id,
                                      @RequestParam(required = false) String typeName ) {

        PageInfo<BookTypeDTO> booktype = bookTypeService.getBookTypeList( pageNum,pageSize, id, typeName );

        // 转换为VO类 分页数据
        PageInfo<BookTypeVO> voPage = convertPage(booktype);

        ResponseResult response = ResponseResult.success("获取列表成功",voPage);

        return ResponseEntity.ok(response) ;
    }

    // 分页对象转换方法
    private PageInfo<BookTypeVO> convertPage(PageInfo<BookTypeDTO> dtoPage) {
        PageInfo<BookTypeVO> voPage = new PageInfo<>();
        // 复制分页信息
        voPage.setPageNum(dtoPage.getPageNum());
        voPage.setPageSize(dtoPage.getPageSize());
        voPage.setTotal(dtoPage.getTotal());
        voPage.setPages(dtoPage.getPages());

        // 转换列表数据

        List<BookTypeVO> voList = new ArrayList<>();
        List<BookTypeDTO> dtoList  = dtoPage.getList();

        for (BookTypeDTO dto : dtoList) {
            BookTypeVO vo = new BookTypeVO();
            BeanUtils.copyProperties(dto, vo);
            voList.add(vo);
        }

        voPage.setList(voList);
        return voPage;
    }

//  新增
    @PostMapping(value = "/add")
    public ResponseEntity<ResponseResult<?>> add(@Valid @RequestBody BookTypeDTO bookTypeDTO) {
        bookTypeService.addBookType(bookTypeDTO);
        ResponseResult response = ResponseResult.success("新增成功",bookTypeDTO);
          return ResponseEntity.ok(response);
    }

//  更新
    @PutMapping(value = "/update/{id}")
    public ResponseEntity<ResponseResult<?>> update(@PathVariable @Min(1) Long id, @Valid @RequestBody BookTypeDTO bookTypeDTO) {
          bookTypeService.updateBookType(id, bookTypeDTO);
        ResponseResult response = ResponseResult.success("修改信息成功",bookTypeDTO);
          return ResponseEntity.ok(response);
    }

  //单个或批量删除
    @DeleteMapping("/delete/{ids}")
    public ResponseEntity<ResponseResult<?>> delete(@PathVariable
                                                        @Pattern(regexp = "^\\d+(,\\d+)*$", message = "ID格式应为逗号分隔的数字")
                                                                String ids) {

        int deleteCount = bookTypeService.deleteBookType(ids);

        ResponseResult response = ResponseResult.success("成功删除 " + deleteCount + " 个", null);

        return ResponseEntity.ok(response);
    }




}
