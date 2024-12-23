package com.touchgfx.mvvm.base

/**
 * @company TouchGFX
 * @author chenxiangbo
 * @date 2021/3/19 16:37
 * @desc
 */
interface ILoading {
    /**
     * 显示加载
     */
    fun showLoading(cancelable: Boolean = true)

    /**
     * 隐藏加载
     */
    fun hideLoading()
}