package com.touchgfx.retrofit

import com.touchgfx.retrofit.interceptor.DomainInterceptor
import com.touchgfx.retrofit.interceptor.TimeoutInterceptor
import com.touchgfx.retrofit.parser.DomainParser
import com.touchgfx.retrofit.parser.HttpUrlParser
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.OkHttpClient
import java.util.*

/**
 * Retrofit帮助类
 *
 *
 * 主要功能介绍：
 * 1.支持管理多个 BaseUrl，且支持运行时动态改变
 * 2.支持接口自定义超时时长，满足每个接口动态定义超时时长
 *
 *
 *
 *
 * RetrofitHelper中的核心方法
 *
 * [.createClientBuilder] 创建 [OkHttpClient.Builder]初始化一些配置参数，用于支持多个 BaseUrl
 *
 * [.with] 传入 [OkHttpClient.Builder] 配置一些参数，用于支持多个 BaseUrl
 *
 * [.setBaseUrl] 和 [.setBaseUrl] 主要用于设置默认的 BaseUrl。
 *
 * [.putDomain] 和 [.putDomain] 主要用于支持多个 BaseUrl，且支持 BaseUrl 动态改变。
 *
 * [.setDynamicDomain] 设置是否支持 配置多个BaseUrl，且支持动态改变，一般会通过其他途径自动开启，此方法一般不会主动用到，只有在特殊场景下可能会有此需求，所以提供此方法主要用于提供更多种可能。
 *
 * [.setHttpUrlParser] 设置 HttpUrl解析器 , 当前默认采用的 [DomainParser] 实现类，你也可以自定义实现 [HttpUrlParser]
 *
 * 这里只是列出一些对外使用的核心方法，和相关的简单说明。如果想了解更多，可以查看对应的方法和详情。
 *
 */
class RetrofitManager private constructor() : HttpUrlParser {
    /**
     * 获取 BaseUrl
     * @return
     */
    /**
     * BaseUrl
     */
    var baseUrl: HttpUrl? = null
        private set

    /**
     * 存储支持多个 BaseUrl
     */
    private val mUrlMap: MutableMap<String, HttpUrl>

    /**
     * 是否支持 配置多个 BaseUrl，且支持动态改变
     * @return
     */
    /**
     * 设置是否支持 配置多个 BaseUrl，且支持动态改变，一般会通过其他途径自动开启，此方法一般不会用到，只有在特殊场景下可能会有此需求，所以提供此方法主要用于提供更多种可能。
     * 特殊场景：使用了多个可切换 BaseUrl 支持功能，但突然想终止切换BaseUrl，全部使用默认的 BaseUrl 时，可通过此方法禁用对多个BaseUrl的支持。
     *
     * @param dynamicDomain
     */
    /**
     * 是否是否支持 配置多个BaseUrl，且支持动态改变
     */
    var isDynamicDomain: Boolean

    /**
     * 是否支持 动态配置超时时长
     * @return
     */
    /**
     * 设置是否支持 动态配置超时时长，可以通过此方法禁用所有自定义的超时时间配置
     * @return 默认为{@code false}
     */
    /**
     * 是否支持 配置动态超时时长
     */
    var isDynamicTimeout: Boolean

    /**
     * Url 解析器
     */
    private var mHttpUrlParser: HttpUrlParser

    private object DomainHolder {
        val INSTANCE = RetrofitManager()
    }

    /**
     * 创建 [OkHttpClient.Builder]初始化一些配置参数，用于支持多个 BaseUrl
     * 相关方法 [.with] 同样具备此功能，两种选其一即可
     * @return
     */
    fun createClientBuilder(): OkHttpClient.Builder {
        return with(OkHttpClient.Builder())
    }

    /**
     * 传入 [OkHttpClient.Builder] 配置一些参数，用于支持多个 BaseUrl
     * 相关方法 [.createClientBuilder] 同样具备此功能，两种选其一即可
     * @param builder
     * @return
     */
    fun with(builder: OkHttpClient.Builder): OkHttpClient.Builder {
        return builder.addInterceptor(DomainInterceptor())
            .addInterceptor(TimeoutInterceptor())
    }

