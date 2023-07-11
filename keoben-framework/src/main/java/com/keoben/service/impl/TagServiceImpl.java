package com.keoben.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.keoben.domain.ResponseResult;
import com.keoben.domain.dto.TagListDto;
import com.keoben.domain.entity.Tag;
import com.keoben.domain.enums.AppHttpCodeEnum;
import com.keoben.domain.vo.PageVo;
import com.keoben.exception.SystemException;
import com.keoben.mapper.TagMapper;
import com.keoben.service.TagService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * 标签(Tag)表服务实现类
 *
 * @author makejava
 * @since 2023-06-21 13:55:15
 */
@Service("tagService")
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements TagService {

	@Override
	public ResponseResult<PageVo> pageTagList(Integer pageNum, Integer pageSize, TagListDto tagListDto) {
		//分页查询
		LambdaQueryWrapper<Tag> queryWrapper = new LambdaQueryWrapper<>();
		//queryWrapper.eq(StringUtils.hasText(tagListDto.getName()),Tag::getName,tagListDto.getName());
		//queryWrapper.eq(StringUtils.hasText(tagListDto.getRemark()),Tag::getRemark,tagListDto.getRemark());
		//模糊查询
		queryWrapper.like(StringUtils.hasText(tagListDto.getName()),Tag::getName,tagListDto.getName());
		queryWrapper.like(StringUtils.hasText(tagListDto.getRemark()),Tag::getRemark,tagListDto.getRemark());

		Page<Tag> page = new Page<>();
		page.setCurrent(pageNum);
		page.setSize(pageSize);
		page(page, queryWrapper);
		//封装数据返回
		PageVo pageVo = new PageVo(page.getRecords(),page.getTotal());
		return ResponseResult.okResult(pageVo);
	}

	@Override
	public ResponseResult addTag(Tag tag) {
		//增加标签
		if(!StringUtils.hasText(tag.getName()) || !StringUtils.hasText(tag.getRemark())) {
			throw new SystemException((AppHttpCodeEnum.TAG_NAME_REMARK_NOT_NULL));
		}
		save(tag);
		return ResponseResult.okResult();
	}

	@Override
	public ResponseResult deleteTag(Integer id) {
		removeById(id);
		return ResponseResult.okResult();
	}

	@Override
	public ResponseResult getTag(Integer id) {
		Tag tag = getById(id);
		return ResponseResult.okResult(tag);
	}

	@Override
	public ResponseResult updateTag(Tag tag) {
		updateById(tag);
		return ResponseResult.okResult();
	}
}

