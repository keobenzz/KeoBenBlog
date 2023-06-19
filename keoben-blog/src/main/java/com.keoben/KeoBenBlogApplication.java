package com.keoben;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.keoben.mapper")
public class KeoBenBlogApplication {
	public static void main(String[] args) {
		SpringApplication.run(KeoBenBlogApplication.class, args);
	}
}
