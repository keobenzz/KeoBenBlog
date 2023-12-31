package com.keoben.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.keoben.domain.ResponseResult;
import com.keoben.domain.dto.AddUserDto;
import com.keoben.domain.dto.UserListDto;
import com.keoben.domain.dto.UserUpdateDto;
import com.keoben.domain.entity.User;


/**
 * 用户表(User)表服务接口
 *
 * @author makejava
 * @since 2023-06-19 11:00:47
 */
public interface UserService extends IService<User> {

	ResponseResult userInfo();

	ResponseResult updateUserInfo(User user);

	ResponseResult register(User user);

	ResponseResult pageUserList(Integer pageNum, Integer pageSize, UserListDto userListDto);

	ResponseResult addUser(AddUserDto addUserDto);

	ResponseResult getUser(Long id);

	ResponseResult updateUser(UserUpdateDto userUpdateDto);

	ResponseResult deleteUser(Long id);

}

