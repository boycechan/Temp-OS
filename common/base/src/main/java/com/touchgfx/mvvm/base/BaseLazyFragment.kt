package com.touchgfx.mvvm.base

import android.os.Bundle
import android.view.View
import androidx.viewbinding.ViewBinding

/**
 * @company TouchGFX
 * @author chenxiangbo
 * @date 2021/3/20 15:45
 * @desc 懒加载fragment
 */
abstract class BaseLazyFragment<VM : BaseViewModel<BaseModel>, VB : ViewBinding> :
        BaseFragment<VM, VB>() {
    /**
     * 是否可见
     */
    var isVisibled = false
        private set

    /**
     * 是否初次加载
     */
    private var isFirstLoad = false

    /**
     * 是否准备好加载
     */
    protected var isPrepared = false
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        isFirstLoad = true
        super.onViewCreated(view, savedInstanceState)
        isPrepared = true
    }

    override fun onResume() {
        super.onResume()
        onVisible()
    }

    override fun onPause() {
        super.onPause()
        onInvisible()
    }

    /**
     * [Fragment]可见
     */
    protected open fun onVisible() {
        isVisibled = true
        lazyLoad()
    }

    /**
     * [Fragment]不可见
     */
    protected open fun onInvisible() {
        isVisibled = false
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (hidden) {
            onInvisible()
        } else {
            onVisible()
        }
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser) {
            onVisible()
        } else {
            onInvisible()
        }
    }

    /**
     * 懒加载，触发[.onLazyLoad]
     */
    private fun lazyLoad() {
        if (isFirstLoad && isPrepared && isVisibled) {
            //保证只加载一次
            isFirstLoad = false
            onLazyLoad()
        }
    }

    /**
     * 懒加载
     */
    abstract fun onLazyLoad()
}