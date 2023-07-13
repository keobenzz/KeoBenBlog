package com.keoben.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.keoben.constants.SystemConstants;
import com.keoben.domain.ResponseResult;
import com.keoben.domain.dto.AddLinkDto;
import com.keoben.domain.dto.LinkListDto;
import com.keoben.domain.dto.UpdateLinkDto;
import com.keoben.domain.entity.Link;
import com.keoben.domain.vo.LinkVo;
import com.keoben.domain.vo.PageVo;
import com.keoben.mapper.LinkMapper;
import com.keoben.service.LinkService;
import com.keoben.utils.BeanCopyUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 友链(Link)表服务实现类
 *
 * @author makejava
 * @since 2023-06-18 14:05:24
 */
@Service("linkService")
public class LinkServiceImpl extends ServiceImpl<LinkMapper, Link> implements LinkService {

	@Override
	public ResponseResult getAllLink() {
		//查询所有审核通过的
		LambdaQueryWrapper<Link> queryWrapper = new LambdaQueryWrapper<>();
		queryWrapper.eq(Link::getStatus, SystemConstants.LINK_STATUS_NORMAL);
		List<Link> links = list(queryWrapper);
		//转换成Vo
		List<LinkVo> linkVos = BeanCopyUtils.copyBeanList(links, LinkVo.class);
		return ResponseResult.okResult(linkVos);
	}

	@Override
	public ResponseResult pageLinkList(Integer pageNum, Integer pageSize, LinkListDto linkListDto) {

		LambdaQueryWrapper<Link> wrapper = new LambdaQueryWrapper<>();
		wrapper.like(StringUtils.hasText(linkListDto.getName()),
					Link::getName, linkListDto.getName())
				.like(StringUtils.hasText(linkListDto.getStatus()),
					Link::getStatus, linkListDto.getStatus());
		Page<Link> page = new Page<>();
		page.setCurrent(pageNum);
		page.setSize(pageSize);
		page(page, wrapper);
		PageVo pageVo = new PageVo(page.getRecords(), page.getTotal());
		return ResponseResult.okResult(pageVo);
	}

	@Override
	public ResponseResult addLink(AddLinkDto addLinkDto) {
		Link link = BeanCopyUtils.copyBean(addLinkDto, Link.class);
		save(link);
		return ResponseResult.okResult(link);
	}

	@Override
	public ResponseResult getLink(Long id) {
		Link link = getById(id);
		LinkVo linkVo = BeanCopyUtils.copyBean(link, LinkVo.class);
		return ResponseResult.okResult(linkVo);
	}

	@Override
	public ResponseResult updateLink(UpdateLinkDto updateLinkDto) {
		Link link = BeanCopyUtils.copyBean(updateLinkDto, Link.class);
		updateById(link);
		return ResponseResult.okResult(link);
	}

	@Override
	public ResponseResult deleteLink(Long id) {
		removeById(id);
		return ResponseResult.okResult();
	}
}

