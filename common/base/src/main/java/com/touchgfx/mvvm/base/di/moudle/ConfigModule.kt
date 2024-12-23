package com.touchgfx.mvvm.base.di.moudle

import android.content.Context
import androidx.annotation.Nullable
import androidx.room.RoomDatabase
import com.touchgfx.mvvm.base.config.AppliesOptions
import com.touchgfx.mvvm.base.config.FrameConfigModule
import com.touchgfx.mvvm.base.config.ManifestParser
import com.touchgfx.retrofit.RetrofitManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import javax.inject.Singleton

/**
 * @company TouchGFX
 * @author chenxiangbo
 * @date 2021/3/19 19:23
 * @desc
 */
@InstallIn(SingletonComponent::class)
@Module
class ConfigModule {

    @Singleton
    @Provides
    fun provideBaseUrl(builder: Builder): HttpUrl? {
        var baseUrl = builder.baseUrl
        if (baseUrl == null) { //如果 mBaseUrl 为空表示没有在自定义配置 FrameConfigModule 中配过 BaseUrl
            baseUrl = RetrofitManager.instance.baseUrl
        }
        return baseUrl
    }

    @Singleton
    @Provides
    @Nullable
    fun provideRetrofitOptions(builder: Builder): AppliesOptions.RetrofitOptions? {
        return builder.retrofitOptions
    }

    @Singleton
    @Provides
    @Nullable
    fun provideOkHttpClientOptions(builder: Builder): AppliesOptions.OkHttpClientOptions? {
        return builder.okHttpClientOptions
    }

    @Singleton
    @Provides
    @Nullable
    fun provideGsonOptions(builder: Builder): AppliesOptions.GsonOptions? {
        return builder.gsonOptions
    }

    @Singleton
    @Provides
    @Nullable
    fun provideInterceptorConfigOptions(builder: Builder): AppliesOptions.InterceptorConfigOptions? {
        return builder.interceptorConfigOptions
    }

    @Singleton
    @Provides
    fun provideRoomDatabaseOptions(builder: Builder): AppliesOptions.RoomDatabaseOptions<RoomDatabase>? {
        return if (builder.roomDatabaseOptions != null) {
            builder.roomDatabaseOptions
        } else object : AppliesOptions.RoomDatabaseOptions<RoomDatabase> {
            override fun applyOptions(it: RoomDatabase.Builder<RoomDatabase>) {}
        }
    }

    @Singleton
    @Provides
    fun provideConfigModuleBuilder(@ApplicationContext context: Context): Builder {
        val builder: Builder = newBuilder()
        //解析配置
        val modules: List<FrameConfigModule> = ManifestParser(context).parse()
        //遍历配置
        for (configModule in modules) {
            //如果启用则申请配置参数
            if (configModule.isManifestParsingEnabled()) {
                configModule.applyOptions(context, builder)
            }
        }
        return builder
    }

    companion object {
        fun newBuilder(): Builder {
            return Builder()
        }
    }

    class Builder {

        var baseUrl: HttpUrl? = null
        var retrofitOptions: AppliesOptions.RetrofitOptions? = null
        var okHttpClientOptions: AppliesOptions.OkHttpClientOptions? = null
        var gsonOptions: AppliesOptions.GsonOptions? = null
        var interceptorConfigOptions: AppliesOptions.InterceptorConfigOptions? = null
        var roomDatabaseOptions: AppliesOptions.RoomDatabaseOptions<RoomDatabase>? = null

        fun baseUrl(baseUrl: String): Builder {
            this.baseUrl = baseUrl.toHttpUrlOrNull()
            return this
        }

        fun baseUrl(baseUrl: HttpUrl): Builder {
            this.baseUrl = baseUrl
            return this
        }

        fun retrofitOptions(retrofitOptions: AppliesOptions.RetrofitOptions?): Builder {
            this.retrofitOptions = retrofitOptions
            return this
        }

        fun okHttpClientOptions(okHttpClientOptions: AppliesOptions.OkHttpClientOptions?): Builder {
            this.okHttpClientOptions = okHttpClientOptions
            return this
        }

        fun gsonOptions(gsonOptions: AppliesOptions.GsonOptions?): Builder {
            this.gsonOptions = gsonOptions
            return this
        }

        fun interceptorConfigOptions(interceptorConfigOptions: AppliesOptions.InterceptorConfigOptions?): Builder {
            this.interceptorConfigOptions = interceptorConfigOptions
            return this
        }

        fun roomDatabaseOptions(roomDatabaseOptions: AppliesOptions.RoomDatabaseOptions<RoomDatabase>?): Builder {
            this.roomDatabaseOptions = roomDatabaseOptions
            return this
        }
    }


}