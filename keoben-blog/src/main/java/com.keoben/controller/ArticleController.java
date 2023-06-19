package com.keoben.controller;

import com.keoben.domain.ResponseResult;
import com.keoben.domain.entity.Article;
import com.keoben.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//@CrossOrigin //注解 配置 一个起效两个都启用则都会失效
@RestController
@RequestMapping("/article")
public class ArticleController {

	@Autowired
	private ArticleService articleService;

	@GetMapping("/hotArticleList")
	public ResponseResult hotArticleList(){
		return articleService.hotArticleList();
	}

	@GetMapping("/articleList")
	public ResponseResult articleList(Integer pageNum, Integer pageSize, Long categoryId) {
		return articleService.articleList(pageNum, pageSize, categoryId);
	}

	@GetMapping("/{id}")
	public ResponseResult getArticleDetail(@PathVariable("id") Long id) {
		return articleService.getArticleDetail(id);
	}

}
