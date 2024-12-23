package com.touchgfx.mvvm.base.livedata

import androidx.annotation.IntDef
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer

/**
 * @company TouchGFX
 * @author chenxiangbo
 * @date 2021/3/19 19:23
 * @desc 提供观察状态事件
 */
class StatusEvent : SingleLiveEvent<Int?>() {
    fun observe(owner: LifecycleOwner?, observer: StatusObserver) {
        super.observe(owner!!, Observer { t ->
            if (t != null) {
                observer.onStatusChanged(t)
            }
        })
    }

    interface StatusObserver {
        fun onStatusChanged(@Status status: Int)
    }

    /**
     * 状态
     */
    @IntDef(Status.LOADING, Status.SUCCESS, Status.FAILURE, Status.ERROR)
    @kotlin.annotation.Retention(AnnotationRetention.SOURCE)
    annotation class Status {
        companion object {
            const val LOADING = 0
            const val SUCCESS = 1
            const val FAILURE = 2
            const val ERROR = 3
        }
    }
}