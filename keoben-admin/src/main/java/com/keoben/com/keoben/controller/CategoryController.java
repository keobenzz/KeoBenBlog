package com.keoben.com.keoben.controller;

import com.keoben.domain.ResponseResult;
import com.keoben.domain.entity.Category;
import com.keoben.domain.vo.CategoryVo;
import com.keoben.domain.vo.TagVo;
import com.keoben.service.CategoryService;
import com.keoben.service.TagService;
import javafx.stage.StageStyle;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/content/category")
public class CategoryController {

	@Autowired
	private CategoryService categoryService;
	@Autowired
	private TagService tagService;

	@GetMapping("/listAllCategory")
	public ResponseResult listAllcategory() {
		List<CategoryVo> list = categoryService.listAllcategory();
		return ResponseResult.okResult(list);
	}



}
