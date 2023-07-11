package com.keoben.service;

import com.keoben.domain.ResponseResult;
import com.keoben.domain.entity.User;

public interface LoginService {

	ResponseResult login(User user);

	ResponseResult logout();
}
