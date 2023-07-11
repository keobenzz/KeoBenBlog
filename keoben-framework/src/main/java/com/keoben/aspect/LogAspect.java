package com.keoben.aspect;

import com.alibaba.fastjson.JSON;
import com.keoben.annotation.SystemLog;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
@Aspect
@Slf4j
public class LogAspect {

	@Pointcut("@annotation(com.keoben.annotation.SystemLog)")
	public void pt() {

	}

	@Around("pt()")
	public Object printLog(ProceedingJoinPoint joinPoint) throws Throwable {
		//joinPoint即被增加方法的信息封装成的对象
		//ret 即目标方法执行完的返回值
		Object ret;
		try {
			handleBefore(joinPoint);
			ret = joinPoint.proceed();
			handleAfter(ret);
		} finally {
			// 结束后换行 System.lineSeparator()为系统的换行符
			log.info("=======End=======" + System.lineSeparator());
		}
		return ret;
	}

	private void handleBefore(ProceedingJoinPoint joinPoint) {

		ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		HttpServletRequest request = requestAttributes.getRequest();

		//过去被增强方法上的注解对象
		SystemLog systemlog = getSystemLog(joinPoint);

		log.info("=======Start=======");
		// 打印请求 URL
		log.info("URL            : {}", request.getRequestURL());
		// 打印描述信息
		log.info("BusinessName   : {}", systemlog.businessName());
		// 打印 Http method
		log.info("HTTP Method    : {}", request.getMethod());
		// 打印调用 controller 的全路径以及执行方法
		log.info("Class Method   : {}.{}", joinPoint.getSignature().getDeclaringTypeName(), joinPoint.getSignature().getName());
		// 打印请求的 IP
		log.info("IP             : {}", request.getRemoteHost());
		// 打印请求入参
		log.info("Request Args   : {}", JSON.toJSONString(joinPoint.getArgs()));
	}

	private void handleAfter(Object ret) {
		// 打印出参
		log.info("Response       : {}", JSON.toJSONString(ret));
	}

	private SystemLog getSystemLog(ProceedingJoinPoint joinPoint) {
		MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
		SystemLog systemLog = methodSignature.getMethod().getAnnotation(SystemLog.class);
		return systemLog;
	}
}
