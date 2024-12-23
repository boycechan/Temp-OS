package com.touchgfx.mvvm.base.widget.dialog

import androidx.annotation.LayoutRes

/**
 * @company TouchGFX
 * @author chenxiangbo
 * @date 2021/3/26 19:27
 * @desc 公用样式Dialog
 */
class CommonDialog : BaseDialog() {

    companion object {
        fun newInstance(): CommonDialog {
            return CommonDialog()
        }
    }

    private var convertListener: ViewConvertListener? = null

    /**
     * 设置Dialog布局
     *
     * @param layoutId
     * @return
     */
    fun setLayoutId(@LayoutRes layoutId: Int): CommonDialog {
        mLayoutResId = layoutId
        return this
    }

    override fun setUpLayoutId(): Int {
        return mLayoutResId
    }

    override fun convertView(holder: ViewHolder, dialog: BaseDialog) {
        convertListener?.convertView(holder, dialog)
    }

    fun setConvertListener(convertListener: ViewConvertListener?): CommonDialog {
        this.convertListener = convertListener
        return this
    }

}

