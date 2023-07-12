package com.keoben.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true) //使get/set有返回值
public class MenuTreeVo {
	private List<MenuTreeVo> children;
	private Long id;
	private String label;
	private Long parentId;
}
