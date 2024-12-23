package com.touchgfx.mvvm.base.http

import retrofit2.Converter

/**
 * @company TouchGFX
 * @author chenxiangbo
 * @date 2021/3/19 19:53
 * @desc
 */
class InterceptorConfig private constructor(builder: Builder) {
    /**
     * 是否添加 支持打印 Http 相关日志
     */
    var isAddLog = builder.isAddLog
        private set

    /**
     * 是否添加支持 Gson 默认转换工厂
     */
    var isAddGsonConverterFactory = builder.isAddGsonConverterFactory
        private set

    var customConverterFactory: Converter.Factory? = builder.customConverterFactory


    companion object {
        fun newBuilder(): Builder {
            return Builder()
        }
    }

    class Builder {
        /**
         * 是否添加 支持打印 Http 相关日志
         * @param addLog
         * @return
         */
        var isAddLog = true

        /**
         * 是否添加 支持 Gson 默认转换工厂
         * @param addGsonConverterFactory
         * @return
         */
        var isAddGsonConverterFactory = true

        /**
         * 自定义网络请求转换工厂
         */
        var customConverterFactory: Converter.Factory? = null

        fun build(): InterceptorConfig {
            return InterceptorConfig(this)
        }
    }
}