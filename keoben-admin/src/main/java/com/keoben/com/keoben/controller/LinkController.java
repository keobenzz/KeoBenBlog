package com.keoben.com.keoben.controller;

import com.keoben.domain.ResponseResult;
import com.keoben.domain.dto.LinkListDto;
import com.keoben.service.LinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/content/link")
public class LinkController {

	@Autowired
	private LinkService linkService;

	@GetMapping("/list")
	public ResponseResult pageLinkList(Integer pageNum, Integer pageSize, LinkListDto linkListDto) {
		return linkService.pageLinkList(pageNum, pageSize, linkListDto);
	}

}
