package com.touchgfx.mvvm.base

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent

/**
 * @company TouchGFX
 * @author chenxiangbo
 * @date 2021/3/19 16:36
 * @desc
 */
interface IModel : LifecycleObserver {
    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy()
}