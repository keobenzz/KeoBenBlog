package com.keoben.job;

import com.keoben.domain.entity.Article;
import com.keoben.service.ArticleService;
import com.keoben.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class UpdateViewCountJob {

	@Autowired
	private RedisCache redisCache;

	@Autowired
	private ArticleService articleService;

	@Scheduled(cron = "0/20 * * * * ?")
	public void updateViewCount() {
		//获取Redis中的浏览量
		Map<String, Integer> viewCountMap = redisCache.getCacheMap("article:viewCount");
		List<Article> articles = viewCountMap.entrySet().stream()
				.map(entry -> new Article(Long.valueOf(entry.getKey()), entry.getValue().longValue()))
				.collect(Collectors.toList());
		//更新到数据库中
		articleService.updateBatchById(articles);
	}

}