    /**
     * 设置 BaseUrl，前提条件：当 [.isDynamicDomain] 为`true` 时才支持动态改变。
     * 通过此方法，可以动态改变 [Request.url]中的 baseUrl，优先级低于[DomainName]
     * 只有在接口没有标记[DomainName]或者标记了，但没找到对应的 `domainUrl` 时，才能动态改变。
     * @param baseUrl
     */
    fun setBaseUrl(baseUrl: String) {
        setBaseUrl(baseUrl.toHttpUrlOrNull()!!)
    }

    /**
     * 设置 BaseUrl，前提条件：当 [.isDynamicDomain] 为`true` 时才支持动态改变。
     * 通过此方法，可以动态改变 [Request.url]中的 baseUrl，优先级低于[DomainName]
     * 只有在接口没有标记[DomainName]或者标记了，但没找到对应的 `domainUrl` 时，才能动态改变。
     * @param baseUrl
     */
    fun setBaseUrl(baseUrl: HttpUrl) {
        this.baseUrl = baseUrl
    }

    /**
     * 移除 BaseUrl
     */
    fun removeBaseUrl() {
        baseUrl = null
    }

    /**
     * 添加动态域名，当执行此操作时，则会自动启用[.isDynamicDomain]，并支持多个 BaseUrl
     * 在接口方法上添加 [DomainName] 标记对应的 `domainName`即可支持动态改变，
     * @param domainName 域名别名
     * @param domainUrl 域名对应的 BaseUrl
     */
    fun putDomain(domainName: String, domainUrl: String) {
        putDomain(domainName, domainUrl.toHttpUrlOrNull()!!)
    }

    /**
     * 添加动态域名，当执行此操作时，则会自动启用[.isDynamicDomain]，并支持多个 BaseUrl
     * 在接口方法上添加 [DomainName] 标记对应的 `domainName`即可支持动态改变，
     * @param domainName 域名别名
     * @param domainUrl 域名对应的 BaseUrl
     */
    fun putDomain(domainName: String, domainUrl: HttpUrl) {
        mUrlMap[domainName] = domainUrl
        isDynamicDomain = true
    }

    /**
     * 移除操作
     * @param domainName
     */
    fun removeDomain(domainName: String) {
        mUrlMap.remove(domainName)
    }

    /**
     * 通过 domainName 获取 [HttpUrl]
     * @param domainName
     * @return
     */
    operator fun get(domainName: String): HttpUrl? {
        return mUrlMap[domainName]
    }

    /**s
     * 清空支持多个 BaseUrl
     */
    fun clearDomain() {
        mUrlMap.clear()
        isDynamicDomain = false
    }

    /**
     * 取切换之后的 BaseUrl，如果没有则返回 [.mBaseUrl]，如果 [.mBaseUrl]也为空，则不切换，直接返回空
     * @param domainName
     * @param baseUrl
     * @return
     */
    @Synchronized
    fun obtainParserDomainUrl(domainName: String, baseUrl: HttpUrl): HttpUrl? {
        val domainUrl = get(domainName)
        if (domainUrl != null) {
            return parseHttpUrl(domainUrl, baseUrl)
        }
        //如果 mBaseUrl 不为空则，切换成 mBaseUrl
        return if (this.baseUrl != null) {
            parseHttpUrl(this.baseUrl!!, baseUrl)
        } else null
    }

    /**
     * 解析 httpUrl, 将 httpUrl 的 baseUrl 换成 domainUrl
     * @param domainUrl
     * @param httpUrl
     * @return
     */
    override fun parseHttpUrl(domainUrl: HttpUrl, httpUrl: HttpUrl): HttpUrl? {
        return mHttpUrlParser.parseHttpUrl(domainUrl, httpUrl)
    }

    /**
     * 设置 HttpUrl解析器 , 当前默认采用的 [DomainParser] 实现类，你也可以自定义实现 [HttpUrlParser]
     * @param httpUrlParser
     */
    fun setHttpUrlParser(httpUrlParser: HttpUrlParser) {
        mHttpUrlParser = httpUrlParser
    }

    companion object {
        val instance: RetrofitManager
            get() = DomainHolder.INSTANCE
    }

    init {
        mUrlMap = HashMap()
        mHttpUrlParser = DomainParser()
        isDynamicDomain = false
        isDynamicTimeout = true
    }
}