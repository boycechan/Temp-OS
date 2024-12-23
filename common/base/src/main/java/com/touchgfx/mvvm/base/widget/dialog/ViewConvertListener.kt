package com.touchgfx.mvvm.base.widget.dialog


/**
 * @company TouchGFX
 * @author chenxiangbo
 * @date 2021/3/26 19:28
 * @desc ViewConvertListener
 */
open interface ViewConvertListener {

    fun convertView(holder: ViewHolder, dialog: BaseDialog)

}