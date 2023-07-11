package com.keoben.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.keoben.domain.entity.Role;

import java.util.List;

/**
 * 角色信息表(Role)表服务接口
 *
 * @author makejava
 * @since 2023-06-21 16:18:11
 */
public interface RoleService extends IService<Role> {

	List<String> selectRoleKeyByUserId(Long id);
}

