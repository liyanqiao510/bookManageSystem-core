package com.lyq.bookManageSystem.controller;

import com.github.pagehelper.PageInfo;
import com.lyq.bookManageSystem.common.response.ResponseResult;
import com.lyq.bookManageSystem.model.DTO.BookDTO;
import com.lyq.bookManageSystem.model.DTO.UserDTO;
import com.lyq.bookManageSystem.model.VO.BookVO;
import com.lyq.bookManageSystem.model.request.ReadRequest;
import com.lyq.bookManageSystem.service.BookService;
import com.lyq.bookManageSystem.util.JwtUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/book")
public class BookController {

    @Autowired
    BookService bookService;

    @Autowired
    private JwtUtils jwtUtils;

//获取列表
    @GetMapping(value = "/getList")
    public ResponseEntity<ResponseResult<?>> getList(@RequestParam(defaultValue = "1") @Min(1) int pageNum,
                                      @RequestParam(defaultValue = "10") @Min(1) int pageSize,
                                      @RequestParam(required = false) @Positive Long id,
                                      @RequestParam(required = false) String bookName,
                                      @RequestParam(required = false) String bookAuthor,
                                      @RequestParam(required = false) Long bookTypeId,
                                      @RequestParam(required = false) Boolean isBorrowed
                                                     ) {

        PageInfo<BookDTO> book = bookService.getBookList( pageNum,pageSize, id,  bookName,
                bookAuthor,
                bookTypeId,
                isBorrowed );

        // 转换为VO类 分页数据
        PageInfo<BookVO> voPage = convertPage(book);

        ResponseResult response = ResponseResult.success(voPage);

        return ResponseEntity.ok(response) ;
    }

    // 分页对象转换方法
    private PageInfo<BookVO> convertPage(PageInfo<BookDTO> dtoPage) {
        PageInfo<BookVO> voPage = new PageInfo<>();
        // 复制分页信息
        voPage.setPageNum(dtoPage.getPageNum());
        voPage.setPageSize(dtoPage.getPageSize());
        voPage.setTotal(dtoPage.getTotal());
        voPage.setPages(dtoPage.getPages());

        // 转换列表数据

        List<BookVO> voList = new ArrayList<>();
        List<BookDTO> dtoList  = dtoPage.getList();

        for (BookDTO dto : dtoList) {
            BookVO vo = new BookVO();
            BeanUtils.copyProperties(dto, vo);
            vo.setTableBookImg("/api" + dto.getBookImg());
            voList.add(vo);
        }

        voPage.setList(voList);
        return voPage;
    }

//    @GetMapping(value = "/getByName")
//    public ResponseEntity<ResponseResult<?>> getByName(@RequestParam String bookName) {
//        BookDTO bookDTO = bookService.getBookByName(bookName);
//        ResponseResult response = ResponseResult.success("查找成功",bookDTO);
//        return ResponseEntity.ok(response);
//    }

//  新增
    @PostMapping(value = "/add")
    public ResponseEntity<ResponseResult<?>> add(@Valid @RequestBody BookDTO bookDTO) {
        bookService.addBook(bookDTO);
        ResponseResult response = ResponseResult.success("新增成功",bookDTO);
          return ResponseEntity.ok(response);
    }

//  更新
    @PutMapping(value = "/update/{id}")
    public ResponseEntity<ResponseResult<?>> update(@PathVariable @Min(1) Long id, @Valid @RequestBody BookDTO bookDTO) {
          bookService.updateBook(id, bookDTO);
        ResponseResult response = ResponseResult.success("修改信息成功",bookDTO);
          return ResponseEntity.ok(response);
    }

  //单个或批量删除
    @DeleteMapping("/delete/{ids}")
    public ResponseEntity<ResponseResult<?>> delete(@PathVariable
                                                        @Pattern(regexp = "^\\d+(,\\d+)*$", message = "ID格式应为逗号分隔的数字")
                                                                String ids) {

        int deleteCount = bookService.deleteBook(ids);

        ResponseResult response = ResponseResult.success("成功删除 " + deleteCount + " 个", null);

        return ResponseEntity.ok(response);
    }

    //  借阅
    @PostMapping(value = "/readBook")
    public ResponseEntity<ResponseResult<?>> readBook(@RequestHeader("Authorization") String authHeader,@Valid @RequestBody ReadRequest readRequest) {

        Long bookId = readRequest.getBookId();
        LocalDateTime returnTime = readRequest.getReturnTime();

        String token = authHeader.substring(7);
        UserDTO userDTO = jwtUtils.extractUserFromClaims(token);
        Long userId = userDTO.getId();

        LocalDateTime borrowTime = LocalDateTime.now();

        bookService.readBook(userId, bookId, borrowTime, returnTime);

        ResponseResult response = ResponseResult.success("借阅成功");
        return ResponseEntity.ok(response);
    }



}
