package com.keoben.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.keoben.constants.SystemConstants;
import com.keoben.domain.ResponseResult;
import com.keoben.domain.dto.MenuListDto;
import com.keoben.domain.vo.MenuTreeVo;
import com.keoben.domain.entity.Menu;
import com.keoben.domain.enums.AppHttpCodeEnum;
import com.keoben.domain.vo.PageVo;
import com.keoben.domain.vo.RoleMenuVo;
import com.keoben.mapper.MenuMapper;
import com.keoben.service.MenuService;
import com.keoben.utils.BeanCopyUtils;
import com.keoben.utils.SecurityUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 菜单权限表(Menu)表服务实现类
 *
 * @author makejava
 * @since 2023-06-21 16:11:17
 */
@Service("menuService")
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {

	@Override
	public List<String> selectPermsByUserId(Long id) {
		//如果是管理员 返回所有的权限
		if(SecurityUtils.isAdmin()) {
			LambdaQueryWrapper<Menu> queryWrapper = new LambdaQueryWrapper<>();
			queryWrapper.in(Menu::getMenuType, SystemConstants.MENU, SystemConstants.BUTTON);
			queryWrapper.eq(Menu::getStatus, SystemConstants.STATUS_NORMAL);
			List<Menu> menus = list(queryWrapper);
			List<String> perms = menus.stream()
					.map(Menu::getPerms)
					.collect(Collectors.toList());
			return perms;
		}
		//否则返回去所具有的权限
		return getBaseMapper().selectPermsByUserId(id);
	}

	@Override
	public List<Menu> selectRouterMenuTreeByUserId(Long userId) {
		MenuMapper menuMapper = getBaseMapper();
		List<Menu> menus = null;
		//判断是否是管理员
		if(SecurityUtils.isAdmin()) {
			//如果是 获取所有符合要求的menu
			menus = menuMapper.selectAllRouterMenu();
		} else {
			menus = menuMapper.selectRouterMenuTreeByUserId(userId);
		}
		//构建menuTree
		//先找出第一层的菜单 然后去找他们的子菜单设置到children属性中
		List<Menu> menuTree = builderMenuTree(menus, 0L);
		return menuTree;
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

	@Override
	public ResponseResult<PageVo> pageMenuList(MenuListDto menuListDto) {
		//分页查询
		LambdaQueryWrapper<Menu> queryWrapper = new LambdaQueryWrapper<>();
		//模糊查询
		queryWrapper.like(StringUtils.hasText(menuListDto.getMenuName()),
					Menu::getMenuName, menuListDto.getMenuName());
		queryWrapper.like(StringUtils.hasText(menuListDto.getStatus()),
					Menu::getStatus, menuListDto.getStatus());
		queryWrapper.orderByAsc(Menu::getOrderNum);
		List<Menu> menus = list(queryWrapper);
		return ResponseResult.okResult(menus);
	}

	@Override
	public ResponseResult addMenu(Menu menu) {
		save(menu);
		return ResponseResult.okResult();
	}

	@Override
	public ResponseResult getMenuById(Long id) {
		Menu menu = getById(id);
		return ResponseResult.okResult(menu);
	}

	@Override
	public ResponseResult updateMenu(Menu menu) {
		if(menu.getParentId().equals(menu.getId())) {
			return ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR,
					"修改菜单'写博文'失败，上级菜单不能选择自己");
		}
		updateById(menu);
		return ResponseResult.okResult();
	}

	@Override
	public ResponseResult deleteMenuById(Long id) {
		LambdaQueryWrapper<Menu> wrapper = new LambdaQueryWrapper<>();
		wrapper.eq(Menu::getParentId, id);
		List<Menu> list = list(wrapper);
		if(list.size() > 0) {
			return ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR, "存在子菜单不允许删除");
		}
		removeById(id);
		return ResponseResult.okResult();
	}

	@Override
	public ResponseResult selectMenuTree() {
		MenuMapper menuMapper = getBaseMapper();
		List<Menu> menus = null;
		//获取目录/菜单/按钮
		menus = menuMapper.selectMenuTree();
		//构建menuTree
		//先找出第一层的菜单,然后去找他们的子菜单设置到children属性中去
		List<Menu> menuTree = builderMenuTree(menus, 0L);
		List<MenuTreeVo> list = new ArrayList<>();
		menuTree.stream()
				.forEach(m -> {
					MenuTreeVo dto = new MenuTreeVo();
					dto.setId(m.getId());
					dto.setParentId(m.getParentId());
					dto.setLabel(m.getMenuName());
					changeChildren(dto, m);
					list.add(dto);
				});
		return ResponseResult.okResult(list);
	}

	@Override
	public ResponseResult selectRoleMenuTree(Long id) {
		MenuMapper menuMapper = getBaseMapper();
		List<Menu> menus = null;
		menus = menuMapper.selectAllMenu();
		//封装成AddRoleVo
		List<MenuTreeVo> menuTreeVos = menus.stream()
				.map(menu -> {
					MenuTreeVo menuTreeVo = BeanCopyUtils.copyBean(menu, MenuTreeVo.class);
					menuTreeVo.setLabel(menu.getMenuName());
					return menuTreeVo;
				})
				.collect(Collectors.toList());
		//构建menuTree
		//先找出第一层的菜单,然后去找他们的子菜单设置到children属性中去
		List<MenuTreeVo> menuTreeVoList = builderMenuTreeVo(menuTreeVos, 0L);
		//角色已有的权限,传回以进行回显选中√
		List<Long> checkedKeys = null;
		//判断是否是管理员
		if (id != null && id.equals(1L)) {
			//如果是管理员 获取所有符合要求的菜单id即checkedKey
			checkedKeys = menuMapper.selectAllId();
		} else {
			//如果不是管理员
			checkedKeys = menuMapper.selectMenuId(id);
		}
		RoleMenuVo roleMenuVo = new RoleMenuVo(menuTreeVoList, checkedKeys);
		return ResponseResult.okResult(roleMenuVo);
	}

	private List<MenuTreeVo> builderMenuTreeVo(List<MenuTreeVo> menus, long parentId) {
		List<MenuTreeVo> menuTree = menus.stream()
				.filter(menu -> menu.getParentId().equals(parentId))
				.map(menu -> menu.setChildren(getVoChildren(menu, menus)))
				.collect(Collectors.toList());
		return menuTree;
	}

	private List<MenuTreeVo> getVoChildren(MenuTreeVo menu, List<MenuTreeVo> menus) {
		List<MenuTreeVo> childrenList = menus.stream()
				.filter(m -> m.getParentId().equals(menu.getId()))
				.map(m -> m.setChildren(getVoChildren(m, menus)))
				.collect(Collectors.toList());
		return childrenList;
	}


	private void changeChildren(MenuTreeVo menuTreeVo, Menu menu) {
		List<Menu> menuTree = menu.getChildren();
		List<MenuTreeVo> list = new ArrayList<>();
		menuTree.stream()
				.forEach(m -> {
					MenuTreeVo dto = new MenuTreeVo();
					dto.setId(m.getId());
					dto.setParentId(m.getParentId());
					dto.setLabel(m.getMenuName());
					changeChildren(dto, m);
					list.add(dto);
				});
		menuTreeVo.setChildren(list);
	}

}












