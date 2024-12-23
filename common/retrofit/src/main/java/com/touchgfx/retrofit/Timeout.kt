package com.touchgfx.retrofit

import java.lang.annotation.Documented
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy
import java.util.concurrent.TimeUnit

/**
 * 标记超时时长，用于支持动态改变超时时长
 */
@Documented
@Target(
    AnnotationTarget.FUNCTION,
    AnnotationTarget.PROPERTY_GETTER,
    AnnotationTarget.PROPERTY_SETTER,
        AnnotationTarget.CLASS,

        )
@Retention(RetentionPolicy.RUNTIME)
annotation class Timeout(
    val timeUnit: TimeUnit = TimeUnit.MINUTES,
    /**
     * 连接超时 时长（默认单位：秒）
     * @return
     */
    val connectTimeout: Int = 10,
    /**
     * 读取超时 时长（默认单位：秒）
     * @return
     */
    val readTimeout: Int = 10,
    /**
     * 写入超时 时长（默认单位：秒）
     * @return
     */
    val writeTimeout: Int = 10
) 