package com.keoben.com.keoben.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSON;
import com.keoben.domain.ResponseResult;
import com.keoben.domain.dto.CategoryListDto;
import com.keoben.domain.entity.Category;
import com.keoben.domain.enums.AppHttpCodeEnum;
import com.keoben.domain.vo.CategoryVo;
import com.keoben.domain.vo.ExcelCategoryVo;
import com.keoben.domain.vo.TagVo;
import com.keoben.service.CategoryService;
import com.keoben.service.TagService;
import com.keoben.utils.BeanCopyUtils;
import com.keoben.utils.WebUtils;
import javafx.stage.StageStyle;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
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

	@PreAuthorize("@ps.hasPermission('content:category:export')")
	@GetMapping("/export")
	public void export(HttpServletResponse response){
		try {
			//设置下载文件的请求头
			WebUtils.setDownLoadHeader("分类.xlsx",response);
			//获取需要导出的数据
			List<Category> categoryVos = categoryService.list();

			List<ExcelCategoryVo> excelCategoryVos = BeanCopyUtils.copyBeanList(categoryVos, ExcelCategoryVo.class);
			//把数据写入到Excel中
			EasyExcel.write(response.getOutputStream(), ExcelCategoryVo.class).autoCloseStream(Boolean.FALSE).sheet("分类导出")
					.doWrite(excelCategoryVos);

		} catch (Exception e) {
			//如果出现异常也要响应json
			ResponseResult result = ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR);
			WebUtils.renderString(response, JSON.toJSONString(result));
		}
	}

	@GetMapping("/list")
	public ResponseResult pageCategoryList(Integer pageNum, Integer pageSize, CategoryListDto categoryListDto) {
		return categoryService.pageCategoryList(pageNum, pageSize, categoryListDto);
	}

}
