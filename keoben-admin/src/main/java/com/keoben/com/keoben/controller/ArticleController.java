package com.keoben.com.keoben.controller;

import com.keoben.domain.ResponseResult;
import com.keoben.domain.dto.AddArticleDto;
import com.keoben.domain.dto.ArticleListDto;
import com.keoben.domain.entity.Article;
import com.keoben.domain.vo.PageVo;
import com.keoben.service.ArticleService;
import com.keoben.utils.BeanCopyUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/content/article")
public class ArticleController {

	@Autowired
	private ArticleService articleService;

	@PostMapping
	public ResponseResult add(@RequestBody AddArticleDto article) {
		return articleService.add(article);
	}

	@GetMapping("/list")
	public ResponseResult<PageVo> pageArticleList(Integer pageNum, Integer pageSize, ArticleListDto articleListDto) {
		return articleService.pageArticleList(pageNum, pageSize, articleListDto);
	}

	@GetMapping("{id}")
	public ResponseResult getArticle(@PathVariable Long id) {
		return ResponseResult.okResult(articleService.getArticleById(id));
	}

	@PutMapping()
	public ResponseResult updateArticle(@RequestBody Article article) {
		return articleService.updateArticle(article);
	}

	@DeleteMapping("{id}")
	public ResponseResult deleteArticle(@PathVariable Long id) {
		return articleService.deleteArticleById(id);
	}

}

