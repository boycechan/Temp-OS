package com.touchgfx.mvvm.base.http.interceptor

import okhttp3.Interceptor
import okhttp3.RequestBody
import okhttp3.Response
import okhttp3.ResponseBody
import okio.Buffer
import timber.log.Timber
import java.io.IOException

/**
 * @author chenxiangbo
 * @company TouchGFX
 * @date 2021/3/19 19:53
 * @desc log日志拦截器
 */
class LogInterceptor : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        Timber.i("${request.method} -> ${request.url}")
        Timber.i("Headers:${request.headers}")
        if (request.body != null) {
            val mediaType = request.body!!.contentType()
            if (mediaType != null) {
                Timber.i("RequestContentType:$mediaType")
            }
            if (mediaType == null || !multipartType.equals(mediaType.type, ignoreCase = true)) {
                Timber.i("RequestBody:${bodyToString(request.body)}")
            }
        }
        val response = chain.proceed(chain.request())
        val mediaType = response.body!!.contentType()
        val responseBody = response.body!!.string()
        Timber.d("ResponseBody:$responseBody")
        return response.newBuilder()
                .body(ResponseBody.create(mediaType, responseBody))
                .build()
    }

    private fun bodyToString(request: RequestBody?): String? {
        if (request != null) {
            try {
                val copy: RequestBody = request
                val buffer = Buffer()
                copy.writeTo(buffer)
                return buffer.readUtf8()
            } catch (e: Exception) {
                Timber.e(e, "Did not work.")
            }
        }
        return null
    }

    companion object {
        private const val multipartType = "multipart"
    }
}