package com.keoben.com.keoben.controller;

import com.keoben.domain.ResponseResult;
import com.keoben.domain.dto.MenuListDto;
import com.keoben.domain.entity.Menu;
import com.keoben.domain.vo.PageVo;
import com.keoben.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/system/menu")
public class MenuController {

	@Autowired
	private MenuService menuService;

	@GetMapping("/list")
	public ResponseResult<PageVo> pageMenuList(MenuListDto menuListDto){
		return menuService.pageMenuList(menuListDto);
	}

	@PostMapping
	public ResponseResult addMenu(@RequestBody Menu Menu) {
		return menuService.addMenu(Menu);
	}

	@GetMapping("{id}")
	public ResponseResult getMenu(@PathVariable Long id) {
		return menuService.getMenuById(id);
	}

	@PutMapping()
	public ResponseResult updateMenu(@RequestBody Menu menu) {
		return menuService.updateMenu(menu);
	}

	@DeleteMapping("{menuId}")
	public ResponseResult deleteMenu(@PathVariable Long menuId) {
		return menuService.deleteMenuById(menuId);
	}

	@GetMapping("/treeselect")
	public ResponseResult selectMenuTree() {
		return menuService.selectMenuTree();
	}

	@GetMapping("/roleMenuTreeselect/{id}")
	public ResponseResult selectRoleMenuTree(@PathVariable Long id){
		return menuService.selectRoleMenuTree(id);
	}

}
