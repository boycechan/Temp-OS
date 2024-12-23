package com.touchgfx.mvvm.base.widget.toolbar

import android.graphics.drawable.Drawable
import android.widget.TextView

/**
 * @company TouchGFX
 * @author chenxiangbo
 * @date 2021/3/26 17:17
 * @desc TooolBarUtil
 */
object ViewUtil {

    fun setDrawablePadding(tv: TextView, resId: Int, dimension: Int) {
        require(!(dimension < 0 || dimension > 3)) { "wow ,not so good" }
        val dimen = arrayOf<Drawable?>(null, null, null, null)
        val d = if (resId == -1) null else tv.context.resources.getDrawable(resId)
        d?.setBounds(0, 0, d.minimumWidth, d.minimumHeight)
        dimen[dimension] = d
        tv.setCompoundDrawables(dimen[0], dimen[1], dimen[2], dimen[3])
    }

    fun setDrawablePadding(tv: TextView, resId: Int, dimension: Int, width: Int) {
        require(!(dimension < 0 || dimension > 3)) { "wow ,not so good" }
        val dimen = arrayOf<Drawable?>(null, null, null, null)
        val d = if (resId == -1) null else tv.context.resources.getDrawable(resId)
        d?.setBounds(0, 0, width, width)
        dimen[dimension] = d
        tv.setCompoundDrawables(dimen[0], dimen[1], dimen[2], dimen[3])
    }


    fun setDrawablePadding(tv: TextView, resId: Int, dimension: Int, width: Int, height: Int) {
        require(!(dimension < 0 || dimension > 3)) { "wow ,not so good" }
        val dimen = arrayOf<Drawable?>(null, null, null, null)
        val d = if (resId == -1) null else tv.context.resources.getDrawable(resId)
        d?.setBounds(0, 0, width, height)
        dimen[dimension] = d
        tv.setCompoundDrawables(dimen[0], dimen[1], dimen[2], dimen[3])
    }

}