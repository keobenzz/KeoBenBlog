package com.keoben.service.impl;

import com.keoben.domain.ResponseResult;
import com.keoben.domain.entity.LoginUser;
import com.keoben.domain.entity.User;
import com.keoben.service.LoginService;
import com.keoben.utils.JwtUtil;
import com.keoben.utils.RedisCache;
import com.keoben.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
public class SystemLoginServiceImpl implements LoginService {

	//在SecurityConfig中配置把AuthenticationManager注入容器,
	//让BlogLoginService通过authenticate方法来进行用户认证
	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private RedisCache redisCache;

	@Override
	public ResponseResult login(User user) {
		UsernamePasswordAuthenticationToken authenticationToken =
				new UsernamePasswordAuthenticationToken(user.getUserName(), user.getPassword());
		//返回一个未认证的Authentication对象
		//然后去manager呢委托方法进行验证
		//底层调用detail与数据库交互验证
		//最终返回一个验证的Auth对象 最终返回上下文
		//authenticationManager回去调用UserDetailServiceImpl(自定义)
		Authentication authenticate = authenticationManager.authenticate(authenticationToken);
		//判断是否认证通过
		if (Objects.isNull(authenticate)) {
			throw new RuntimeException("用户名或密码错误");
		}
		//获取userid,生产token
		LoginUser loginUser = (LoginUser) authenticate.getPrincipal();
		String userId = loginUser.getUser().getId().toString();
		String jwt = JwtUtil.createJWT(userId);
		//把用户信息存入redis
		redisCache.setCacheObject("login:" + userId, loginUser);

		//把token封装 返回
		Map<String, String> map = new HashMap<>();
		map.put("token", jwt);
		return ResponseResult.okResult(map);
	}

	@Override
	public ResponseResult logout() {
		//获取当前登录的用户id
		Long userId = SecurityUtils.getUserId();
		//删除redis中对应的值
		redisCache.deleteObject("login:" + userId);
		return ResponseResult.okResult();
	}
}

