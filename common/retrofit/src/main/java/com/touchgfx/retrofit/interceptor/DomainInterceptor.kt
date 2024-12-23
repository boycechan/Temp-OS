package com.touchgfx.retrofit.interceptor

import com.touchgfx.retrofit.DomainName
import com.touchgfx.retrofit.RetrofitManager
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import retrofit2.Invocation
import java.io.IOException
import kotlin.jvm.Throws

class DomainInterceptor : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        return chain.proceed(processRequest(chain.request()))
    }

    /**
     * 处理请求，切换 BaseUrl
     * @param request
     * @return
     */
    private fun processRequest(request: Request): Request {
        //如果支持动态配置 BaseUrl
        if (RetrofitManager.instance.isDynamicDomain) {
            val invocation = request.tag(Invocation::class.java)
            if (invocation != null) {
                val domainName =
                    invocation.method().getAnnotation(DomainName::class.java)
                if (domainName != null) {
                    val httpUrl = RetrofitManager.instance
                        .obtainParserDomainUrl(domainName.value, request.url)
                    //如果不为空，则切换 BaseUrl
                    if (httpUrl != null) {
                        return request.newBuilder()
                            .url(httpUrl)
                            .build()
                    }
                }
            }
            val baseUrl = RetrofitManager.instance.baseUrl
            if (baseUrl != null) {
                val httpUrl =
                    RetrofitManager.instance.parseHttpUrl(baseUrl, request.url)
                //如果不为空，则切换 BaseUrl
                if (httpUrl != null) {
                    return request.newBuilder()
                        .url(httpUrl)
                        .build()
                }
            }
        }
        return request
    }
}