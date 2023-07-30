package com.sky.annotation;

import com.sky.enumeration.OperationType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * ymy
 * 2023/7/29 - 21 : 52
 *
 * 创建自定义注解 AutoFill
 *
 **/


@Target(ElementType.METHOD)  // 只能标记在方法上
@Retention(RetentionPolicy.RUNTIME)
public @interface AutoFill {
    OperationType value();
}
