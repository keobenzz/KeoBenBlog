package com.keoben.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.keoben.domain.ResponseResult;
import com.keoben.domain.dto.TagListDto;
import com.keoben.domain.entity.Tag;
import com.keoben.domain.vo.PageVo;
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
		queryWrapper.eq(StringUtils.hasText(tagListDto.getName()),Tag::getName,tagListDto.getName());
		queryWrapper.eq(StringUtils.hasText(tagListDto.getRemark()),Tag::getRemark,tagListDto.getRemark());

		Page<Tag> page = new Page<>();
		page.setCurrent(pageNum);
		page.setSize(pageSize);
		page(page, queryWrapper);
		//封装数据返回
		PageVo pageVo = new PageVo(page.getRecords(),page.getTotal());
		return ResponseResult.okResult(pageVo);
	}
}

