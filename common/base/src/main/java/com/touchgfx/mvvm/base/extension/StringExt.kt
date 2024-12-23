package com.touchgfx.mvvm.base.extension

import com.blankj.utilcode.util.RegexUtils
import java.util.*

/**
 * @company TouchGFX
 * @author chenxiangbo
 * @date 2021/4/12 16:18
 * @desc StringExt
 */
val POSITIVE_INTEGER_REG = "^[1-9]+\\d*$".toRegex()

val REGEX_EMAIL = "^([a-z0-9A-Z]+[-_|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$".toRegex()

fun String.isPositiveInt(): Boolean {
    return this.matches(POSITIVE_INTEGER_REG)
}

/**
 * 格式化浮点弄数据(避免点变成逗号)
 */
fun String.ff(vararg args: Any?): String = java.lang.String.format(Locale.ENGLISH, this, *args)

fun String.isEmail(): Boolean {
    if (!this.contains("@")) {
        return false
    }
    return RegexUtils.isEmail(this)
}

fun CharSequence.isEmail(): Boolean {
    if (!this.contains("@")) {
        return false
    }
    return RegexUtils.isEmail(this)
}

/**
 * 获取字符数量 汉字占2个长度，英文占1个长度
 *
 * @param text
 * @return
 */
fun String.getTextLength(): Int {
    var length = 0
    for (element in this) {
        if (element.code > 255) {
            length += 2
        } else {
            length++
        }
    }
    return length
}