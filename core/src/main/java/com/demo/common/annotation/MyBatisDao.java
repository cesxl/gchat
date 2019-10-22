package com.demo.common.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * 标识 MyBatis 的 DAO
 *
 * @author gc
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Component
public @interface MyBatisDao {

    String value() default "";

}