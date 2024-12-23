package com.touchgfx.mvvm.base.http

import com.google.gson.Gson
import com.google.gson.TypeAdapter
import com.google.gson.reflect.TypeToken
import com.touchgfx.mvvm.base.bean.BaseResponse
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.ResponseBody
import okio.Buffer
import retrofit2.Converter
import retrofit2.Retrofit
import java.io.*
import java.lang.reflect.Type
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets

class CustomGsonConverterFactory private constructor(gson: Gson?) : Converter.Factory() {

    companion object {
        @JvmOverloads
        fun create(gson: Gson? = Gson()): CustomGsonConverterFactory {
            return CustomGsonConverterFactory(gson)
        }
    }

    private val gson: Gson

    init {
        if (gson == null) throw NullPointerException("gson == null")
        this.gson = gson
    }

    override fun responseBodyConverter(type: Type, annotations: Array<Annotation>, retrofit: Retrofit): Converter<ResponseBody, *> {
        val adapter = gson.getAdapter(TypeToken.get(type))
        return CustomGsonResponseBodyConverter(gson, adapter)
    }

    override fun requestBodyConverter(type: Type, parameterAnnotations: Array<Annotation>, methodAnnotations: Array<Annotation>, retrofit: Retrofit): Converter<*, RequestBody> {
        val adapter = gson.getAdapter(TypeToken.get(type))
        return CustomGsonRequestBodyConverter(gson, adapter)
    }

    internal inner class CustomGsonRequestBodyConverter<T>(private val gson: Gson, private val adapter: TypeAdapter<T>) : Converter<T, RequestBody> {
        private val MEDIA_TYPE = "application/json; charset=UTF-8".toMediaTypeOrNull()
        private val UTF_8 = Charset.forName("UTF-8")

        @Throws(IOException::class)
        override fun convert(value: T): RequestBody {
            val buffer = Buffer()
            val writer: Writer = OutputStreamWriter(buffer.outputStream(), UTF_8)
            val jsonWriter = gson.newJsonWriter(writer)
            adapter.write(jsonWriter, value)
            jsonWriter.close()
            return RequestBody.create(MEDIA_TYPE, buffer.readByteString())
        }
    }

    internal inner class CustomGsonResponseBodyConverter<T>(private val gson: Gson, private val adapter: TypeAdapter<T>) : Converter<ResponseBody, T> {
        @Throws(IOException::class)
        override fun convert(value: ResponseBody): T {
            val response = value.string()
            val baseResponse = gson.fromJson(response, BaseResponse::class.java)
            //核心代码:  判断 status 是否是后台定义的正常值
            if (!baseResponse.isSuccess()) {
                value.close()
                throw ApiException(baseResponse.code, baseResponse.message)
            }
            val contentType = value.contentType()
            val charset = if (contentType != null) contentType.charset(StandardCharsets.UTF_8) else StandardCharsets.UTF_8
            val inputStream: InputStream = ByteArrayInputStream(response.toByteArray())
            val reader: Reader = InputStreamReader(inputStream, charset)
            val jsonReader = gson.newJsonReader(reader)
            return try {
                adapter.read(jsonReader)
            } finally {
                value.close()
            }
        }
    }

}