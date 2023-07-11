package com.keoben.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.keoben.domain.entity.Role;

import java.util.List;


/**
 * 角色信息表(Role)表数据库访问层
 *
 * @author makejava
 * @since 2023-06-21 16:18:11
 */
public interface RoleMapper extends BaseMapper<Role> {

	List<String> selectRoleKeyByUserId(Long userId);
}
