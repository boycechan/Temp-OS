package com.touchgfx.retrofit.parser

import android.text.TextUtils
import androidx.collection.LruCache
import okhttp3.HttpUrl
import java.util.*

class DomainParser @JvmOverloads constructor(maxSize: Int = 100) : HttpUrlParser {
    /**
     * 缓存支持多个 BaseUrl相关的 Url
     */
    private val mCacheUrlMap: LruCache<String, String> = LruCache(maxSize)

    /**
     * 解析 httpUrl, 将 httpUrl 的 baseUrl 换成 domainUrl
     * @param domainUrl
     * @param httpUrl
     * @return
     */
    override fun parseHttpUrl(domainUrl: HttpUrl, httpUrl: HttpUrl): HttpUrl {
        val builder = httpUrl.newBuilder()
        //优先从缓存里面取
        val url = mCacheUrlMap[getUrlKey(domainUrl, httpUrl)]
        if (TextUtils.isEmpty(url)) {
            for (i in 0 until httpUrl.pathSize) {
                builder.removePathSegment(0)
            }
            val pathSegments: MutableList<String> = ArrayList()
            pathSegments.addAll(domainUrl.encodedPathSegments)
            httpUrl.encodedPathSegments.forEach {
                if (!pathSegments.contains(it)) {
                    pathSegments.add(it)
                }
            }
            for (pathSegment in pathSegments) {
                builder.addEncodedPathSegment(pathSegment)
            }
        } else {
            if (url != null) {
                builder.encodedPath(url)
            }
        }
        val resultUrl = builder.scheme(domainUrl.scheme)
                .host(domainUrl.host)
                .port(domainUrl.port)
                .build()
        updateThreadName(resultUrl)
        if (TextUtils.isEmpty(url)) {
            //缓存 Url
            mCacheUrlMap.put(getUrlKey(domainUrl, httpUrl), resultUrl.encodedPath)
        }
        return resultUrl
    }

    /**
     * 更新线程名称
     * @param httpUrl
     */
    private fun updateThreadName(httpUrl: HttpUrl) {
        Thread.currentThread().name = "OkHttp " + httpUrl.redact()
    }

    /**
     * 获取用于缓存 Url 时的 key，
     * @param domainUrl
     * @param currentUrl
     * @return
     */
    private fun getUrlKey(domainUrl: HttpUrl, currentUrl: HttpUrl): String {
        return String.format("%s_%s", domainUrl.encodedPath, currentUrl.encodedPath)
    }

}