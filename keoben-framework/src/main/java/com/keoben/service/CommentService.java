package com.keoben.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.keoben.domain.ResponseResult;
import com.keoben.domain.entity.Comment;

/**
 * 评论表(Comment)表服务接口
 *
 * @author makejava
 * @since 2023-06-19 10:26:10
 */
public interface CommentService extends IService<Comment> {

	ResponseResult commentList(String commentType, Long articleId, Integer pageNum, Integer pageSize);

	ResponseResult addComment(Comment comment);
}

