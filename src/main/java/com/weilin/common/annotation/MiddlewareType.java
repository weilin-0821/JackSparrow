package com.weilin.common.annotation;

import java.lang.annotation.*;

/**
 * 自定义注解
 * ElementType.TYPE：可以用在类、接口、枚举、注解类型上
 * @Retention(RetentionPolicy.RUNTIME)： 这个注解在运行时可以被反射读取
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface MiddlewareType {
    String value();
}
