package com.keoben.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.keoben.constants.SystemConstants;
import com.keoben.domain.ResponseResult;
import com.keoben.domain.entity.Link;
import com.keoben.domain.vo.LinkVo;
import com.keoben.mapper.LinkMapper;
import com.keoben.service.LinkService;
import com.keoben.utils.BeanCopyUtils;
import org.springframework.stereotype.Service;

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
}

