package com.keoben.com.keoben.controller;

import com.keoben.domain.ResponseResult;
import com.keoben.domain.dto.ChangeStatusRoleDto;
import com.keoben.domain.dto.RoleListDto;
import com.keoben.domain.vo.PageVo;
import com.keoben.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/system/role")
public class RoleController {

	@Autowired
	private RoleService roleService;

	@GetMapping("/list")
	public ResponseResult<PageVo> pageRoleList(Integer pageNum, Integer pageSize, RoleListDto roleListDto) {
		return roleService.pageRoleList(pageNum, pageSize, roleListDto);
	}

	@PutMapping("/changeStatus")
	public ResponseResult changeStatus(@RequestBody ChangeStatusRoleDto changeStatusRoleDto) {
		return roleService.changeStatus(changeStatusRoleDto);
	}

}
