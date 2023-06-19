package com.keoben.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.keoben.domain.ResponseResult;
import com.keoben.domain.entity.Link;

/**
 * 友链(Link)表服务接口
 *
 * @author makejava
 * @since 2023-06-18 14:05:24
 */
public interface LinkService extends IService<Link> {

	ResponseResult getAllLink();
}

