package com.keoben.com.keoben.controller;

import com.keoben.domain.ResponseResult;
import com.keoben.domain.dto.AddUserDto;
import com.keoben.domain.dto.UserListDto;
import com.keoben.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/system/user")
public class UserController {

	@Autowired
	private UserService userService;

	@GetMapping("/list")
	public ResponseResult pageUserList(Integer pageNum, Integer pageSize, UserListDto userListDto) {
		return userService.pageUserList(pageNum, pageSize, userListDto);
	}

	@PostMapping()
	public ResponseResult addUser(@RequestBody AddUserDto addUserDto) {
		return userService.addUser(addUserDto);
	}

}
