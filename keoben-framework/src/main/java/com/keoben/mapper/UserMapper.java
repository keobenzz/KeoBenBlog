package com.keoben.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.keoben.domain.entity.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * 用户表(User)表数据库访问层
 *
 * @author makejava
 * @since 2023-06-18 14:35:14
 */
public interface UserMapper extends BaseMapper<User> {

	void addUserRole(@Param("userId") Long userId, @Param("roleIds") List<Long> roleIds);
}
