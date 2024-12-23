package com.touchgfx.mvvm.base.widget.dialog

import android.util.SparseArray
import android.view.View
import android.widget.TextView
import androidx.annotation.IdRes


/**
 * @company TouchGFX
 * @author chenxiangbo
 * @date 2021/3/26 19:30
 * @desc ViewHolder
 */
class ViewHolder private constructor(view: View) {

    companion object {
        fun create(view: View): ViewHolder {
            return ViewHolder(view)
        }
    }

    private val views: SparseArray<View?> = SparseArray()
    private val convertView: View = view

    /**
     * 获取View
     *
     * @param viewId
     * @param <T>
     * @return
    </T> */
    fun <T : View?> getView(@IdRes viewId: Int): T {
        var view: T? = views[viewId] as T?
        if (view == null) {
            view = convertView.findViewById<T>(viewId)
            views.put(viewId, view)
        }
        return view!!
    }

    /**
     * 设置文本
     *
     * @param viewId
     * @param text
     */
    fun setText(viewId: Int, text: String?) {
        val textView: TextView = getView(viewId)
        textView.text = text
    }

    /**
     * 设置字体颜色
     *
     * @param viewId
     * @param colorId
     */
    fun setTextColor(viewId: Int, colorId: Int) {
        val textView: TextView = getView(viewId)
        textView.setTextColor(colorId)
    }

    /**
     * 设置背景图片
     *
     * @param viewId
     * @param resId
     */
    fun setBackgroundResource(viewId: Int, resId: Int) {
        val view: View = getView(viewId)
        view.setBackgroundResource(resId)
    }

    /**
     * 设置背景颜色
     *
     * @param viewId
     * @param colorId
     */
    fun setBackgroundColor(viewId: Int, colorId: Int) {
        val view: View = getView(viewId)
        view.setBackgroundColor(colorId)
    }

    /**
     * 设置点击事件
     *
     * @param viewId
     * @param listener
     */
    fun setOnClickListener(viewId: Int, listener: View.OnClickListener?) {
        val view: View = getView(viewId)
        view.setOnClickListener(listener)
    }

}