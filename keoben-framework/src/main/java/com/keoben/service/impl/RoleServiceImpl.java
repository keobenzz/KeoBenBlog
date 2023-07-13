package com.keoben.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.keoben.domain.ResponseResult;
import com.keoben.domain.vo.AddRoleVo;
import com.keoben.domain.dto.ChangeStatusRoleDto;
import com.keoben.domain.dto.RoleListDto;
import com.keoben.domain.dto.UpdateRoleDto;
import com.keoben.domain.entity.Menu;
import com.keoben.domain.entity.Role;
import com.keoben.domain.vo.PageVo;
import com.keoben.mapper.RoleMapper;
import com.keoben.service.RoleService;
import com.keoben.utils.BeanCopyUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 角色信息表(Role)表服务实现类
 *
 * @author makejava
 * @since 2023-06-21 16:19:29
 */
@Service("roleService")
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

	@Override
	public List<String> selectRoleKeyByUserId(Long id) {
		//判断是否是管理员 如果是返回集合中只需要有admin
		if(id == 1L) {
			List<String> roleKeys = new ArrayList<>();
			roleKeys.add("admin");
			return roleKeys;
		}
		//否则查询用户所具有的角色信息
		return getBaseMapper().selectRoleKeyByUserId(id);
	}

	@Override
	public ResponseResult<PageVo> pageRoleList(Integer pageNum, Integer pageSize, RoleListDto roleListDto) {
		LambdaQueryWrapper<Role> wrapper = new LambdaQueryWrapper<>();
		wrapper.like(StringUtils.hasText(roleListDto.getRoleName()),
				Role::getRoleName, roleListDto.getRoleName());
		wrapper.eq(StringUtils.hasText(roleListDto.getStatus()),
				Role::getStatus, roleListDto.getRoleName());
		wrapper.orderByAsc(Role::getRoleSort);

		Page<Role> page = new Page<>();
		page.setCurrent(pageNum);
		page.setSize(pageSize);
		page(page, wrapper);
		PageVo pageVo = new PageVo(page.getRecords(), page.getTotal());
		return ResponseResult.okResult(pageVo);
	}

	@Override
	public ResponseResult changeStatus(ChangeStatusRoleDto changeStatusRoleDto) {
		Role role = new Role();
		role.setId(changeStatusRoleDto.getRoleId());
		role.setStatus(changeStatusRoleDto.getStatus());
		updateById(role);
		return ResponseResult.okResult();
	}

	@Override
	public ResponseResult addRole(AddRoleVo addRoleVo) {
		Role role = BeanCopyUtils.copyBean(addRoleVo, Role.class);
		save(role);
		List<Long> menuIds = addRoleVo.getMenuIds();
		//单次循环插入
		//menuIds.stream()
		//		.forEach(menuId -> {
		//			roleMapper.add(role.getId(), menuId);
		//		});
		//批量插入
		RoleMapper roleMapper = getBaseMapper();
		roleMapper.addBatch(role.getId(), menuIds);
		return ResponseResult.okResult();
	}

	@Override
	public ResponseResult selectRoleList(Long id) {
		Role role = getById(id);
		AddRoleVo addRoleVo = BeanCopyUtils.copyBean(role, AddRoleVo.class);
		return ResponseResult.okResult(addRoleVo);
	}

	@Override
	public ResponseResult updateRole(UpdateRoleDto updateRoleDto) {
		Role role = BeanCopyUtils.copyBean(updateRoleDto, Role.class);
		//修改基本信息
		updateById(role);
		//修改role_menu权限
		RoleMapper roleMapper = getBaseMapper();
		//删除原有权限
		roleMapper.deleteMenu(updateRoleDto.getId());
		//添加新权限
		List<Long> menuIds = updateRoleDto.getMenuIds();
		roleMapper.addBatch(role.getId(), menuIds);
		return ResponseResult.okResult();
	}

	@Override
	public ResponseResult deleteRole(Long id) {
		RoleMapper roleMapper = getBaseMapper();
		//删除角色权限
		roleMapper.deleteMenu(id);
		// 删除角色
		removeById(id);
		return ResponseResult.okResult();
	}

	@Override
	public ResponseResult listAllRole() {
		List<Role> roles = list();
		return ResponseResult.okResult(roles);
	}


	private List<Menu> builderMenuTree(List<Menu> menus, long parentId) {
		List<Menu> menuTree = menus.stream()
				.filter(menu -> menu.getParentId().equals(parentId))
				.map(menu -> menu.setChildren(getChildren(menu, menus)))
				.collect(Collectors.toList());
		return menuTree;
	}
	//获取存入参数的 子menu
	private List<Menu> getChildren(Menu menu, List<Menu> menus) {
		List<Menu> childrenList = menus.stream()
				.filter(m -> m.getParentId().equals(menu.getId()))
				.map(m -> m.setChildren(getChildren(m, menus)))
				.collect(Collectors.toList());
		return childrenList;
	}
}

