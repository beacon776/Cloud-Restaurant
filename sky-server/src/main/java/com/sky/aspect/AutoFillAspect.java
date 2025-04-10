package com.sky.aspect;

import com.fasterxml.jackson.databind.ser.Serializers;
import com.sky.annotation.AutoFill;
import com.sky.constant.AutoFillConstant;
import com.sky.context.BaseContext;
import com.sky.enumeration.OperationType;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.reflect.MethodSignature;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.time.LocalDateTime;

/**
 * 自定义切面类，实现公共字段自动填充处理逻辑
 */
@Aspect
@Component
@Slf4j
public class AutoFillAspect {
	/**
	 * 切入点
	 */
	@Pointcut("execution(* com.sky.mapper.*.*(..)) && @annotation(com.sky.annotation.AutoFill)")
	public void autoFillPointCut() {
	}

	/**
	 * 前置通知
	 */

	@Before("autoFillPointCut()")
	public void autoFill(JoinPoint joinPoint) {
		log.info("开始进行公共字段自动填充......");
		// 获取操作类型
		MethodSignature signature = (MethodSignature) joinPoint.getSignature();
		AutoFill autoFill = signature.getMethod().getAnnotation(AutoFill.class);
		OperationType operationType = autoFill.value(); // 获取数据库操作类型

		//  获取方法参数 - 实体对象
		Object[] args = joinPoint.getArgs();
		if (args == null && args.length == 0) {
			return;
		}
		Object entity = args[0]; // 接收实体类的类型（类型不固定）
		// 准备id + now()
		LocalDateTime now = LocalDateTime.now();
		Long id = BaseContext.getCurrentId();

		// 赋值操作
		if (operationType == OperationType.INSERT) {
			try {
				Method setCreateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_TIME, LocalDateTime.class);
				Method setUpdateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);
				Method setCreateUser = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_USER, Long.class);
				Method setUpdateUser = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER, Long.class);
				setCreateTime.invoke(entity, now);
				setUpdateTime.invoke(entity, now);
				setCreateUser.invoke(entity, id);
				setUpdateUser.invoke(entity, id);
			} catch (Exception e) {
				e.printStackTrace();
			}

		} else if (operationType == OperationType.UPDATE) {
			try {
				Method setUpdateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);
				Method setUpdateUser = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER, Long.class);
				setUpdateTime.invoke(entity, now);
				setUpdateUser.invoke(entity, id);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
