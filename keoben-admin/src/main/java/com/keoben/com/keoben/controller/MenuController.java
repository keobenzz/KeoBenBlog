package com.keoben.com.keoben.controller;

import com.keoben.domain.ResponseResult;
import com.keoben.domain.dto.MenuListDto;
import com.keoben.domain.vo.PageVo;
import com.keoben.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/system/menu")
public class MenuController {

	@Autowired
	private MenuService menuService;

	@GetMapping("/list")
	public ResponseResult<PageVo> pageMenuList(MenuListDto menuListDto){
		return menuService.pageMenuList(menuListDto);
	}

}
