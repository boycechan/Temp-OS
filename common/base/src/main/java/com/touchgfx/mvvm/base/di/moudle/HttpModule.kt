package com.touchgfx.mvvm.base.di.moudle

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.touchgfx.mvvm.base.config.AppliesOptions
import com.touchgfx.mvvm.base.http.InterceptorConfig
import com.touchgfx.mvvm.base.http.interceptor.LogInterceptor
import com.touchgfx.retrofit.RetrofitManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber
import java.security.KeyManagementException
import java.security.NoSuchAlgorithmException
import java.security.SecureRandom
import java.security.cert.X509Certificate
import javax.inject.Singleton
import javax.net.ssl.*


/**
 * @company TouchGFX
 * @author chenxiangbo
 * @date 2021/3/20 10:55
 * @desc
 */
@InstallIn(SingletonComponent::class)
@Module
class HttpModule {
    @Singleton
    @Provides
    fun provideRetrofit(builder: Retrofit.Builder, options: AppliesOptions.RetrofitOptions?): Retrofit {
        options?.applyOptions(builder)
        return builder.build()
    }

    @Singleton
    @Provides
    fun provideOkHttpClient(builder: OkHttpClient.Builder, options: AppliesOptions.OkHttpClientOptions?): OkHttpClient {
        options?.applyOptions(builder)
        return builder.build()
    }

    @Singleton
    @Provides
    fun provideGson(builder: GsonBuilder, options: AppliesOptions.GsonOptions?): Gson {
        options?.applyOptions(builder)
        return builder.create()
    }

    @Singleton
    @Provides
    fun provideRetrofitBuilder(httpUrl: HttpUrl?, client: OkHttpClient?,
                               gson: Gson?, config: InterceptorConfig): Retrofit.Builder {
        val builder = Retrofit.Builder()
        builder.baseUrl(httpUrl)
                .client(client)
        if (config.customConverterFactory != null) {
            builder.addConverterFactory(config.customConverterFactory)
        } else {
            if (config.isAddGsonConverterFactory) {
                builder.addConverterFactory(GsonConverterFactory.create(gson))
            }
        }
        return builder
    }

    @Singleton
    @Provides
    fun provideClientBuilder(config: InterceptorConfig): OkHttpClient.Builder {
        val builder: OkHttpClient.Builder = RetrofitManager.instance.createClientBuilder()
        if (config.isAddLog) {
            builder.addInterceptor(LogInterceptor())
        }
        ignoreSSL(builder)
        return builder
    }

    private fun ignoreSSL(builder: OkHttpClient.Builder) {
        val trustManager = arrayOf(
                object : X509TrustManager {

                    override fun checkClientTrusted(chain: Array<X509Certificate?>?, authType: String?) {}

                    override fun checkServerTrusted(chain: Array<X509Certificate?>?, authType: String?) {}

                    override fun getAcceptedIssuers(): Array<X509Certificate?> {
                        return arrayOfNulls<X509Certificate>(0)
                    }
                }
        )
        try {
            val sslContext: SSLContext = SSLContext.getInstance("TLS")
            Timber.d("SSL.Protocol: ${sslContext.protocol}")
            sslContext.init(null, trustManager, SecureRandom())
            val sslSocketFactory: SSLSocketFactory = sslContext.socketFactory
            builder.sslSocketFactory(sslSocketFactory, trustManager.first())
            builder.hostnameVerifier { hostname, session -> true }
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        } catch (e: KeyManagementException) {
            e.printStackTrace()
        }
    }

    @Singleton
    @Provides
    fun provideGsonBuilder(): GsonBuilder {
        return GsonBuilder()
    }

    @Singleton
    @Provides
    fun provideInterceptorConfig(builder: InterceptorConfig.Builder,
                                 options: AppliesOptions.InterceptorConfigOptions?): InterceptorConfig {
        options?.applyOptions(builder)
        return builder.build()
    }

    @Singleton
    @Provides
    fun provideInterceptorConfigBuilder(): InterceptorConfig.Builder {
        return InterceptorConfig.newBuilder()
    }
}