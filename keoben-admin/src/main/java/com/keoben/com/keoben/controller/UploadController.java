package com.keoben.com.keoben.controller;

import com.keoben.domain.ResponseResult;
import com.keoben.service.UploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping
public class UploadController {

	@Autowired
	private UploadService uploadService;

	@PostMapping("/upload")
	public ResponseResult uploadImg(@RequestParam("img") MultipartFile multipartFile) {
		try{
			return uploadService.uploadImg(multipartFile);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("文件上传上传失败");
		}
	}

}
