package com.keoben.domain.vo;

import com.keoben.domain.entity.Role;
import com.keoben.domain.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserVo {

	//用户所关联的角色id列表
	private List<Long> roleIds;
	//所有用户的列表
	private List<Role> roles;
	//用户信息
	private EchoUserVo user;

}
