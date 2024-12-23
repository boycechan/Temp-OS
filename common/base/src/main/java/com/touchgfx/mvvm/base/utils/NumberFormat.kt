package com.touchgfx.mvvm.base.utils

/**
 * @author: BINGO
 * @date: 2020/11/04
 */
object NumberFormat {
    fun formatNum(number: Long, unit: Int): String {
        val f = number / unit.toDouble()
        return String.format("%.2f", f)
    }

    fun formatNum(number: Int, unit: Int): String {
        val f = number / unit.toDouble()
        return String.format("%.2f", f)
    }

    fun formatNum(number: Double): String {
        return String.format("%.2f", number)
    }
}