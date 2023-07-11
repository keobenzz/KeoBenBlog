package com.keoben.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.keoben.domain.ResponseResult;
import com.keoben.domain.dto.RoleListDto;
import com.keoben.domain.entity.Role;
import com.keoben.domain.vo.PageVo;

import java.util.List;

/**
 * 角色信息表(Role)表服务接口
 *
 * @author makejava
 * @since 2023-06-21 16:18:11
 */
public interface RoleService extends IService<Role> {

	List<String> selectRoleKeyByUserId(Long id);

	ResponseResult<PageVo> pageRoleList(Integer pageNum, Integer pageSize, RoleListDto roleListDto);
}

