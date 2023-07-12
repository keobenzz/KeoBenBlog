package com.keoben.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MenuTreeDto {
	private List<MenuTreeDto> children;
	private Long id;
	private String label;
	private Long parentId;
}
