package com.keoben.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.keoben.domain.ResponseResult;
import com.keoben.domain.entity.User;
import com.keoben.domain.vo.UserInfoVo;
import com.keoben.mapper.UserMapper;
import com.keoben.service.UserService;
import com.keoben.utils.BeanCopyUtils;
import com.keoben.utils.SecurityUtils;
import org.springframework.stereotype.Service;

/**
 * 用户表(User)表服务实现类
 *
 * @author makejava
 * @since 2023-06-19 11:00:47
 */
@Service("userService")
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

	@Override
	public ResponseResult userInfo() {
		//获取当前用户id
		Long userId = SecurityUtils.getUserId();
		//根据用户id查询用户信息
		User user = getById(userId);
		//封装成UserInfoVo
		UserInfoVo vo = BeanCopyUtils.copyBean(user, UserInfoVo.class);
		return ResponseResult.okResult(vo);
	}
}

