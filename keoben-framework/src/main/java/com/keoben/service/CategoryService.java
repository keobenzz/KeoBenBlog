package com.keoben.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.keoben.domain.ResponseResult;
import com.keoben.domain.entity.Category;

/**
 * 分类表(Category)表服务接口
 *
 * @author makejava
 * @since 2023-06-17 20:31:42
 */
public interface CategoryService extends IService<Category> {

	ResponseResult getCategoryList();
}

