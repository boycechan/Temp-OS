package com.touchgfx.retrofit.interceptor

import com.touchgfx.retrofit.RetrofitManager
import com.touchgfx.retrofit.Timeout
import okhttp3.Interceptor
import okhttp3.Response
import retrofit2.Invocation
import java.io.IOException
import kotlin.jvm.Throws

@Timeout(connectTimeout = 700)
class TimeoutInterceptor : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {


        //如果支持动态配置超时时长
        if (RetrofitManager.instance.isDynamicTimeout) {
            val request = chain.request()
            val invocation = request.tag(Invocation::class.java)
            if (invocation != null) {

                var iss =TimeoutInterceptor::class.java.isAnnotationPresent(Timeout::class.java)
                var timeout:Timeout? =null
                if(iss){
                    timeout=TimeoutInterceptor::class.java.getAnnotation(Timeout::class.java)
                }
                return chain.withConnectTimeout(timeout!!.connectTimeout, timeout!!.timeUnit)
                        .withReadTimeout(timeout!!.readTimeout, timeout!!.timeUnit)
                        .withWriteTimeout(timeout!!.writeTimeout, timeout!!.timeUnit)
                        .proceed(request)




//                val timeout = invocation.method().getAnnotation(
//                    Timeout::class.java
//                )
//                if (timeout != null) {
//                    return chain.withConnectTimeout(timeout.connectTimeout, timeout.timeUnit)
//                        .withReadTimeout(timeout.readTimeout, timeout.timeUnit)
//                        .withWriteTimeout(timeout.writeTimeout, timeout.timeUnit)
//                        .proceed(request)
//                }
            }
        }
        return chain.proceed(chain.request())
    }
}