package com.keoben.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArticleListDto {
	//文章标题
	private String title;
	//文章摘要
	private String summary;
}
