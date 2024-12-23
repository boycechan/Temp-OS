package com.touchgfx.mvvm.base.livedata

import androidx.annotation.MainThread
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import timber.log.Timber
import java.util.concurrent.atomic.AtomicBoolean

/**
 * @company TouchGFX
 * @author chenxiangbo
 * @date 2021/3/19 19:23
 * @desc 提供观察单个对象{@link T}事件
 */
open class SingleLiveEvent<T> : MutableLiveData<T?>() {
    private val mPending =
        AtomicBoolean(false)

    @MainThread
    override fun observe(owner: LifecycleOwner, observer: Observer<in T?>) {
        if (hasActiveObservers()) {
            Timber.w("Multiple observers registered but only one will be notified of changes.")
        }

        // Observe the internal MutableLiveData
        super.observe(owner, Observer { t ->
            if (mPending.compareAndSet(true, false)) {
                observer.onChanged(t)
            }
        })
    }

    @MainThread
    override fun setValue(t: T?) {
        mPending.set(true)
        super.setValue(t)
    }

    /**
     * Used for cases where T is Void, to make calls cleaner.
     */
    @MainThread
    fun call() {
        value = null
    }
}