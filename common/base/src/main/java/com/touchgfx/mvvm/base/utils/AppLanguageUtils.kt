package com.touchgfx.mvvm.base.utils

import android.content.Context
import com.blankj.utilcode.util.LanguageUtils
import com.blankj.utilcode.util.SPUtils
import java.util.*

object AppLanguageUtils {

    const val AUTO_LANGUAGE = "auto"

    private const val APP_LANGUAGE = "app_language"

    fun initLanguage(context: Context) {
        if (isAutoSystem()) {
            LanguageUtils.applySystemLanguage()
        } else {
            LanguageUtils.updateAppContextLanguage(getAppLocale()){}
        }
    }

    fun changeAppLanguage(langCode: String) {
        val language = LanguageUtils.getAppContextLanguage().language
        if (isAutoSystem(langCode)) {
            if (!language.equals(LanguageUtils.getSystemLanguage().language)) {
                LanguageUtils.applySystemLanguage()
            }
        } else {
            if (!language.equals(langCode.split("_")[0])) {
                LanguageUtils.applyLanguage(getAppLocale(langCode))
            }
        }
    }

    fun updateAppContextLanguage() {
        if (!isAutoSystem()) {
            LanguageUtils.updateAppContextLanguage(getAppLocale()){}
        }
    }

    fun saveAppLanguage(code: String) {
        SPUtils.getInstance().put(APP_LANGUAGE, code)
    }

    fun getAppLanguage(): String {
        return SPUtils.getInstance().getString(APP_LANGUAGE, AUTO_LANGUAGE)
    }

    fun getAppLocale(): Locale {
        return getAppLocale(getAppLanguage())
    }

    private fun getAppLocale(langCode: String): Locale {
        return when {
            isAutoSystem(langCode) -> LanguageUtils.getSystemLanguage()
            else -> {
                val language = langCode.split("_")[0]
                val country = langCode.split("_")[1]
                Locale(language, country)
            }
        }
    }

    fun isAutoSystem() = isAutoSystem(getAppLanguage())

    private fun isAutoSystem(langCode: String) = langCode == AUTO_LANGUAGE

}