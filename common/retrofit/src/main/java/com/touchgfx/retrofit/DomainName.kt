package com.touchgfx.retrofit

/**
 * 标记域名别名，用于支持动态改变 BaseUrl
 */
@MustBeDocumented
@Target(
    AnnotationTarget.FUNCTION,
    AnnotationTarget.PROPERTY_GETTER,
    AnnotationTarget.PROPERTY_SETTER
)
@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
annotation class DomainName(val value: String)