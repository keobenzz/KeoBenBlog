package com.keoben.handler.exception;

import com.keoben.domain.ResponseResult;
import com.keoben.domain.enums.AppHttpCodeEnum;
import com.keoben.exception.SystemException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

//@ControllerAdvice
//@ResponseBody
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

	@ExceptionHandler(SystemException.class)
	public ResponseResult systemExceptionHandler(SystemException e) {
		//打印异常信息
		log.error("出现了异常! {}", e);
		//从异常对象中获取提示信息封装返回
		return ResponseResult.errorResult(e.getCode(), e.getMsg());
	}

	@ExceptionHandler(Exception.class)
	public ResponseResult ExceptionHandler(Exception e) {
		//打印异常信息
		log.error("出现了异常! {}", e);
		//从异常对象中获取提示信息封装返回
		return ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR.getCode(), e.getMessage());
	}

}
