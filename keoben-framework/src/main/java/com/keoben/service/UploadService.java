package com.keoben.service;

import com.keoben.domain.ResponseResult;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

public interface UploadService {
	ResponseResult uploadImg(MultipartFile img);
}
