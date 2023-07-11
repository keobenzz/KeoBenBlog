package com.keoben.com.keoben.controller;

import com.keoben.domain.ResponseResult;
import com.keoben.domain.dto.TagListDto;
import com.keoben.domain.entity.Tag;
import com.keoben.domain.vo.PageVo;
import com.keoben.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sun.security.timestamp.TSRequest;

@RestController
@RequestMapping("/content/tag")
public class TagController {

	@Autowired
	private TagService tagService;

	//@GetMapping("/list")
	//public ResponseResult list() {
	//	return ResponseResult.okResult(TagService.list());
	//}

	@GetMapping("/list")
	public ResponseResult<PageVo> list(Integer pageNum, Integer pageSize, TagListDto tagListDto) {
		return tagService.pageTagList(pageNum, pageSize, tagListDto);
	}

	@PostMapping
	public ResponseResult addTag(@RequestBody Tag tag) {
		return tagService.addTag(tag);
	}

	@DeleteMapping("{id}")
	public ResponseResult deletetag(@PathVariable("id") Integer id) {
		return tagService.deleteTag(id);
	}

	@GetMapping("{id}")
	public ResponseResult getTag(@PathVariable("id") Integer id) {
		return tagService.getTag(id);
	}

	@PutMapping
	public ResponseResult updateTag(@RequestBody Tag tag) {
		return tagService.updateTag(tag);
	}

}
