package com.touchgfx.retrofit.parser

import okhttp3.HttpUrl

interface HttpUrlParser {
    /**
     * 解析 httpUrl, 将 httpUrl 的 baseUrl 换成 domainUrl
     * @param domainUrl
     * @param httpUrl
     * @return
     */
    fun parseHttpUrl(domainUrl: HttpUrl, httpUrl: HttpUrl): HttpUrl?
}