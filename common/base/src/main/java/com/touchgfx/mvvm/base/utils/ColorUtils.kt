package com.touchgfx.mvvm.base.utils

import android.graphics.Color

/**
 * @company TouchGFX
 * @author boyce.chan
 * @date 2022/3/4 4:14 下午
 * @desc ColorUtils
 */
object ColorUtils {

    fun isLightColor(color: Int): Boolean {
        val darkness: Double = 1 - (0.299 * Color.red(color) + 0.587 * Color.green(color) + 0.114 * Color.blue(color)) / 255
        return darkness < 0.5
    }

}