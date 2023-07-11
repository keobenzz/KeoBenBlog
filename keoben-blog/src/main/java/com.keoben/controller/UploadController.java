package com.keoben.controller;

import com.keoben.domain.ResponseResult;
import com.keoben.domain.entity.User;
import com.keoben.service.UploadService;
import com.keoben.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class UploadController {

	@Autowired
	private UploadService uploadService;

	@Autowired
	private UserService userService;

	@PostMapping("/upload")
	public ResponseResult uploadImg(MultipartFile img) {
		return uploadService.uploadImg(img);
	}


}
