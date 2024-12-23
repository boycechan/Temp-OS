package com.touchgfx.mvvm.base.config

import android.content.Context
import androidx.room.RoomDatabase
import com.google.gson.GsonBuilder
import com.touchgfx.mvvm.base.di.moudle.ConfigModule
import com.touchgfx.mvvm.base.http.InterceptorConfig
import okhttp3.OkHttpClient
import retrofit2.Retrofit

/**
 * @company TouchGFX
 * @author chenxiangbo
 * @date 2021/3/19 17:59
 * @desc 为框架提供一些配置参数入口
 */
interface AppliesOptions {
    /**
     * 为框架提供一些配置参数入口
     * @param context
     * @param builder
     */
    fun applyOptions(context: Context, builder: ConfigModule.Builder)

    /**
     * 为框架中的[Retrofit]提供配置参数入口
     */
    interface RetrofitOptions {
        fun applyOptions(builder: Retrofit.Builder)
    }

    /**
     * 为框架中的[OkHttpClient]提供配置参数入口
     */
    interface OkHttpClientOptions {
        fun applyOptions(builder: OkHttpClient.Builder)
    }

    /**
     * 为框架中的[Gson]提供配置参数入口
     */
    interface GsonOptions {
        fun applyOptions(builder: GsonBuilder)
    }

    /**
     * 为框架中的[InterceptorConfig]提供配置参数入口
     */
    interface InterceptorConfigOptions {
        fun applyOptions(builder: InterceptorConfig.Builder)
    }

    /**
     * 为框架中的[Room]提供配置参数入口
     */
    interface RoomDatabaseOptions<T : RoomDatabase?> {
        fun applyOptions(builder: RoomDatabase.Builder<T>)
    }
}