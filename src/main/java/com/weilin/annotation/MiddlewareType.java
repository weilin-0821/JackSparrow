package com.weilin.annotation;

import java.lang.annotation.*;

/**
 * 自定义注解
 * ElementType.TYPE：可以用在类、接口、枚举、注解类型上
 * @Retention(RetentionPolicy.RUNTIME)： 指定注解什么时候生效
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MiddlewareType {
    String value();
}
