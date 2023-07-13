package com.keoben.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.keoben.constants.SystemConstants;
import com.keoben.domain.ResponseResult;
import com.keoben.domain.dto.CategoryListDto;
import com.keoben.domain.entity.Article;
import com.keoben.domain.entity.Category;
import com.keoben.domain.vo.CategoryVo;
import com.keoben.domain.vo.PageVo;
import com.keoben.mapper.CategoryMapper;
import com.keoben.service.ArticleService;
import com.keoben.service.CategoryService;
import com.keoben.utils.BeanCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 分类表(Category)表服务实现类
 *
 * @author makejava
 * @since 2023-06-17 20:31:42
 */
@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

	@Autowired
	private ArticleService articleService;

	@Override
	public ResponseResult getCategoryList() {
		//查询文章表 状态为已发布的
		LambdaQueryWrapper<Article> articleWrapper = new LambdaQueryWrapper<>();
		articleWrapper.eq(Article::getStatus, SystemConstants.ARTICLE_STATUS_NORMAL);
		List<Article> articleList = articleService.list(articleWrapper);
		//获取文章的分类id,并且去重
		Set<Long> categoryIds = articleList.stream()
				.map(article -> article.getCategoryId())
				.collect(Collectors.toSet());
		//查询分类表
		List<Category> categories = listByIds(categoryIds);
		categories = categories.stream()
				.filter(category -> SystemConstants.STATUS_NORMAL.equals(category.getStatus()))
				.collect(Collectors.toList());
		//封装vo
		List<CategoryVo> categoryVos = BeanCopyUtils.copyBeanList(categories, CategoryVo.class);

		return ResponseResult.okResult(categoryVos);
	}

	@Override
	public List<CategoryVo> listAllcategory() {
		LambdaQueryWrapper<Category> wrapper = new LambdaQueryWrapper<>();
		wrapper.eq(Category::getStatus, SystemConstants.NORMAL);
		List<Category> list = list(wrapper);
		List<CategoryVo> categoryVos = BeanCopyUtils.copyBeanList(list, CategoryVo.class);
		return categoryVos;
	}

	@Override
	public ResponseResult pageCategoryList(Integer pageNum, Integer pageSize, CategoryListDto categoryListDto) {
		LambdaQueryWrapper<Category> wrapper = new LambdaQueryWrapper<>();
		wrapper.like(StringUtils.hasText(categoryListDto.getName()),
					Category::getName, categoryListDto.getName())
				.like(StringUtils.hasText(categoryListDto.getStatus()),
					Category::getStatus, categoryListDto.getStatus());
		Page<Category> page = new Page<>();
		page.setCurrent(pageNum);
		page.setSize(pageSize);
		page(page, wrapper);
		PageVo pageVo = new PageVo(page.getRecords(), page.getTotal());
		return ResponseResult.okResult(pageVo);
	}

}

