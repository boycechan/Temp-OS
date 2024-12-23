package com.touchgfx.mvvm.base.widget

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.view.Gravity
import com.touchgfx.mvvm.base.R

/**
 * @company TouchGFX
 * @author chenxiangbo
 * @date 2021/3/24 15:41
 * @desc BaseProgressDialog
 */
open class BaseProgressDialog : Dialog {
    companion object {
        fun newInstance(context: Context): BaseProgressDialog {
            return BaseProgressDialog(context)
        }
    }

    constructor(context: Context) : this(context, R.style.progress_dialog)

    constructor(context: Context, themeResId: Int) : super(context, themeResId) {
        initUI()
    }

    constructor(context: Context, cancelable: Boolean,
                cancelListener: DialogInterface.OnCancelListener?) : super(context, cancelable, cancelListener) {
        initUI()
    }

    protected open fun initUI() {
        window!!.attributes.gravity = Gravity.CENTER
        setCanceledOnTouchOutside(false)
    }
}