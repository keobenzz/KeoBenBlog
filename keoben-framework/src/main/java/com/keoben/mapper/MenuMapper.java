package com.keoben.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.keoben.domain.entity.Menu;

import java.util.List;


/**
 * 菜单权限表(Menu)表数据库访问层
 *
 * @author makejava
 * @since 2023-06-21 16:11:17
 */
public interface MenuMapper extends BaseMapper<Menu> {

	List<String> selectPermsByUserId(Long id);

	List<Menu> selectAllRouterMenu();

	List<Menu> selectRouterMenuTreeByUserId(Long userId);

	List<Menu> selectMenuTree();

	List<Long> selectMenuId(Long id);

	List<Menu> selectAllMenu();

	List<Long> selectAllId();
}
