package com.touchgfx.mvvm.base.utils

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import com.touchgfx.mvvm.base.glide.ImageLoader

/**
 * @company TouchGFX
 * @author chaun
 * @date 2021/4/9
 * @desc edittetx输入框
 */

/**
 * 隐藏键盘
 */
fun View.hideKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(windowToken, 0)
}