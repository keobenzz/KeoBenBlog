package com.keoben.com.keoben.controller;

import com.keoben.domain.ResponseResult;
import com.keoben.domain.dto.AddLinkDto;
import com.keoben.domain.dto.LinkListDto;
import com.keoben.service.LinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/content/link")
public class LinkController {

	@Autowired
	private LinkService linkService;

	@GetMapping("/list")
	public ResponseResult pageLinkList(Integer pageNum, Integer pageSize, LinkListDto linkListDto) {
		return linkService.pageLinkList(pageNum, pageSize, linkListDto);
	}

	@PostMapping
	public ResponseResult addLink(@RequestBody AddLinkDto addLinkDto) {
		return linkService.addLink(addLinkDto);
	}

}
