package com.sunducation.waterflow.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component("springContextUtils")
public class SpringContextUtils implements ApplicationContextAware {

  private static ApplicationContext applicationContext = null;

  public static ApplicationContext getApplicationContext() {

    return applicationContext;

  }

  @Override
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {

    SpringContextUtils.applicationContext = applicationContext;

  }

  @SuppressWarnings("unchecked")

  public static <T> T getBean(String beanId) {

    return (T) applicationContext.getBean(beanId);

  }

  public static <T> T getBean(Class<T> requiredType) {

    return (T) applicationContext.getBean(requiredType);

  }


}