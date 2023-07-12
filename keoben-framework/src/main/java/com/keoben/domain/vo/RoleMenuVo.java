package com.keoben.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoleMenuVo {
	List<MenuTreeVo> menus;
	List<Long> checkedKeys;
}
