package com.keoben.com.keoben.controller;

import com.keoben.domain.ResponseResult;
import com.keoben.domain.dto.AddUserDto;
import com.keoben.domain.dto.UserListDto;
import com.keoben.domain.dto.UserUpdateDto;
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

	@GetMapping("/{id}")
	public ResponseResult getUser(@PathVariable Long id) {
		return userService.getUser(id);
	}

	@PutMapping()
	public ResponseResult updateUser(@RequestBody UserUpdateDto userUpdateDto) {
		return userService.updateUser(userUpdateDto);
	}

	@DeleteMapping("/{id}")
	public ResponseResult deleteUser(@PathVariable Long id) {
		return userService.deleteUser(id);
	}

}
