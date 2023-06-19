package com.keoben.domain.vo;

import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArticleListVo {

	private Long id;
	//标题
	private String title;
	//文章摘要
	private String summary;
	//所属分类名
	private String categoryName;
	//缩略图
	private String thumbnail;
	//访问量
	private Long viewCount;
	@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
	private Date createTime;

}
