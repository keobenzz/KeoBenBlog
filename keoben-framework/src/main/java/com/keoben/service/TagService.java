package com.keoben.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.keoben.domain.ResponseResult;
import com.keoben.domain.dto.TagListDto;
import com.keoben.domain.entity.Tag;
import com.keoben.domain.vo.PageVo;

/**
 * 标签(Tag)表服务接口
 *
 * @author makejava
 * @since 2023-06-21 13:55:15
 */
public interface TagService extends IService<Tag> {

	ResponseResult<PageVo> pageTagList(Integer pageNum, Integer pageSize, TagListDto tagListDto);

	ResponseResult addTag(Tag tag);

	ResponseResult deleteTag(Integer id);

	ResponseResult getTag(Integer id);

	ResponseResult updateTag(Tag tag);
}

