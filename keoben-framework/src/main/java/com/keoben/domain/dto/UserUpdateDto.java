package com.keoben.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateDto {

	//邮箱
	private String email;
	//主键@TableId
	private Long id;
	//昵称
	private String nickName;
	//用户性别（0男，1女，2未知）
	private String sex;
	//账号状态（0正常 1停用）
	private String status;
	//用户名
	private String userName;
	//用户关联的角色ids
	List<Long> roleIds;

}
