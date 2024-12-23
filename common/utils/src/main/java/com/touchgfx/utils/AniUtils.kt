package com.touchgfx.utils

import android.view.View
import android.view.animation.Animation
import android.view.animation.OvershootInterpolator
import android.view.animation.TranslateAnimation

/**
 * 设置颜色直接使用colors.xml中定义的颜色即可
 */
fun View.initAnim(view : View)  {

    var animation= TranslateAnimation((10).toFloat(), (-10).toFloat(), 0F, 0F)
    animation.setInterpolator(OvershootInterpolator())
    animation.setDuration(20)
    animation.setRepeatCount(2)
    animation.setRepeatMode(Animation.REVERSE)
    view.startAnimation(animation)
}

