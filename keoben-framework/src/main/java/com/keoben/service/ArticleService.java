package com.keoben.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.keoben.domain.ResponseResult;
import com.keoben.domain.dto.AddArticleDto;
import com.keoben.domain.dto.ArticleListDto;
import com.keoben.domain.entity.Article;
import com.keoben.domain.vo.PageVo;

public interface ArticleService extends IService<Article> {

	ResponseResult hotArticleList();

	ResponseResult articleList(Integer pageNum, Integer pageSize, Long categoryId);

	ResponseResult getArticleDetail(Long id);

	ResponseResult updateViewCount(Long id);

	ResponseResult add(AddArticleDto article);

	ResponseResult<PageVo> pageArticleList(Integer pageNum, Integer pageSize, ArticleListDto articleListDto);

	Article getArticleById(Long id);

	ResponseResult updateArticle(Article article);

	ResponseResult deleteArticleById(Long id);
}
