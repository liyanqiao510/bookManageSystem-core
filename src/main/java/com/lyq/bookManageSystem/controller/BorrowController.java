package com.lyq.bookManageSystem.controller;

import com.github.pagehelper.PageInfo;
import com.lyq.bookManageSystem.common.response.ResponseResult;
import com.lyq.bookManageSystem.model.DTO.BorrowDTO;
import com.lyq.bookManageSystem.model.VO.BorrowVO;
import com.lyq.bookManageSystem.service.BorrowService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/borrow")
public class BorrowController {

    @Autowired
    BorrowService borrowService;


//获取列表
    @GetMapping(value = "/getList")
    public ResponseEntity<ResponseResult<?>> getList(@RequestParam(defaultValue = "1") @Min(1) int pageNum,
                                      @RequestParam(defaultValue = "10") @Min(1) int pageSize,
                                      @RequestParam(required = false) @Positive Long id,
                                      @RequestParam(required = false) String userName,
                                      @RequestParam(required = false) String bookName,
                                      @RequestParam(value = "borrowRange[]", required = false) String[] borrowRange ,
                                      @RequestParam(value = "returnRange[]", required = false) String[] returnRange
                                                     ) {

        PageInfo<BorrowDTO> borrow = borrowService.getBorrowList( pageNum,pageSize, id,  userName,
                bookName, borrowRange , returnRange  );

        // 转换为VO类 分页数据
        PageInfo<BorrowVO> voPage = convertPage(borrow);

        ResponseResult response = ResponseResult.success(voPage);

        return ResponseEntity.ok(response) ;
    }

    // 分页对象转换方法
    private PageInfo<BorrowVO> convertPage(PageInfo<BorrowDTO> dtoPage) {
        PageInfo<BorrowVO> voPage = new PageInfo<>();
        // 复制分页信息
        voPage.setPageNum(dtoPage.getPageNum());
        voPage.setPageSize(dtoPage.getPageSize());
        voPage.setTotal(dtoPage.getTotal());
        voPage.setPages(dtoPage.getPages());

        // 转换列表数据

        List<BorrowVO> voList = new ArrayList<>();
        List<BorrowDTO> dtoList  = dtoPage.getList();

        for (BorrowDTO dto : dtoList) {
            BorrowVO vo = new BorrowVO();
            BeanUtils.copyProperties(dto, vo);
            voList.add(vo);
        }

        voPage.setList(voList);
        return voPage;
    }

//  新增
    @PostMapping(value = "/add")
    public ResponseEntity<ResponseResult<?>> add(@Valid @RequestBody BorrowDTO borrowDTO) {
        borrowService.addBorrow(borrowDTO);
        ResponseResult response = ResponseResult.success("新增成功",borrowDTO);
          return ResponseEntity.ok(response);
    }

//  更新
    @PutMapping(value = "/update/{id}")
    public ResponseEntity<ResponseResult<?>> update(@PathVariable @Min(1) Long id, @Valid @RequestBody BorrowDTO borrowDTO) {
          borrowService.updateBorrow(id, borrowDTO);
        ResponseResult response = ResponseResult.success("修改信息成功",borrowDTO);
          return ResponseEntity.ok(response);
    }

  //单个或批量删除
    @DeleteMapping("/delete/{ids}")
    public ResponseEntity<ResponseResult<?>> delete(@PathVariable
                                                        @Pattern(regexp = "^\\d+(,\\d+)*$", message = "ID格式应为逗号分隔的数字")
                                                                String ids) {

        int deleteCount = borrowService.deleteBorrow(ids);

        ResponseResult response = ResponseResult.success("成功删除 " + deleteCount + " 个", null);

        return ResponseEntity.ok(response);
    }




}
