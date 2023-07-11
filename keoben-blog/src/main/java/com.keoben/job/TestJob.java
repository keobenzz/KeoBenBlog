package com.keoben.job;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class TestJob {

	//从每一分钟的0秒开始,每隔5秒执行一次
	//秒 分 时 日 月 星期
	//@Scheduled(cron = "0/5 * * * * ?")
	public void testJod() {
		//要执行的代码
		System.out.println("定时执行");
	}

}
