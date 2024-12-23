package com.touchgfx.mvvm.base.livedata

import android.text.TextUtils
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer

/**
 * @company TouchGFX
 * @author chenxiangbo
 * @date 2021/3/19 19:23
 * @desc 提供观察消息事件
 */
class MessageEvent : SingleLiveEvent<String?>() {
    fun observe(owner: LifecycleOwner?, observer: MessageObserver) {
        super.observe(owner!!, Observer { t -> //过滤空消息
            if (TextUtils.isEmpty(t)) {
                return@Observer
            }
            observer.onNewMessage(t!!)
        })
    }

    interface MessageObserver {
        fun onNewMessage(message: String)
    }
}