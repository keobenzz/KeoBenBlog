package com.keoben.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.keoben.domain.ResponseResult;
import com.keoben.domain.dto.AddUserDto;
import com.keoben.domain.dto.UserListDto;
import com.keoben.domain.dto.UserUpdateDto;
import com.keoben.domain.entity.LoginUser;
import com.keoben.domain.entity.Role;
import com.keoben.domain.entity.User;
import com.keoben.domain.enums.AppHttpCodeEnum;
import com.keoben.domain.vo.EchoUserVo;
import com.keoben.domain.vo.PageVo;
import com.keoben.domain.vo.UserInfoVo;
import com.keoben.domain.vo.UserVo;
import com.keoben.exception.SystemException;
import com.keoben.mapper.UserMapper;
import com.keoben.service.UserService;
import com.keoben.utils.BeanCopyUtils;
import com.keoben.utils.SecurityUtils;
import com.keoben.utils.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PutMapping;

import java.util.List;

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

	@Override
	public ResponseResult pageUserList(Integer pageNum, Integer pageSize, UserListDto userListDto) {
		LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
		wrapper.like(StringUtils.hasText(userListDto.getUserName()),
						User::getUserName, userListDto.getUserName())
				.like(StringUtils.hasText(userListDto.getPhonenumber()),
						User::getPhonenumber, userListDto.getPhonenumber())
				.like(StringUtils.hasText(userListDto.getStatus()),
						User::getStatus, userListDto.getStatus());
		Page<User> page = new Page<>();
		page.setCurrent(pageNum);
		page.setSize(pageSize);
		page(page, wrapper);
		PageVo userPageVo = new PageVo(page.getRecords(), page.getTotal());
		return ResponseResult.okResult(userPageVo);
	}

	@Override
	public ResponseResult addUser(AddUserDto addUserDto) {
		//判断用户名是否为空
		if(!StringUtils.hasText(addUserDto.getUserName())) {
			return ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR, "必须填写用户名");
		}
		List<User> list = null;
		//查询用户名是否存在
		list = list(new LambdaQueryWrapper<User>()
				.eq(StringUtils.hasText(addUserDto.getUserName()), User::getUserName, addUserDto.getUserName()));
		if(list.size() > 0) {
			return ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR, "用户名已存在");
		}
		//查询手机号是否存在
		if(StringUtils.hasText(addUserDto.getPhonenumber())) {
			list = list(new LambdaQueryWrapper<User>()
					.eq(User::getPhonenumber, addUserDto.getPhonenumber()));
			if(list.size() > 0) {
				return ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR, "手机号已存在");
			}
		}
		//查询邮箱是否存在
		if(StringUtils.hasText(addUserDto.getEmail())) {
			list = list(new LambdaQueryWrapper<User>()
					.eq(User::getEmail, addUserDto.getEmail()));
			if(list.size() > 0) {
				return ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR, "邮箱已存在");
			}
		}
		User user = BeanCopyUtils.copyBean(addUserDto, User.class);
		//密码加密
		String pre = user.getPassword();
		String post = passwordEncoder.encode(pre);
		user.setPassword(post);
		//新增用户基本信息
		save(user);
		//新增用户关联权限
		Long userId = user.getId();
		List<Long> roleIds = addUserDto.getRoleIds();
		//判断是否增加权限
		if(roleIds.size() > 0) {
			UserMapper userMapper = getBaseMapper();
			userMapper.addUserRole(userId, roleIds);
		}
		return ResponseResult.okResult();
	}

	@Override
	public ResponseResult getUser(Long id) {
		UserMapper userMapper = getBaseMapper();
		//获取用户所关联的角色id列表
		List<Long> roleIds = userMapper.listRoleIds(id);
		//获取所有角色的列表
		List<Role> roles = userMapper.listRoles();
		//获取角色信息
		User user = getById(id);
		EchoUserVo echoUserVo = BeanCopyUtils.copyBean(user, EchoUserVo.class);
		//封装UserVo
		UserVo userVo = new UserVo(roleIds, roles, echoUserVo);
		return ResponseResult.okResult(userVo);
	}

	@Override
	public ResponseResult updateUser(UserUpdateDto userUpdateDto) {
		//更新用户基本信息
		User user = BeanCopyUtils.copyBean(userUpdateDto, User.class);
		updateById(user);
		//更新用户权限
		UserMapper userMapper = getBaseMapper();
		//1.删除用户关联角色
		userMapper.deleteUserRole(user.getId());
		//2.增加选择的关联角色
		List<Long> roleIds = userUpdateDto.getRoleIds();
		if(roleIds.size() > 0) {
			userMapper.addUserRole(user.getId(), roleIds);
		}
		return ResponseResult.okResult();
	}

	@Override
	public ResponseResult deleteUser(Long id) {
		//获取当前用户信息
		LoginUser loginUser = SecurityUtils.getLoginUser();
		Long loginUserId = loginUser.getUser().getId();
		if(loginUserId.equals(id)) {
			return ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR, "不能删除当前用户");
		}
		//删除用户信息
		removeById(id);
		//删除用户关联的角色信息
		//UserMapper userMapper = getBaseMapper();
		//userMapper.deleteUserRole(id);
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

