package com.keoben.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.keoben.domain.entity.Menu;

import java.util.List;

/**
 * 菜单权限表(Menu)表服务接口
 *
 * @author makejava
 * @since 2023-06-21 16:11:17
 */
public interface MenuService extends IService<Menu> {

	List<String> selectPermsByUserId(Long id);

	List<Menu> selectRouterMenuTreeByUserId(Long userId);
}
