package com.keoben.domain.vo;

import com.keoben.domain.entity.Menu;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoutersVo {

	private List<Menu> menus;

}
