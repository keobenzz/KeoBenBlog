package com.keoben.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.keoben.domain.ResponseResult;
import com.keoben.domain.entity.User;
import com.keoben.domain.enums.AppHttpCodeEnum;
import com.keoben.domain.vo.UserInfoVo;
import com.keoben.exception.SystemException;
import com.keoben.mapper.UserMapper;
import com.keoben.service.UserService;
import com.keoben.utils.BeanCopyUtils;
import com.keoben.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PutMapping;

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

	@Override
	public ResponseResult updateUserInfo(User user) {
		updateById(user);
		return ResponseResult.okResult();
	}

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public ResponseResult register(User user) {
		//对数据进行非空判断
		{
			if (!StringUtils.hasText(user.getUserName())) {
				throw new SystemException(AppHttpCodeEnum.USERNAME_NOT_NULL);
			}
			if (!StringUtils.hasText(user.getPassword())) {
				throw new SystemException(AppHttpCodeEnum.PASSWORD_NOT_NULL);
			}
			if (!StringUtils.hasText(user.getEmail())) {
				throw new SystemException(AppHttpCodeEnum.EMAIL_NOT_NULL);
			}
			if (!StringUtils.hasText(user.getNickName())) {
				throw new SystemException(AppHttpCodeEnum.NICKNAME_NOT_NULL);
			}
		}
		//对数据进行是否存在的判断
		{
			if (userNameExist(user.getUserName())) {
				throw new SystemException(AppHttpCodeEnum.USERNAME_EXIST);
			}
			if (userNickNameExist(user.getNickName())) {
				throw new SystemException(AppHttpCodeEnum.NICKNAME_EXIST);
			}
			if (userEmailExist(user.getEmail())) {
				throw new SystemException(AppHttpCodeEnum.EMAIL_EXIST);
			}
		}
		//对密码进行加密
		String encodePassword = passwordEncoder.encode(user.getPassword());
		user.setPassword(encodePassword);
		//存入数据库
		save(user);
		return ResponseResult.okResult();
	}

	private boolean userNameExist(String userName) {
		LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
		queryWrapper.eq(User::getUserName, userName);
		return count(queryWrapper) > 0;
	}
	private boolean userNickNameExist(String userNickName) {
		LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
		queryWrapper.eq(User::getNickName, userNickName);
		return count(queryWrapper) > 0;
	}
	private boolean userEmailExist(String userEmail) {
		LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
		queryWrapper.eq(User::getEmail, userEmail);
		return count(queryWrapper) > 0;
	}


}

