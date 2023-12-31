package com.keoben.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.keoben.constants.SystemConstants;
import com.keoben.domain.ResponseResult;
import com.keoben.domain.dto.AddArticleDto;
import com.keoben.domain.dto.ArticleListDto;
import com.keoben.domain.entity.Article;
import com.keoben.domain.entity.ArticleTag;
import com.keoben.domain.entity.Category;
import com.keoben.domain.vo.ArticleDetailVo;
import com.keoben.domain.vo.ArticleListVo;
import com.keoben.domain.vo.HotArticleVo;
import com.keoben.domain.vo.PageVo;
import com.keoben.mapper.ArticleMapper;
import com.keoben.service.ArticleService;
import com.keoben.service.ArticleTagService;
import com.keoben.service.CategoryService;
import com.keoben.utils.BeanCopyUtils;
import com.keoben.utils.RedisCache;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {

	@Autowired
	private CategoryService categoryService;

	@Autowired
	private RedisCache redisCache;

	@Autowired
	private ArticleTagService articleTagService;

	@Override
	public ResponseResult hotArticleList() {
		//查询热门文章 封装成ResponseResult返回
		LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
		//必须是正式文章
		queryWrapper.eq(Article::getStatus, SystemConstants.ARTICLE_STATUS_NORMAL);
		//按照浏览量进行排序
		queryWrapper.orderByDesc(Article::getViewCount);
		//最多只查询10条
		Page<Article> page = new Page(1, 10);
		page(page, queryWrapper);
		List<Article> articles = page.getRecords();

		//bean拷贝
		//List<HotArticleVo> articleVos = new ArrayList<>();
		//for (Article article : articles) {
		//	HotArticleVo vo = new HotArticleVo();
		//	BeanUtils.copyProperties(article, vo);
		//	articleVos.add(vo);
		//}
		List<HotArticleVo> vs = BeanCopyUtils.copyBeanList(articles , HotArticleVo.class);

		return ResponseResult.okResult(vs);
	}

	@Override
	public ResponseResult articleList(Integer pageNum, Integer pageSize, Long categoryId) {
		//查询条件
		LambdaQueryWrapper<Article> lambdaQueryWrapper = new LambdaQueryWrapper<>();
		//如果有categoryId就要查询时要和传入的相同
		lambdaQueryWrapper.eq(Objects.nonNull(categoryId) && categoryId > 0,
				Article::getCategoryId, categoryId);
		//状态是正式发布的
		lambdaQueryWrapper.eq(Article::getStatus, SystemConstants.STATUS_NORMAL);
		//对isTop进行降序
		lambdaQueryWrapper.orderByDesc(Article::getIsTop);
		//分页查询
		Page<Article> page = new Page<>(pageNum, pageSize);
		page(page, lambdaQueryWrapper);

		//查询categoryName
		List<Article> articles = page.getRecords();
		//articleId去查询articleName进行设置
		articles = articles.stream()
				.map(article -> article.setCategoryName(categoryService.getById(article.getCategoryId()).getName()))
				.collect(Collectors.toList());

		//for(Article article : articles) {
		//	Category category = categoryService.getById(article.getCategoryId());
		//	article.setCategoryName(category.getName());
		//}


		//封装查询结果
		List<ArticleListVo> articleListVos = BeanCopyUtils.copyBeanList(page.getRecords(), ArticleListVo.class);
		PageVo pageVo = new PageVo(articleListVos, page.getTotal());
		return ResponseResult.okResult(pageVo);
	}

	@Override
	public ResponseResult getArticleDetail(Long id) {
		//根据id查询文章
		Article article = getById(id);
		//从redis中获取viewCount
		Integer viewCount = redisCache.getCacheMapValue("article:viewCount", id.toString());
		article.setViewCount(viewCount.longValue());
		//转换成VO
		ArticleDetailVo articleDetailVo = BeanCopyUtils.copyBean(article, ArticleDetailVo.class);
		//根据分类id查询分类名
		Long categoryId = articleDetailVo.getCategoryId();
		Category category = categoryService.getById(categoryId);
		if (category != null) {
			articleDetailVo.setCategoryName(category.getName());
		}
		//封装响应返回
		return ResponseResult.okResult(articleDetailVo);
	}

	@Override
	public ResponseResult updateViewCount(Long id) {
		//更新redis
		redisCache.incrementCacheMapValue("article:viewCount", id.toString(), 1);
		return ResponseResult.okResult();
	}

	@Override
	@Transactional
	public ResponseResult add(AddArticleDto articleDto) {
		//添加博客
		Article article = BeanCopyUtils.copyBean(articleDto, Article.class);
		save(article);

		List<ArticleTag> articleTags = articleDto.getTags().stream()
				.map(tagId -> new ArticleTag(article.getId(), tagId))
				.collect(Collectors.toList());
		//添加博客和标签的关联
		articleTagService.saveBatch(articleTags);
		return ResponseResult.okResult();
	}

	@Override
	public ResponseResult<PageVo> pageArticleList(Integer pageNum, Integer pageSize, ArticleListDto articleListDto) {
		//分页查询
		LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
		//模糊查询
		queryWrapper.like(StringUtils.hasText(articleListDto.getTitle()),
				Article::getTitle, articleListDto.getTitle());
		queryWrapper.like(StringUtils.hasText(articleListDto.getSummary()),
				Article::getSummary, articleListDto.getSummary());
		Page<Article> page = new Page<>();
		page.setCurrent(pageNum);
		page.setSize(pageSize);
		page(page, queryWrapper);
		//封装数据返回
		PageVo pageVo = new PageVo(page.getRecords(),page.getTotal());
		return ResponseResult.okResult(pageVo);
	}

	@Override
	public Article getArticleById(Long id) {
		Article article = getById(id);
		return article;
	}

	@Override
	public ResponseResult updateArticle(Article article) {
		Long id = article.getId();
		updateById(article);
		return ResponseResult.okResult(getArticleById(id));
	}

	@Override
	public ResponseResult deleteArticleById(Long id) {
		removeById(id);
		return ResponseResult.okResult();
	}


}
