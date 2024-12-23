package com.touchgfx.mvvm.base

import android.os.Bundle
import androidx.lifecycle.ViewModel
import androidx.viewbinding.ViewBinding

/**
 * @company TouchGFX
 * @author chenxiangbo
 * @date 2021/3/19 15:32
 * @desc 用来规范{@link BaseActivity} 和{@link BaseFragment} 风格。
 */
interface IView<VM : ViewModel, VB : ViewBinding> {
    /**
     * 页面初始化之前需要做的操作
     */
    fun preInit()

    /**
     * 初始化ViewBinding
     */
    fun initViewBinding(): VB

    /**
     * 初始化数据
     * @param savedInstanceState
     */
    fun initData(savedInstanceState: Bundle?)

    /**
     * 初始化View
     */
    fun initView()

    /**
     * 创建ViewModel
     * @return
     */
    fun createViewModel(): VM
}