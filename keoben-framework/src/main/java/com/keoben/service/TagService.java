package com.keoben.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.keoben.domain.ResponseResult;
import com.keoben.domain.dto.TagListDto;
import com.keoben.domain.entity.Tag;
import com.keoben.domain.vo.PageVo;
import com.keoben.domain.vo.TagVo;

import java.util.List;

/**
 * 标签(Tag)表服务接口
 *
 * @author makejava
 * @since 2023-06-21 13:55:15
 */
public interface TagService extends IService<Tag> {

	ResponseResult<PageVo> pageTagList(Integer pageNum, Integer pageSize, TagListDto tagListDto);

	ResponseResult addTag(Tag tag);

	ResponseResult deleteTag(Long id);

	ResponseResult getTag(Long id);

	ResponseResult updateTag(Tag tag);

	List<TagVo> listAllTag();
}

