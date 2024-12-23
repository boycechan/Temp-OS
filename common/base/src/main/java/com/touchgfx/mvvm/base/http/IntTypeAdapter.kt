package com.touchgfx.mvvm.base.http

import android.text.TextUtils
import com.google.gson.JsonParseException
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter
import com.touchgfx.mvvm.base.http.IntTypeAdapter
import java.io.IOException

/**
 * @author boyce.chan
 * @company TouchGFX
 * @date 2022/6/11 18:23
 * @desc IntTypeAdapter
 */
class IntTypeAdapter : TypeAdapter<Int?>() {
    @Throws(IOException::class)
    override fun write(out: JsonWriter, value: Int?) {
        if (value == null) {
            out.nullValue()
        } else {
            out.value(value)
        }
    }

    @Throws(IOException::class)
    override fun read(`in`: JsonReader): Int? {
        val peek = `in`.peek()
        return when (peek) {
            JsonToken.BOOLEAN -> if (`in`.nextBoolean()) 1 else 0 //如果为true则返回为int的1，false返回0.
            JsonToken.NULL -> {
                `in`.nextNull()
                null
            }
            JsonToken.NUMBER -> `in`.nextInt()
            JsonToken.STRING -> toInteger(`in`.nextString())
            else -> throw JsonParseException("Expected BOOLEAN or NUMBER but was $peek")
        }
    }

    companion object {
        /**
         * true  TURE 都为true
         * "0" 为 false
         * "1" 为 true
         *
         * @param name
         * @return
         */
        fun toInteger(name: String): Int {
            if (TextUtils.isEmpty(name)) {
                return 0
            } else {
                if (name.equals("true", ignoreCase = true)) {
                    return 1
                } else if (name.equals("false", ignoreCase = true)) {
                    return 0
                } else if (name == "1") {
                    return 1
                } else if (name == "0") {
                    return 0
                }
            }
            return 0
        }
    }
}