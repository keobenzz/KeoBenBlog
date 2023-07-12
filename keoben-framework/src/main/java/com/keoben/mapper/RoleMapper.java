package com.keoben.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.keoben.domain.entity.Role;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * 角色信息表(Role)表数据库访问层
 *
 * @author makejava
 * @since 2023-06-21 16:18:11
 */
public interface RoleMapper extends BaseMapper<Role> {

	List<String> selectRoleKeyByUserId(Long userId);

	void add(@Param("roleId") Long roleId, @Param("menuId") Long menuId);

	void addBatch(@Param("roleId") Long id, @Param(("menuIds")) List<Long> menuIds);

	void deleteMenu(Long id);
}
