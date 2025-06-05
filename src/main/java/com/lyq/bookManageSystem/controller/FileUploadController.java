package com.lyq.bookManageSystem.controller;

import com.lyq.bookManageSystem.common.exception.BusinessException;
import com.lyq.bookManageSystem.common.response.ResponseResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import java.io.File;
import java.io.IOException;

@RestController
@RequestMapping("/upload")
public class FileUploadController {

    @Value("${upload.path}")
    private String uploadPath;

    @PostMapping("/image")
    public ResponseEntity<ResponseResult<?>> uploadImage(@RequestParam("file") MultipartFile file) {
        try {
            // 校验文件类型
            String originalName = file.getOriginalFilename();
            if(originalName == null || !originalName.matches(".*\\.(jpg|jpeg|png|gif)$")) {

                throw new BusinessException(40051,"只允许上传图片文件",400);
            }

            // 生成唯一文件名
            String fileName = UUID.randomUUID() + originalName.substring(originalName.lastIndexOf("."));

            // 创建保存目录
            File path = new File(uploadPath);
            if (!path.exists()) {
                path.mkdirs();
            }

            // 保存文件
            File dest = new File(uploadPath + fileName);
            file.transferTo(dest);

            ResponseResult response = ResponseResult.success(null, "/upload/images/" + fileName);
            // 返回访问路径
            return ResponseEntity.ok(response);
        } catch (IOException e) {
            throw new BusinessException(40050, e.getMessage(), 400);
        }
    }
}
