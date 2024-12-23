package com.touchgfx.mvvm.base.extension

import android.graphics.drawable.Drawable
import android.view.MotionEvent
import android.view.View.OnTouchListener
import android.widget.EditText


fun EditText.setOnDrawableRightClick(onClick: () -> Unit) {
    this.setOnTouchListener(OnTouchListener { v, event ->
        // et.getCompoundDrawables()得到一个长度为4的数组，分别表示左右上下四张图片
        val drawable: Drawable = this.compoundDrawables[2] ?: return@OnTouchListener false
        //如果右边没有图片，不再处理
        if (event.action != MotionEvent.ACTION_UP) {
            return@OnTouchListener false
        }
        if (event.x > (this.width - this.paddingRight - drawable.intrinsicWidth)) {
            onClick()
        }
        false
    })
}