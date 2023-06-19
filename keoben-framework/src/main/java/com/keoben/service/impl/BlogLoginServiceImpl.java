package com.keoben.service.impl;

import com.keoben.domain.ResponseResult;
import com.keoben.domain.entity.LoginUser;
import com.keoben.domain.entity.User;
import com.keoben.domain.vo.BlogUserLoginVo;
import com.keoben.domain.vo.UserInfoVo;
import com.keoben.service.BlogLoginService;
import com.keoben.utils.BeanCopyUtils;
import com.keoben.utils.JwtUtil;
import com.keoben.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class BlogLoginServiceImpl implements BlogLoginService {

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
		if(Objects.isNull(authenticate)) {
			throw new RuntimeException("用户名或密码错误");
		}
		//获取userid,生产token
		LoginUser loginUser = (LoginUser) authenticate.getPrincipal();
		String userId = loginUser.getUser().getId().toString();
		String jwt = JwtUtil.createJWT(userId);
		//把用户信息存入redis
		redisCache.setCacheObject("bloglogin:" + userId, loginUser);

		//把token和userinfo封装返回
		//把User转换成UserInfoVo
		UserInfoVo userInfoVo = BeanCopyUtils.copyBean(loginUser.getUser(), UserInfoVo.class);
		BlogUserLoginVo vo = new BlogUserLoginVo(jwt, userInfoVo);
		return ResponseResult.okResult(vo);
	}

	@Override
	public ResponseResult logout() {
		//获取token 解析获取userid
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		LoginUser loginUser = (LoginUser) authentication.getPrincipal();
		//获取userid
		Long userId = loginUser.getUser().getId();
		//删除redis中的用户信息
		redisCache.deleteObject("bloglogin:" + userId);
		return ResponseResult.okResult();
	}
}
