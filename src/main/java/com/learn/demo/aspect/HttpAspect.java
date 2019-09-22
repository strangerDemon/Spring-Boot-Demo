package com.learn.demo.aspect;

import javax.servlet.http.HttpServletRequest;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;


/*
 * @author demo
 * @version 1.0.0
 * @date 2019/9/15 19:34
 * @description aop 截取所有调用接口
 */

@Aspect
@Component
public class HttpAspect {

  private static final Logger logger = LoggerFactory.getLogger(HttpAspect.class);

  @Pointcut("execution(public * com.learn.demo.controller.*.*(..))")
  public void log() {

  }

  /**
   * 请求前.
   *
   * @param joinPoint 封装了SpringAop中切面方法的信息
   */
  @Before("log()")
  public void aspectBefore(JoinPoint joinPoint) {
    logger.info("aspectBefore");

    ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder
        .getRequestAttributes();
    assert attributes != null;
    HttpServletRequest request = attributes.getRequest();
    //url
    logger.info("url={}", request.getRequestURL());
    //method
    logger.info("method={}", request.getMethod());
    //ip
    logger.info("ip={}", request.getRemoteAddr());
    //类
    logger.info("class={}",
        joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName());
    //类
    logger.info("args={}", joinPoint.getArgs());
  }

  /**
   * 请求后.
   *
   * @param joinPoint joinPoint
   */
  @After("log()")
  public void aspectAfter(final JoinPoint joinPoint) {
    logger.info("aspectAfter");
  }

  /*
   * 返回前
   *
   * @param object 返回参数
   */
  @AfterReturning(returning = "object", pointcut = "log()")
  public void aspectReturn(Object object) {
    logger.info("response={}", object.toString());
  }
}