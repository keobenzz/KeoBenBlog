package com.keoben.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
//import .dao.ArticleTagDao;
//import .entity.ArticleTag;
//import .service.ArticleTagService;
import com.keoben.domain.entity.ArticleTag;
import com.keoben.mapper.ArticleTagMapper;
import com.keoben.service.ArticleTagService;
import org.springframework.stereotype.Service;

/**
 * 文章标签关联表(ArticleTag)表服务实现类
 *
 * @author makejava
 * @since 2023-07-11 14:18:40
 */
@Service("articleTagService")
public class ArticleTagServiceImpl extends ServiceImpl<ArticleTagMapper, ArticleTag> implements ArticleTagService {

}

