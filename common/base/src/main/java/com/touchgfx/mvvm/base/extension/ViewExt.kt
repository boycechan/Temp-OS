package com.touchgfx.mvvm.base.extension

import android.annotation.SuppressLint
import android.view.MotionEvent
import android.view.View
import timber.log.Timber
import kotlin.math.abs
import android.animation.ObjectAnimator

import android.animation.Keyframe

import android.animation.PropertyValuesHolder
import android.view.animation.Animation


/**
 * @company TouchGFX
 * @author chenxiangbo
 * @date 2021/3/31 12:00
 * @desc ViewExt
 */


/***
 * 防止快速点击-并且添加按下变暗效果
 */
@SuppressLint("ClickableViewAccessibility")
fun View.click(listener: (view: View) -> Unit) {
    val minTime = 500L
    var lastTime = 0L

    this.setOnClickListener {
        val tmpTime = System.currentTimeMillis()
        if (abs(tmpTime - lastTime) > minTime) {
            lastTime = tmpTime
            listener(it)
        } else {
            Timber.i("点击过快，取消触发")
        }
    }

    this.setOnTouchListener { view, motionEvent ->
        when (motionEvent.action) {
            MotionEvent.ACTION_DOWN -> {
                view.alpha = 0.7f//这里改变前，可以存储原view.alpha值，这样不会影响设置了alpha的view
            }
            MotionEvent.ACTION_UP -> {
                view.alpha = 1f//存储了alpha，取出值
            }
            MotionEvent.ACTION_CANCEL -> {
                view.alpha = 1f
            }
        }
        return@setOnTouchListener false
    }
}

fun View.show() {
    visibility = View.VISIBLE
}

fun View.hide() {
    visibility = View.GONE
}

fun View.invisible() {
    visibility = View.INVISIBLE
}

fun View.invisible(isInvisible: Boolean) {
    visibility = if (isInvisible) View.INVISIBLE else View.VISIBLE
}

fun View.visible(isVisible: Boolean) {
    visibility = if (isVisible) View.VISIBLE else View.GONE
}

/**
 * 动画抖动效果
 * @param scaleLarge 缩小倍数
 * @param shakeDegrees 放大倍数
 * @param duration 抖动角度
 * @param isInfinite 是否循环抖动
 */
fun View.startShakeByPropertyAnim(scaleSmall: Float, scaleLarge: Float, shakeDegrees: Float, duration: Long, isInfinite: Boolean) {
    val scaleXValuesHolder = PropertyValuesHolder.ofKeyframe(View.SCALE_X,
            Keyframe.ofFloat(0f, 1.0f),
            Keyframe.ofFloat(0.25f, scaleSmall),
            Keyframe.ofFloat(0.5f, scaleLarge),
            Keyframe.ofFloat(0.75f, scaleLarge),
            Keyframe.ofFloat(1.0f, 1.0f)
    )
    val scaleYValuesHolder = PropertyValuesHolder.ofKeyframe(View.SCALE_Y,
            Keyframe.ofFloat(0f, 1.0f),
            Keyframe.ofFloat(0.25f, scaleSmall),
            Keyframe.ofFloat(0.5f, scaleLarge),
            Keyframe.ofFloat(0.75f, scaleLarge),
            Keyframe.ofFloat(1.0f, 1.0f)
    )
    val rotateValuesHolder = PropertyValuesHolder.ofKeyframe(View.ROTATION,
            Keyframe.ofFloat(0f, 0f),
            Keyframe.ofFloat(0.1f, -shakeDegrees),
            Keyframe.ofFloat(0.2f, shakeDegrees),
            Keyframe.ofFloat(0.3f, -shakeDegrees),
            Keyframe.ofFloat(0.4f, shakeDegrees),
            Keyframe.ofFloat(0.5f, -shakeDegrees),
            Keyframe.ofFloat(0.6f, shakeDegrees),
            Keyframe.ofFloat(0.7f, -shakeDegrees),
            Keyframe.ofFloat(0.8f, shakeDegrees),
            Keyframe.ofFloat(0.9f, -shakeDegrees),
            Keyframe.ofFloat(1.0f, 0f)
    )
    val objectAnimator = ObjectAnimator.ofPropertyValuesHolder(this, scaleXValuesHolder, scaleYValuesHolder, rotateValuesHolder)
    objectAnimator.duration = duration
    if (isInfinite) {
        objectAnimator.repeatCount = Animation.INFINITE
    }
    objectAnimator.start()
}